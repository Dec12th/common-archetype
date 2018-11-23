package com.benny.framework.common.framework.util.cache;

import com.benny.framework.common.framework.lang.exception.CommonErrorCode;
import com.benny.framework.common.framework.util.AssertUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 本地缓存工具
 *
 * @author yin.beibei
 * @date 2018/11/23 14:20
 */
public class LocalCacheUtil {
    /**
     * 构建异步刷新缓存, 过期时间后获取缓存值异步触发刷新流程, 同步返回旧值
     *
     * @param refreshInterval   刷新间隔
     * @param timeUnit          刷新时间单位
     * @param refreshThreadSize 刷新线程池大小
     * @param cacheSize         缓存大小
     * @param cacheLoadCallback 缓存载入回调函数
     * @param <K>               缓存key类型
     * @param <T>               缓存对象类型
     * @return 自动刷新缓存
     */
    public static <K, T> LoadingCache<K, T> buildAsyncCache(long refreshInterval, TimeUnit timeUnit,
                                                            int refreshThreadSize,
                                                            long cacheSize, Function<K, T> cacheLoadCallback) {
        AssertUtil.isNotNull(cacheLoadCallback, CommonErrorCode.UNKNOWN_ERROR, "缓存载入回调对象为空");
        AssertUtil.isGreaterZero(refreshInterval, CommonErrorCode.UNKNOWN_ERROR, "刷新间隔必须大于0, value: {0}", refreshInterval);
        AssertUtil.isGreaterZero(refreshThreadSize, CommonErrorCode.UNKNOWN_ERROR, "刷新线程池大小必须大于0, value: {0}", refreshThreadSize);
        AssertUtil.isGreaterZero(cacheSize, CommonErrorCode.UNKNOWN_ERROR, "缓存大小必须大于0, value: {0}", cacheSize);

        ExecutorService refreshExecutorService = Executors.newFixedThreadPool(refreshThreadSize);
        Runtime.getRuntime().addShutdownHook(new Thread(refreshExecutorService::shutdown));

        ListeningExecutorService bgRefreshPools = MoreExecutors.listeningDecorator(refreshExecutorService);

        LoadingCache<K, T> cache = CacheBuilder.newBuilder()
                                               .refreshAfterWrite(refreshInterval, timeUnit)
                                               .maximumSize(cacheSize)
                                               .build(new CacheLoader<K, T>() {
                                                   /**
                                                    * @see CacheLoader#load(Object)
                                                    */
                                                   @Override
                                                   public T load(K key) throws Exception {
                                                       return cacheLoadCallback.apply(key);
                                                   }

                                                   /**
                                                    * @see CacheLoader#reload(Object, Object)
                                                    */
                                                   @Override
                                                   public ListenableFuture<T> reload(K key, T oldValue) throws Exception {
                                                       return bgRefreshPools.submit(() -> cacheLoadCallback.apply(key));
                                                   }
                                               });

        return cache;
    }

    /**
     * 构建可过期的异步刷新缓存
     * 过期时间后缓存存活时间内获取缓存值异步触发刷新流程, 同步返回旧值
     * 过期时间后缓存存活时间外缓存旧值失效, 获取缓存值同步触发重新载入流程, 返回新值
     *
     * @param refreshInterval   刷新间隔
     * @param lifeExtension     缓存续命时长
     * @param timeUnit          时间单位
     * @param refreshThreadSize 刷新线程池大小
     * @param cacheSize         缓存大小
     * @param cacheLoadCallback 缓存载入回调函数
     * @param <K>               缓存key类型
     * @param <T>               缓存对象类型
     * @return 自动刷新缓存
     */
    public static <K, T> LoadingCache<K, T> buildExpireableAsyncCache(long refreshInterval, int lifeExtension, TimeUnit timeUnit,
                                                                      int refreshThreadSize,
                                                                      long cacheSize, Function<K, T> cacheLoadCallback) {
        AssertUtil.isNotNull(cacheLoadCallback, CommonErrorCode.UNKNOWN_ERROR, "缓存载入回调对象为空");

        AssertUtil.isGreaterZero(refreshInterval, CommonErrorCode.UNKNOWN_ERROR, "刷新间隔必须大于0, value: {0}", refreshInterval);
        AssertUtil.isGreaterZero(lifeExtension, CommonErrorCode.UNKNOWN_ERROR, "续命时长必须大于0, value: {0}", lifeExtension);
        AssertUtil.isGreaterZero(refreshThreadSize, CommonErrorCode.UNKNOWN_ERROR, "刷新线程池大小必须大于0, value: {0}", refreshThreadSize);
        AssertUtil.isGreaterZero(cacheSize, CommonErrorCode.UNKNOWN_ERROR, "缓存大小必须大于0, value: {0}", cacheSize);

        ExecutorService refreshExecutorService = Executors.newFixedThreadPool(refreshThreadSize);
        Runtime.getRuntime().addShutdownHook(new Thread(refreshExecutorService::shutdown));

        ListeningExecutorService bgRefreshPools = MoreExecutors.listeningDecorator(refreshExecutorService);

        LoadingCache<K, T> cache = CacheBuilder.newBuilder()
                                               .expireAfterWrite(refreshInterval + lifeExtension, timeUnit)
                                               .refreshAfterWrite(refreshInterval, timeUnit)
                                               .maximumSize(cacheSize)
                                               .build(new CacheLoader<K, T>() {
                                                   /**
                                                    * @see CacheLoader#load(Object)
                                                    */
                                                   @Override
                                                   public T load(K key) throws Exception {
                                                       return cacheLoadCallback.apply(key);
                                                   }

                                                   /**
                                                    * @see CacheLoader#reload(Object, Object)
                                                    */
                                                   @Override
                                                   public ListenableFuture<T> reload(K key, T oldValue) throws Exception {
                                                       return bgRefreshPools.submit(() -> cacheLoadCallback.apply(key));
                                                   }
                                               });

        return cache;
    }


}
