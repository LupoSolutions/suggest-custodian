package com.lupo.suggestcustodian.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.google.gson.Gson;

@SpringBootApplication
@PropertySources({@PropertySource("classpath:/config/default.properties")})
@ComponentScan(basePackages = "com.lupo")
public class SuggestCustodianApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SuggestCustodianApplication.class,
                              args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SuggestCustodianApplication.class);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
