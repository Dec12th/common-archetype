package com.benny.framework.common.net.domain.token;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:36
 */
@Data
public class Payload implements Serializable {
    private static final long serialVersionUID = 837386728472609880L;
    /** token的id, 防止重放攻击, jti: json web token id */
    private String jsonWebTokenId;

    /** 签发者, iss: issuer */
    private String issuer;

    /** token面向对象实体id, 如用户id, sub:subject */
    private String subjectId;

    /** 接收方, aud:audience */
    private String audience;

    /** 签发时间, 使用ISO 8601格式, iat: issue at */
    private String issuedTime;

    /** 过期时间, 使用ISO 8601格式, exp: expires */
    private String expires;

    /** 指定该时间之前token无效, 使用ISO 8601格式, nbf: not before */
    private String invalidBeforeTime;
}
