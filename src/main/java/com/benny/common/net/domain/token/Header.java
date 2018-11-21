package com.benny.common.net.domain.token;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:31
 */
@Data
public class Header implements Serializable {
    private static final long serialVersionUID = 6271026310674492986L;

    /** token类型, typ, {@link com.bestv.common.lang.enums.TokenType#getCode()} */
    private String tokenType;

    /** 加密方式, alg, {@link com.bestv.common.lang.enums.TokenEncryptType#getCode()} */
    private String encryptType;
}
