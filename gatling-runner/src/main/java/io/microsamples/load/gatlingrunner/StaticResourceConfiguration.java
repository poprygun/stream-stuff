package io.microsamples.load.gatlingrunner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class StaticResourceConfiguration implements WebMvcConfigurer {

    @Value("${reports.location:classpath:/static/}")
    private String reportsLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Loading reports from: {}", reportsLocation);
        registry.addResourceHandler("/**").addResourceLocations(reportsLocation);
    }

}
