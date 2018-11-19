package com.benny.archetype.common.net.utils.log;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.LiteralConverter;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.helper.*;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.FileSize;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static ch.qos.logback.core.CoreConstants.UNBOUNDED_TOTAL_SIZE_CAP;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:43
 */
public class PatchedSizeAndTimeBasedRollingPolicy<E> extends TimeBasedRollingPolicy<E> {
    /** 文件名和触发策略对象字段名 */
    private static final String FILE_NAME_AND_TRIGGER_POLICY_FIELD_NAME = "timeBasedFileNamingAndTriggeringPolicy";

    /** 最大文件大小 */
    private FileSize maxFileSize;

    /**
     * @see TimeBasedRollingPolicy#start()
     */
    @Override
    public void start()
    {
        PatchedLogFNATP sizeAndTimeBasedFNATP = new PatchedLogFNATP();
        if (maxFileSize == null)
        {
            addError("未配置最大文件大小参数");
            return;
        }
        else
        {
            addInfo("每个日志文件大小都会限制在 [" + maxFileSize + "] 内.");
        }
        sizeAndTimeBasedFNATP.setMaxFileSize(maxFileSize);

        try
        {
            Field timeBasedFileNamingAndTriggeringPolicyField = TimeBasedRollingPolicy.class.getDeclaredField(
                    FILE_NAME_AND_TRIGGER_POLICY_FIELD_NAME);
            timeBasedFileNamingAndTriggeringPolicyField.setAccessible(true);
            timeBasedFileNamingAndTriggeringPolicyField.set(this, sizeAndTimeBasedFNATP);
            timeBasedFileNamingAndTriggeringPolicyField.setAccessible(false);
            addInfo("已向策略中载入 文件名触发器策略 对象: " + sizeAndTimeBasedFNATP.toString());
        }
        catch (Exception e)
        {
            addError("载入 文件名触发器策略 对象: " + sizeAndTimeBasedFNATP.toString() + "失败");
        }

        if (!isUnboundedTotalSizeCap() && totalSizeCap.getSize() < maxFileSize.getSize())
        {
            addError("全日志文件大小限制: " + totalSizeCap.toString() + " 不能小于最大日志文件大小限制: " + maxFileSize.getSize());
            return;
        }

        // most work is done by the parent
        super.start();
    }


    /**
     * 设置最大文件大小
     * @param aMaxFileSize 最大文件大小
     */
    public void setMaxFileSize(FileSize aMaxFileSize)
    {
        this.maxFileSize = aMaxFileSize;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return "c.q.l.core.rolling.SizeAndTimeBasedRollingPolicy@" + this.hashCode();
    }

}

/**
 * UOP日志删除器, 解决了无法删除序号为1000以上的日志文件的问题
 * Created by flamhaze on 2017/6/8.
 */
class PatchedLogFNATP extends SizeAndTimeBasedFNATP
{
    /**
     * @see SizeAndTimeBasedFNATP#createArchiveRemover()
     */
    @Override
    protected ArchiveRemover createArchiveRemover()
    {
        return new PatchedLogArchiveRemover(new FileNamePattern(tbrp.getFileNamePattern(), this.context), rc);
    }
}


/**
 * UOP日志删除器, 解决了无法删除序号为1000以上的日志文件的问题
 * Created by flamhaze on 2017/6/8.
 */
class PatchedLogArchiveRemover extends ContextAwareBase implements ArchiveRemover
{
    /** 未初始化标志位 */
    private static final long UNINITIALIZED = -1;

    /** 最后心跳时间, 标记最后扫描时间 */
    private long lastHeartBeat = UNINITIALIZED;
    // aim for 32 days, except in case of hourly rollover
    private static final long INACTIVITY_TOLERANCE_IN_MILLIS = 32L * CoreConstants.MILLIS_IN_ONE_DAY;

    private static final int MAX_VALUE_FOR_INACTIVITY_PERIODS = 14 * 24; // 14 days in case of hourly rollover
    private final FileNamePattern fileNamePattern;
    private final RollingCalendar rc;
    private int maxHistory = CoreConstants.UNBOUND_HISTORY;
    private long totalSizeCap = CoreConstants.UNBOUNDED_TOTAL_SIZE_CAP;
    private final boolean parentClean;

    private Converter headTokenConvertor;

    PatchedLogArchiveRemover(FileNamePattern fileNamePattern, RollingCalendar rc)
    {
        this.fileNamePattern = fileNamePattern;
        this.rc = rc;
        this.parentClean = computeParentCleaningFlag(fileNamePattern);
    }

    /**
     * 从当前日期开始往前推, 清理日志
     * @param now 当前日期
     */
    public void clean(Date now)
    {
        long nowInMillis = now.getTime();

        // for a live appender periodsElapsed is expected to be 1
        int periodsElapsed = computeElapsedPeriodsSinceLastClean(nowInMillis);
        lastHeartBeat = nowInMillis;
        if (periodsElapsed > 1)
        {
            addInfo("Multiple periods, i.e. " + periodsElapsed + " periods, seem to have elapsed. This is expected at application start.");
        }
        for (int i = 0; i < periodsElapsed; i++)
        {
            int offset = getPeriodOffsetForDeletionTarget() - i;
            Date dateOfPeriodToClean = rc.getEndOfNextNthPeriod(now, offset);
            cleanPeriod(dateOfPeriodToClean);
        }
    }

    protected File[] getFilesInPeriod(Date dateOfPeriodToClean)
    {
        File archive0 = new File(fileNamePattern.convertMultipleArguments(dateOfPeriodToClean, 0));
        File parentDir = getParentDir(archive0);
        String stemRegex = createStemRegex(dateOfPeriodToClean);
        File[] matchingFileArray = FileFilterUtil.filesInFolderMatchingStemRegex(parentDir, stemRegex);
        return matchingFileArray;
    }

    private String createStemRegex(final Date dateOfPeriodToClean)
    {
        String regex = fileNamePatternToRegexForFixedDate(fileNamePattern, dateOfPeriodToClean);
        return FileFilterUtil.afterLastSlash(regex);
    }

    /**
     * 将文件名转换为固定日期的正则表达式
     *
     * @param fileNamePattern 文件名正则表达式
     * @param date            日期
     * @return 固定日期的正则表达式
     */
    private String fileNamePatternToRegexForFixedDate(FileNamePattern fileNamePattern, Date date)
    {
        StringBuilder buf = new StringBuilder();
        Converter<Object> p = getHeadTokenConvertor(fileNamePattern);
        while (p != null)
        {
            if (p instanceof LiteralConverter)
            {
                buf.append(p.convert(null));
            }
            else if (p instanceof IntegerTokenConverter)
            {
                buf.append("(\\d{1,100})");
            }
            else if (p instanceof DateTokenConverter)
            {
                buf.append(p.convert(date));
            }
            p = p.getNext();
        }
        return buf.toString();
    }

    private boolean fileExistsAndIsFile(File file2Delete)
    {
        return file2Delete.exists() && file2Delete.isFile();
    }

    public void cleanPeriod(Date dateOfPeriodToClean)
    {
        File[] matchingFileArray = getFilesInPeriod(dateOfPeriodToClean);

        for (File f : matchingFileArray)
        {
            addInfo("deleting " + f);
            f.delete();
        }

        if (parentClean && matchingFileArray.length > 0)
        {
            File parentDir = getParentDir(matchingFileArray[0]);
            removeFolderIfEmpty(parentDir);
        }
    }

    void capTotalSize(Date now)
    {
        long totalSize = 0;
        long totalRemoved = 0;
        for (int offset = 0; offset < maxHistory; offset++)
        {
            Date date = rc.getEndOfNextNthPeriod(now, -offset);
            File[] matchingFileArray = getFilesInPeriod(date);
            descendingSortByLastModified(matchingFileArray);
            for (File f : matchingFileArray)
            {
                long size = f.length();
                if (totalSize + size > totalSizeCap)
                {
                    addInfo("Deleting [" + f + "]" + " of size " + new FileSize(size));
                    totalRemoved += size;
                    f.delete();
                }
                totalSize += size;
            }
        }
        addInfo("Removed  " + new FileSize(totalRemoved) + " of files");
    }

    private void descendingSortByLastModified(File[] matchingFileArray)
    {
        Arrays.sort(matchingFileArray, new Comparator<File>()
        {
            @Override
            public int compare(final File f1, final File f2)
            {
                long l1 = f1.lastModified();
                long l2 = f2.lastModified();
                if (l1 == l2) return 0;
                // descending sort, i.e. newest files first
                if (l2 < l1)
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
        });
    }

    File getParentDir(File file)
    {
        File absolute = file.getAbsoluteFile();
        File parentDir = absolute.getParentFile();
        return parentDir;
    }

    /**
     * 计算从上次清理开始到现在过去的时间
     * @param nowInMillis 现在的时间
     * @return 上次清理开始到现在过去的时间
     */
    int computeElapsedPeriodsSinceLastClean(long nowInMillis)
    {
        long periodsElapsed;
        if (lastHeartBeat == UNINITIALIZED)
        {
            addInfo("日志框架初始化后第一次清理..");
            periodsElapsed = rc.periodBarriersCrossed(nowInMillis, nowInMillis + INACTIVITY_TOLERANCE_IN_MILLIS);
            periodsElapsed = Math.min(periodsElapsed, MAX_VALUE_FOR_INACTIVITY_PERIODS);
        }
        else
        {
            periodsElapsed = rc.periodBarriersCrossed(lastHeartBeat, nowInMillis);
            // periodsElapsed of zero is possible for size and time based policies
        }
        return (int) periodsElapsed;
    }

    boolean computeParentCleaningFlag(FileNamePattern fileNamePattern)
    {
        DateTokenConverter<Object> dtc = fileNamePattern.getPrimaryDateTokenConverter();
        // if the date pattern has a /, then we need parent cleaning
        if (dtc.getDatePattern().indexOf('/') != -1)
        {
            return true;
        }
        // if the literal string subsequent to the dtc contains a /, we also
        // need parent cleaning

        Converter<Object> p = getHeadTokenConvertor(fileNamePattern);

        // find the date converter
        while (p != null)
        {
            if (p instanceof DateTokenConverter)
            {
                break;
            }
            p = p.getNext();
        }

        while (p != null)
        {
            if (p instanceof LiteralConverter)
            {
                String s = p.convert(null);
                if (s.indexOf('/') != -1)
                {
                    return true;
                }
            }
            p = p.getNext();
        }

        // no /, so we don't need parent cleaning
        return false;
    }

    void removeFolderIfEmpty(File dir)
    {
        removeFolderIfEmpty(dir, 0);
    }

    /**
     * Will remove the directory passed as parameter if empty. After that, if the
     * parent is also becomes empty, remove the parent dir as well but at most 3
     * times.
     *
     * @param dir
     * @param depth
     */
    private void removeFolderIfEmpty(File dir, int depth)
    {
        // we should never go more than 3 levels higher
        if (depth >= 3)
        {
            return;
        }
        if (dir.isDirectory() && FileFilterUtil.isEmptyDirectory(dir))
        {
            addInfo("deleting folder [" + dir + "]");
            dir.delete();
            removeFolderIfEmpty(dir.getParentFile(), depth + 1);
        }
    }

    public void setMaxHistory(int maxHistory)
    {
        this.maxHistory = maxHistory;
    }

    protected int getPeriodOffsetForDeletionTarget()
    {
        return -maxHistory - 1;
    }

    public void setTotalSizeCap(long totalSizeCap)
    {
        this.totalSizeCap = totalSizeCap;
    }

    public String toString()
    {
        return "c.q.l.core.rolling.helper.TimeBasedArchiveRemover";
    }

    public Future<?> cleanAsynchronously(Date now)
    {
        ArchiveRemoverRunnable runnable = new ArchiveRemoverRunnable(now);
        ExecutorService executorService = context.getScheduledExecutorService();
        Future<?> future = executorService.submit(runnable);
        return future;
    }

    /**
     * 获取文件名正则表达式对象的转换器
     *
     * @param fileNamePattern 文件名正则表达式对象
     * @return 转换器
     */
    private Converter<Object> getHeadTokenConvertor(FileNamePattern fileNamePattern)
    {
        try
        {
            if (headTokenConvertor == null)
            {
                synchronized (this)
                {
                    if (headTokenConvertor == null)
                    {
                        Field field = FileNamePattern.class.getDeclaredField("headTokenConverter");
                        field.setAccessible(true);
                        headTokenConvertor = (Converter) field.get(fileNamePattern);
                        field.setAccessible(false);
                    }
                }
            }
        }
        catch (Exception e)
        {
            addError("获取headTokenConvertor时发生异常");
        }
        return headTokenConvertor;
    }

    public class ArchiveRemoverRunnable implements Runnable
    {
        Date now;

        ArchiveRemoverRunnable(Date now)
        {
            this.now = now;
        }

        /**
         * @see Runnable#run()
         */
        @Override
        public void run()
        {
            clean(now);
            if (totalSizeCap != UNBOUNDED_TOTAL_SIZE_CAP && totalSizeCap > 0)
            {
                capTotalSize(now);
            }
        }
    }
}
