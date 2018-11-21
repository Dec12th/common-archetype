package com.benny.common.framework.service.template;

import com.benny.common.net.domain.BaseResult;

/**
 * 业务执行模板
 * @author yin.beibei
 * @date 2018/11/19 17:38
 */
public interface ServiceExecuteTemplate {

    /**
     * 执行业务
     * @param result          业务执行结果
     * @param serviceCallBack 业务执行回调
     * @return 业务执行结果
     */
    <T extends BaseResult> T executeService(T result, ServiceCallBack serviceCallBack);
}
