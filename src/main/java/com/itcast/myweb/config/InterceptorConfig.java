package com.itcast.myweb.config;


import com.itcast.myweb.interceptor.Interceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {// 拦截器配置

    private final StringRedisTemplate stringRedisTemplate;


    // 添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor(stringRedisTemplate))
                .addPathPatterns("/**")
                .excludePathPatterns("/myweb/c/auth/login")
                .excludePathPatterns("/myweb/c/auth/code")
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/doc.html/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/v2/api-docs")
                .excludePathPatterns("/v3/api-docs/**")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/doc.html/**");
//        WebMvcConfigurer.super.addInterceptors(registry);
    }


}
