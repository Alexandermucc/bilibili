package com.alex.service.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandermucc
 * @date 2022/05/14 13:25
 **/

@Configuration
public class JsonHttpMessageConverterConfig {

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        Object o = new Object();
        list.add(o);
        list.add(o);
        System.out.println(list.size());
        System.out.println(JSONObject.toJSONString(list));
        System.out.println(JSONObject.toJSONString(list,SerializerFeature.DisableCircularReferenceDetect));
    }

    @Bean
    @Primary
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 设置日期格式
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        // 序列化相关配置
        fastJsonConfig.setSerializerFeatures(
                // Json格式化输出
                SerializerFeature.PrettyFormat,
                // 将value为null的数据转换为空字符串 String,List,Map
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue,
                // 升序排序
                SerializerFeature.MapSortField,
                // 禁用循环引用
                SerializerFeature.DisableCircularReferenceDetect
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }
}

