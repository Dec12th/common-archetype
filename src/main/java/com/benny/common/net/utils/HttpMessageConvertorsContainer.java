package com.benny.common.net.utils;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yin.beibei
 * @date 2018/11/19 14:52
 */
public class HttpMessageConvertorsContainer {
    private static boolean romePresent =
            ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", RestTemplate.class.getClassLoader());

    private static final boolean jaxb2Present =
            ClassUtils.isPresent("javax.xml.bind.Binder", RestTemplate.class.getClassLoader());

    private static final boolean jackson2Present =
            ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", RestTemplate.class.getClassLoader()) &&
                    ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", RestTemplate.class.getClassLoader());

    private static final boolean jackson2XmlPresent =
            ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", RestTemplate.class.getClassLoader());

    private static final boolean gsonPresent =
            ClassUtils.isPresent("com.google.gson.Gson", RestTemplate.class.getClassLoader());

    /**
     * 获取定义http消息转换器列表, 这里不共用转换器对象, 避免线程问题
     *
     * @return 定义http消息转换器列表
     */
    public static List<HttpMessageConverter<?>> getDefinedConvertors() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        List<MediaType> fsSupportMediaTypes = new ArrayList<>();
        fsSupportMediaTypes.add(MediaType.TEXT_HTML);
        fsSupportMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fsSupportMediaTypes);

        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
        httpMessageConverters.add(new ByteArrayHttpMessageConverter());
        httpMessageConverters.add(new StringHttpMessageConverter());
        httpMessageConverters.add(new ResourceHttpMessageConverter());
        httpMessageConverters.add(new SourceHttpMessageConverter<>());
        httpMessageConverters.add(new AllEncompassingFormHttpMessageConverter());
        httpMessageConverters.add(fastJsonHttpMessageConverter);

        if (romePresent) {
            httpMessageConverters.add(new AtomFeedHttpMessageConverter());
            httpMessageConverters.add(new RssChannelHttpMessageConverter());
        }

        if (jackson2XmlPresent) {
            httpMessageConverters.add(new MappingJackson2XmlHttpMessageConverter());
        } else if (jaxb2Present) {
            httpMessageConverters.add(new Jaxb2RootElementHttpMessageConverter());
        }

        return httpMessageConverters;
    }

}
