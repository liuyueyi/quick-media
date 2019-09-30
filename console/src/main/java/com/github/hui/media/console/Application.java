package com.github.hui.media.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created by @author yihui in 17:48 19/8/15.
 */
@ServletComponentScan
@SpringBootApplication
public class Application  {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
