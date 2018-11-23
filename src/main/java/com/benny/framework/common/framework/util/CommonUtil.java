package com.benny.framework.common.framework.util;

import com.benny.framework.common.framework.lang.enums.BaseEnum;
import com.benny.framework.common.framework.lang.enums.Brace;
import com.benny.framework.common.framework.lang.exception.CommonErrorCode;
import com.benny.framework.common.net.domain.BaseResult;
import com.benny.framework.common.net.domain.ErrorContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 通用工具, 无状态, 或无不可修复状态
 *
 * @author yin.beibei
 * @date 2018/11/23 15:11
 */
public final class CommonUtil {
    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 枚举缓存
     */
    private static final Map<Class<? extends BaseEnum>, Map<String, BaseEnum>> BASE_ENUM_CACHE;

    /**
     * 字段修饰符字段名
     */
    private static final String FIELD_MODIFIERS = "modifiers";

    /**
     * 修饰符字段
     */
    private static Field modifiersField;

    /**
     * 观测到的final修饰符修饰的字段集合
     */
    private static final Set<Field> FINAL_FIELDS = Collections.synchronizedSet(new HashSet<>());

    /**
     * 初始化工具类
     */
    static {
        try {
            modifiersField = Field.class.getDeclaredField(FIELD_MODIFIERS);
        } catch (NoSuchFieldException e) {
            LOGGER.error("初始化通用工具类失败", e);
            modifiersField = null;
        }
        BASE_ENUM_CACHE = new ConcurrentHashMap<>();
    }


    /**
     * 判断异常上下文中的异常码是否和传入值相同
     *
     * @param errorContext 异常上下文
     * @param errorCode    异常码
     * @return 异常上下文中的异常码是否和传入值相同
     */
    public static boolean isErrorCodeEquals(ErrorContext errorContext, String errorCode) {
        if (errorContext == null) {
            return StringUtils.isBlank(errorCode);
        } else {
            return StringUtils.equals(errorContext.getErrorCode(), errorCode);
        }
    }

    /**
     * 判断结果中的异常码是否和传入值相同
     *
     * @param result    结果
     * @param errorCode 异常码
     * @return 异常上下文中的异常码是否和传入值相同
     */
    public static boolean isErrorCodeEquals(BaseResult result, String errorCode) {
        if (result == null) {
            return StringUtils.isBlank(errorCode);
        } else {
            return isErrorCodeEquals(result.getErrorContext(), errorCode);
        }
    }

    /**
     * 设置字段值, <font color='red'>static final 修饰符的字段不支持基本类型(编译原因)</font>
     *
     * @param field 字段
     * @param value 值
     */
    public static void setFieldValue(Field field, Object value) throws IllegalAccessException {
        AssertUtil.isNotNull(modifiersField, CommonErrorCode.UNKNOWN_ERROR);

        synchronized (field) {
            field.setAccessible(true);
            closeFieldModifiers(field);

            field.set(null, value);

            reOpenFieldModifiers(field);
            field.setAccessible(false);
        }
    }

    /**
     * 获取当前类名, 支持静态调用
     *
     * @return 当前类名
     */
    public static String getCurrentClassName() {
        return new Object() {
            String getContainerClassName() {
                String className = this.getClass().getName();
                return className.substring(0, className.lastIndexOf('$'));
            }
        }.getContainerClassName();
    }

    /**
     * 获取包名下的所有类
     *
     * @param packageName 包名
     * @return 包名下的所有类
     */
    public static List<Class> getClassesUnderPackage(String packageName) {
        return getClassesUnderPackage(packageName, Collections.singletonList(new ClassFilter() {
            /**
             * @see ClassFilter#reject(Class)
             */
            @Override
            public boolean reject(Class clazz) {
                return false;
            }
        }));
    }

    /**
     * 获取包名下的所有类
     *
     * @param packageName  包名
     * @param classFilters 类过滤器
     * @return 包名下的所有类
     */
    public static List<Class> getClassesUnderPackage(String packageName, List<ClassFilter> classFilters) {
        String packageUrl = packageName.replace(".", "/");
        List<Class> classes = new ArrayList<>();
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageUrl);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (StringUtils.equals(protocol, "file")) {
                        String packagePath = url.getPath().replaceAll("%20", "");
                        addClass(classes, packagePath, packageName, classFilters);
                    } else if (StringUtils.equals(protocol, "jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                            while (jarEntryEnumeration.hasMoreElements()) {
                                JarEntry jarEntry = jarEntryEnumeration.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (jarEntryName.endsWith(".class")) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf('.')).replaceAll("/", ".");

                                    Class clazz = Class.forName(className);

                                    boolean reject = false;
                                    for (ClassFilter classFilter : classFilters) {
                                        if (classFilter == null) {
                                            continue;
                                        }

                                        if (classFilter.reject(clazz)) {
                                            reject = true;
                                            break;
                                        }
                                    }

                                    if (!reject) {
                                        classes.add(clazz);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return classes;
    }

    /**
     * 将网络地址对象转换为IP地址
     *
     * @param inetAddress 网络地址对象
     * @return IP地址
     */
    public static String convert2IPAdress(InetAddress inetAddress) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : inetAddress.getAddress()) {
            int value = b & 0xff;
            value = (value + 256) % 256;
            stringBuffer.append(value).append(".");
        }

        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    /**
     * 构建URL
     *
     * @param ipAddress ip地址
     * @param port      端口号
     * @return URL
     */
    public static String buildUrl(String ipAddress, int port) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(ipAddress).append(":").append(port);
        return stringBuffer.toString();
    }

    /**
     * 根据枚举码获取枚举对象
     *
     * @param code      枚举码
     * @param enumClass 枚举类
     * @param <T>       枚举类泛型
     * @return 枚举对象
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseEnum> T getByCode(String code, Class<? extends T> enumClass) {
        Map<String, BaseEnum> classEnumsMap = BASE_ENUM_CACHE.get(enumClass);

        if (classEnumsMap == null) {
            classEnumsMap = new HashMap<>();
            BaseEnum[] classEnums = enumClass.getEnumConstants();
            if (classEnums != null && classEnums.length > 0) {
                for (BaseEnum baseEnum : classEnums) {
                    classEnumsMap.put(baseEnum.getCode(), baseEnum);
                }
            }
            classEnumsMap = MapUtils.unmodifiableMap(classEnumsMap);
            BASE_ENUM_CACHE.putIfAbsent(enumClass, classEnumsMap);
        }

        return (T) classEnumsMap.get(code);
    }

    /**
     * 输出花括号
     *
     * @param brace      花括号
     * @param needFormat 是否格式化
     * @return 花括号
     */
    private static String outputBrace(Brace brace, boolean needFormat) {
        if (needFormat) {
            return "'" + brace.getValue() + "'";
        }
        return brace.getValue();
    }

    /**
     * 将类路径和类名对应的类添加进队列中
     *
     * @param classes      类队列
     * @param packagePath  类路径
     * @param packageName  类名
     * @param classFilters 类过滤器列表
     */
    private static void addClass(List<Class> classes, String packagePath, String packageName, List<ClassFilter> classFilters) {
        if (classes == null) {
            throw new RuntimeException("class集合为空!");
        }

        final File[] files = new File(packagePath).listFiles(new FileFilter() {
            /**
             * @see FileFilter#accept(File)
             */
            @Override
            public boolean accept(File pathname) {
                return ((pathname.isFile() && pathname.getName().endsWith(".class")) || pathname.isDirectory());
            }
        });

        if (files == null) {
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf('.'));
                if (StringUtils.isNotBlank(packageName)) {
                    className = packageName + "." + className;
                }

                try {
                    Class clazz = Class.forName(className);
                    boolean reject = false;
                    for (ClassFilter classFilter : classFilters) {
                        if (classFilter == null) {
                            continue;
                        }

                        if (classFilter.reject(clazz)) {
                            reject = true;
                            break;
                        }
                    }

                    if (!reject) {
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                String subPackagePath = packagePath + "/" + fileName;

                String subPackageName = fileName;
                if (StringUtils.isNotBlank(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classes, subPackagePath, subPackageName, classFilters);
            }
        }
    }

    /**
     * 将文件转换为Base64编码
     *
     * @param sourceFilePath 源文件路径
     * @return base64字符串
     * @throws IOException
     */
    public static String fileToBase64(String sourceFilePath) throws IOException {
        File picFile = new File(sourceFilePath);
        FileInputStream fileInputStream = new FileInputStream(picFile);
        List<Byte> fileByteList = new ArrayList<>();
        int bufferLength = 1024;
        byte[] buffer = new byte[bufferLength];

        // 将源文件转换为中间态, 字节数组
        while (fileInputStream.read(buffer, 0, buffer.length) != -1) {
            for (byte b : buffer) {
                fileByteList.add(b);
            }
            buffer = new byte[bufferLength];
        }

        fileInputStream.close();


        byte[] fileBytes = new byte[fileByteList.size()];

        for (int i = 0; i < fileByteList.size(); i++) {
            fileBytes[i] = fileByteList.get(i);
        }

        // 将源文件对应的字节数组转换为base64编码字符串
        return Base64.encodeBase64String(fileBytes);
    }

    /**
     * 关闭字段修饰符控制, 关闭后需要重新打开
     * {@link CommonUtil#reOpenFieldModifiers(Field)}
     *
     * @param field 要关闭字段修饰符控制的字段
     */
    private static void closeFieldModifiers(Field field) throws IllegalAccessException {
        AssertUtil.isNotNull(modifiersField, CommonErrorCode.UNKNOWN_ERROR);

        int modifier = field.getModifiers();

        if (!Modifier.isFinal(modifier)) {
            return;
        }

        FINAL_FIELDS.add(field);

        modifiersField.setAccessible(true);

        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        modifiersField.setAccessible(false);
    }

    /**
     * 重新打开字段修饰符控制, 只有被关闭了字段修饰符控制的字段才能有效使用此方法
     * 需要被关闭过的字段才能被重新打开, {@link CommonUtil#closeFieldModifiers(Field)}
     *
     * @param field 要打开字段修饰符控制的字段
     */
    private static void reOpenFieldModifiers(Field field) throws IllegalAccessException {
        AssertUtil.isNotNull(modifiersField, CommonErrorCode.UNKNOWN_ERROR);

        int modifier = field.getModifiers();

        if (!FINAL_FIELDS.contains(field)) {
            return;
        }

        if (!Modifier.isFinal(modifier)) {
            return;
        }

        modifiersField.setAccessible(true);

        modifiersField.setInt(field, field.getModifiers() ^ ~Modifier.FINAL);

        modifiersField.setAccessible(false);
    }

}
