package com.neu.video_recognize.config;

import com.neu.video_recognize.filter.CrossFilter;
import com.neu.video_recognize.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CrossFilter> registerFilter(){
        FilterRegistrationBean<CrossFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CrossFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("crossFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<LoginFilter> registerLoginFilter(){
        FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoginFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("loginFilter");
        return registrationBean;
    }
}
