package com.demo.springbootrestdemo.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class ContentConfig extends WebMvcConfigurationSupport {

    /**
     * Defaults the content configuration we can support. Our current API supports both
     * - Accept header => 'application/json' or 'application/xml'
     * - or path extensions => GET 'localhost:8080/alive.json' or GET 'localhost:8080/alive.xml'
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer config) {
        config.favorPathExtension(true) // Allow content types to be specified as extensions
                .favorParameter(false) // Do not allow content types to be specified as a query parameter
                .ignoreAcceptHeader(false) // Honor any HTTP Accept header
                .ignoreUnknownPathExtensions(false) // Fail if an unknown path extension is given
                .defaultContentType(MediaType.APPLICATION_JSON); // Default to JSON
    }

    @Bean(name = "ObjectHttpMessageConverter")
    public ObjectHttpMessageConverter createObjectHttpMessageConverter() {
        // Example how to add/modify configuration of application by magically injecting a specific MVC message converter
        // This is required to get the @RequestBody to work with our supported content type 'application/x-www-form-urlencoded'
        ObjectHttpMessageConverter converter = new ObjectHttpMessageConverter();
        return converter;
    }

    /**
     * overrides existing message converters to add in our custom object converter for supporting @RequestBody mappings
     * for 'application/x-www-form-urlencoded' content types.
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(createObjectHttpMessageConverter());
        addDefaultHttpMessageConverters(converters);
    }

}
