package com.benny.archetype.common.net.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:38
 */
@Data
public class BaseRequest implements Serializable {
    private static final long serialVersionUID = -5806675968316141676L;
    /**
     * 环境上下文
     */
    private EnvContext envContext;

}
