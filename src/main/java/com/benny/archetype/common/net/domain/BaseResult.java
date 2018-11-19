package com.benny.archetype.common.net.domain;

import java.io.Serializable;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:24
 */
public class BaseResult  implements Serializable {

    private static final long serialVersionUID = -8080296699531492240L;

    /** 业务是否成功 */
    private boolean success = false;

    /** 环境上下文 */
    private EnvContext envContext;

    /** 错误上下文 */
    private ErrorContext errorContext;

    /**
     * method for get envContext
     */
    public EnvContext getEnvContext()
    {
        return envContext;
    }

    /**
     * method for set envContext
     */
    public void setEnvContext(EnvContext envContext)
    {
        this.envContext = envContext;
    }

    /**
     * method for get success
     */
    public boolean isSuccess()
    {
        return success;
    }

    /**
     * method for set success
     */
    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    /**
     * method for get errorContext
     */
    public ErrorContext getErrorContext()
    {
        return errorContext;
    }

    /**
     * method for set errorContext
     */
    public void setErrorContext(ErrorContext errorContext)
    {
        this.errorContext = errorContext;
    }
}
