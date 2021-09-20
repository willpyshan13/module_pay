package com.fjace.pay.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fjace
 * @version 1.0.0
 * @Description mvc 配置
 * @date 2021-09-10 20:15:00
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 允许跨域请求
     **/
    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);   //带上cookie信息
        config.addAllowedOriginPattern(CorsConfiguration.ALL);  //允许跨域的域名， *表示允许任何域名使用
        config.addAllowedOriginPattern(CorsConfiguration.ALL);  //使用addAllowedOriginPattern 避免出现 When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
        config.addAllowedHeader(CorsConfiguration.ALL);   //允许任何请求头
        config.addAllowedMethod(CorsConfiguration.ALL);   //允许任何方法（post、get等）
        source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
        return new CorsFilter(source);
    }
}
