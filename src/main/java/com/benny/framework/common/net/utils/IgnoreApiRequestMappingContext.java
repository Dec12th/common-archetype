package com.benny.framework.common.net.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 忽略api的requestMapping映射上下文
 *
 * @author yin.beibei
 * @date 2018/11/19 14:08
 */
public class IgnoreApiRequestMappingContext {
    /**
     * 被忽略的api的requestMapping映射上下文的包名前缀列表
     */
    private static final List<String> IGNORED_API_BASE_PACKAGES = new ArrayList<>();

    /**
     * 增加被忽略的api的requestMapping映射上下文的包名前缀
     *
     * @param basePackage 包名前缀
     */
    public static void addIgnoredBasePackage(String basePackage) {
        IGNORED_API_BASE_PACKAGES.add(basePackage);
    }

    /**
     * 获取所有被忽略的api的requestMapping映射上下文的包名前缀列表
     *
     * @return 所有被忽略的api的requestMapping映射上下文的包名前缀列表
     */
    public static List<String> getAllIgnoredBasePackage() {
        return Collections.unmodifiableList(IGNORED_API_BASE_PACKAGES);
    }

    /**
     * 清除被忽略的api的requestMapping映射上下文的包名前缀列表信息
     */
    public static void clear() {
        IGNORED_API_BASE_PACKAGES.clear();
    }
}
