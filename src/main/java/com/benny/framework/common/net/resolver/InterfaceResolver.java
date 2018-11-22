package com.benny.framework.common.net.resolver;


import com.benny.framework.common.net.domain.InterfaceInfo;

/**
 * 接口信息解析器
 * @author yin.beibei
 * @date 2018/11/19 11:57
 */
public interface InterfaceResolver<T> {
    /**
     * 将源接口信息解析为框架接口信息
     * @param sourceInterfaceInfo 源接口信息
     * @return 框架接口信息
     */
    InterfaceInfo parse(T sourceInterfaceInfo);
}
