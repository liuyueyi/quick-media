package com.hust.hui.quickmedia.web;

import com.hust.hui.quickmedia.web.filter.WebActionInFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

@SpringBootApplication
public class QuickMediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickMediaApplication.class, args);
	}


	/**
	 * 创建一个bean
	 * @return
	 */
	@Bean(name = "webActionInFilter")
	public Filter webActionInFilter() {
		return new WebActionInFilter();
	}

	@Bean
	public FilterRegistrationBean someFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(webActionInFilter());
		registration.addUrlPatterns("/web/*");
		registration.setName("loginFilter");
		return registration;
	}
}
