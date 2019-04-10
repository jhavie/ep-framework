package com.easipass;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.easipass.sys.service.impl.RedisUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan(value = {"com.easipass.business.servlet"})
public class Application{

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        //return builder.build();
        //解决返回串中文乱码问题
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return builder.additionalMessageConverters(m).build();
    }

    @Bean(initMethod = "init")
    public RedisUtil redisUtil(){
        return new RedisUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
