package com.keshe.server.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.dir:${user.dir}/uploads}")
    private String uploadDir;

    @Value("${file.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 纭繚璺緞鏍煎紡姝ｇ‘锛屼娇鐢ㄦ爣鍑嗙殑鏂囦欢璺緞鏍煎紡
        String absolutePath;
        if (uploadDir.endsWith("/") || uploadDir.endsWith("\\")) {
            absolutePath = uploadDir;
        } else {
            absolutePath = uploadDir + File.separator;
        }
        
        // 娣诲姞鏂囦欢鍓嶇紑锛岀‘淇漇pring鑳芥纭槧灏勫埌鏈湴鏂囦欢绯荤粺
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolutePath)
                .setCachePeriod(3600) // 缂撳瓨1灏忔椂锛屾彁楂樻€ц兘
                .resourceChain(true);
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
