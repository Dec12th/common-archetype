package com.benny.archetype.common.net.domain;

import com.benny.archetype.common.net.domain.token.Header;
import com.benny.archetype.common.net.domain.token.Payload;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:30
 */
@Data
public class WebToken implements Serializable {
    private static final long serialVersionUID = -35935356587164626L;
    /** token头 */
    private Header header;

    /** token标准信息 */
    private Payload payload;

    /** token签名 */
    private String signature;
}
