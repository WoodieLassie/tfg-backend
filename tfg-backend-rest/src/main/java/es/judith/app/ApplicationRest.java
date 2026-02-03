package es.judith.app;

import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.MultipartConfigElement;



@SpringBootApplication
@EntityScan({"es.judith.domain","es.judith.security.domain"})
@EnableJpaRepositories({"es.judith.dao", "es.judith.security.dao"})
@ComponentScan({"es.judith.rest", "es.judith.controller", "es.judith.bo", "es.judith.config",
    "es.judith.security", "es.judith.dao"})
@EnableScheduling
@EnableSpringDataWebSupport
@EnableAsync
public class ApplicationRest extends SpringBootServletInitializer {
  public static void main(final String[] args) {
    SpringApplication.run(ApplicationRest.class);
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    return new MultipartConfigElement("");
  }

  @Bean(name = "multipartResolver")
  public StandardServletMultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
  }

  @Bean
  public LocalValidatorFactoryBean validator(MessageSource messageSource) {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }

  @Bean(name = "localeResolver")
  public LocaleResolver localeResolver() {
    return new SessionLocaleResolver();
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(ApplicationRest.class);
  }

  @PostConstruct
  void started() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    Locale.setDefault(new Locale("en"));
  }

  @Bean
  public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
    return new BufferedImageHttpMessageConverter();
  }
}
