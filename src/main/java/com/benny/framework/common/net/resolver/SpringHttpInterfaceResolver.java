package com.benny.framework.common.net.resolver;

import com.benny.framework.common.net.annotations.CommonNet;
import com.benny.framework.common.net.domain.BaseRequest;
import com.benny.framework.common.net.domain.CommonServicePointInfo;
import com.benny.framework.common.net.domain.InterfaceInfo;
import com.benny.framework.common.net.domain.ServicePointInfo;
import com.benny.framework.common.net.exception.ArgumentStyleNotAllowException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring http 接口信息解析器, 需要接口有符合标准的风格<br/><br/>
 * 参数风格: 参数个数为1个, 参数类型为继承了BaseRequest的类<br/>
 * 参数个数为1个的原因是, RequestBody只能有一个, http读取该信息是按流来读取的, 读取完一个就结束了, 不能做很好的区分.<br/>
 * 参数类型要继承BaseRequest, 是因为该类中含有一些基础的描述信息, 例如环境信息等; 为什么不在框架层面透明化这些信息呢, 那是因为要改写SpringMVC的转发逻辑, 比较麻烦..<br/>
 * @author yin.beibei
 * @date 2018/11/19 11:58
 */
public class SpringHttpInterfaceResolver implements InterfaceResolver<Class> {
    /**
     * 服务点前缀
     */
    public static final String SERVICE_POINT_PREFIX = "http://";

    /**
     * 服务路径分隔符
     */
    private static final String SERVICE_PATH_SEP = "/";

    /**
     * @see InterfaceResolver#parse(Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public InterfaceInfo parse(Class sourceInterfaceInfo) {
        if (sourceInterfaceInfo == null) {
            return null;
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setInterfaceClass(sourceInterfaceInfo);
        interfaceInfo.setAppName(getAppName(sourceInterfaceInfo));
        interfaceInfo.setServicePointInfos(getServicePointInfos(sourceInterfaceInfo));

        return interfaceInfo;
    }

    /**
     * 根据源接口信息获取接口服务点信息列表
     *
     * @param sourceInterfaceInfo 源接口信息
     * @return 接口服务点信息列表
     */
    private List<ServicePointInfo> getServicePointInfos(Class sourceInterfaceInfo) {
        List<ServicePointInfo> servicePointInfos = new ArrayList<>();
        String appName = getAppName(sourceInterfaceInfo);

        for (Method service : sourceInterfaceInfo.getDeclaredMethods()) {
            Class[] argumentTypes = service.getParameterTypes();
            if (argumentTypes.length != 1) {
                throw new ArgumentStyleNotAllowException(sourceInterfaceInfo, service);
            }

            Class argumentType = argumentTypes[0];

            if (!BaseRequest.class.isAssignableFrom(argumentType)) {
                throw new ArgumentStyleNotAllowException(sourceInterfaceInfo, service);
            }

            ServicePointInfo servicePointInfo = new CommonServicePointInfo();
            servicePointInfo.setInterfaceClass(sourceInterfaceInfo);
            servicePointInfo.setServiceClientPoint(service);
            servicePointInfo.setArgumentType(argumentType);
            servicePointInfo.setResultType(service.getReturnType());
            servicePointInfo.setServiceServerPoint(buildServicePoint(appName, getServiceRootPath(sourceInterfaceInfo), getServiceRelativePath(service)));

            servicePointInfos.add(servicePointInfo);
        }
        return servicePointInfos;
    }

    /**
     * 获取应用名
     *
     * @param sourceInterfaceInfo 源接口信息
     * @return 应用名
     */
    private String getAppName(Class sourceInterfaceInfo) {
        String appName = null;
        if (sourceInterfaceInfo.isAnnotationPresent(CommonNet.class)) {
            CommonNet commonNetAnnotation = (CommonNet) sourceInterfaceInfo.getAnnotation(CommonNet.class);
            appName = commonNetAnnotation.appName();
        }
        return appName;
    }

    /**
     * 获取服务根路径, 没有配置则返回null
     *
     * @param sourceInterfaceInfo 源接口信息
     * @return 服务根路径
     */
    private String getServiceRootPath(Class sourceInterfaceInfo) {
        String serviceRootPath = null;
        if (sourceInterfaceInfo.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMappingAnnotation = (RequestMapping) sourceInterfaceInfo.getAnnotation(RequestMapping.class);
            serviceRootPath = getRequestMappingPath(requestMappingAnnotation);
        }

        return serviceRootPath;
    }

    /**
     * 获取服务点相对路径
     *
     * @param service 服务对象
     * @return 服务点相对路径
     */
    private String getServiceRelativePath(Method service) {
        String serviceRelativePath = null;
        if (service.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMappingAnnotation = service.getAnnotation(RequestMapping.class);
            serviceRelativePath = getRequestMappingPath(requestMappingAnnotation);
        } else if (service.isAnnotationPresent(PostMapping.class)) {
            PostMapping postMappingAnnotation = service.getAnnotation(PostMapping.class);
            serviceRelativePath = getPostMappingPath(postMappingAnnotation);
        }

        return serviceRelativePath;
    }

    /**
     * 构建服务点
     *
     * @param appName         应用名
     * @param serviceRootPath 服务根路径
     * @param relativePath    服务相对路径
     * @return 服务绝对路径
     */
    private String buildServicePoint(String appName, String serviceRootPath, String relativePath) {
        if (StringUtils.isBlank(serviceRootPath)) {
            serviceRootPath = "";
        }

        if (StringUtils.isBlank(relativePath)) {
            relativePath = "";
        }

        StringBuilder servicePoint = new StringBuilder(SERVICE_POINT_PREFIX);
        servicePoint.append(appName).append(SERVICE_PATH_SEP).
                append(serviceRootPath).append(SERVICE_PATH_SEP).
                            append(relativePath);

        return servicePoint.toString();
    }

    /**
     * 获取 RequestMapping注解 中描述的路径值
     *
     * @param requestMappingAnnotation RequestMapping注解
     * @return RequestMapping注解 中描述的路径值
     */
    private String getRequestMappingPath(RequestMapping requestMappingAnnotation) {
        if (requestMappingAnnotation == null) {
            return null;
        }

        String[] serviceRootPaths = requestMappingAnnotation.path();
        if (serviceRootPaths.length == 0) {
            serviceRootPaths = requestMappingAnnotation.value();
        }

        if (serviceRootPaths.length == 0) {
            return "";
        }

        return serviceRootPaths[0];
    }

    /**
     * 获取 postMapping注解 中描述的路径值
     *
     * @param postMappingAnnotation postMapping注解
     * @return postMapping注解 中描述的路径值
     */
    private String getPostMappingPath(PostMapping postMappingAnnotation) {
        if (postMappingAnnotation == null) {
            return null;
        }

        String[] serviceRootPaths = postMappingAnnotation.path();
        if (serviceRootPaths.length == 0) {
            serviceRootPaths = postMappingAnnotation.value();
        }

        if (serviceRootPaths.length == 0) {
            return "";
        }

        return serviceRootPaths[0];
    }
}
