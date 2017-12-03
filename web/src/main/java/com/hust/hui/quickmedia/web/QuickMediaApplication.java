package com.hust.hui.quickmedia.web;

import com.hust.hui.quickmedia.web.filter.WebActionInFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

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


	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return (container -> {
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");

			container.addErrorPages(error404Page, error500Page);
		});
	}
}
