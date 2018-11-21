package com.benny.framework.common.net.utils;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yin.beibei
 * @date 2018/11/19 13:52
 */
public class RemoteAppsContext {
    /** 需要创建客户端的远程服务列表 */
    private static final Set<String> REMOTE_APPS = new HashSet<>();

    /**
     * 增加远程服务名
     * @param appName 包名前缀
     */
    public static void addRemoteApp(String appName)
    {
        REMOTE_APPS.add(appName);
    }

    /**
     * 获取所有被忽略的api的requestMapping映射上下文的包名前缀列表
     * @return 所有被忽略的api的requestMapping映射上下文的包名前缀列表
     */
    @SuppressWarnings("unchecked")
    public static Set<String> getAllRemoteApps()
    {
        return Collections.unmodifiableSet(REMOTE_APPS);
    }

    /**
     * @see RemoteAppsContext#getAllRemoteApps()
     */
    public static List<String> getAllRemoteAppList()
    {
        return Collections.unmodifiableList(Lists.newArrayList(REMOTE_APPS));
    }

    /**
     * 清除被忽略的api的requestMapping映射上下文的包名前缀列表信息
     */
    public static void clear()
    {
        REMOTE_APPS.clear();
    }
}
