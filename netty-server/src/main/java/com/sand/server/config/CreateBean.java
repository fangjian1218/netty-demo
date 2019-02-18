package com.sand.server.config;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateBean {

    @Bean
    public StringDecoder createStringDecoder() {
        return new StringDecoder();
    }

    @Bean
    public StringEncoder createStringEncoder() {
        return new StringEncoder();
    }
}
