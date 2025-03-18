package com.zl.mjga.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class I18nConfig {

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setAlwaysUseMessageFormat(false);
    messageSource.setFallbackToSystemLocale(false);
    messageSource.setUseCodeAsDefaultMessage(true);
    messageSource.setDefaultLocale(Locale.US);
    messageSource.setCacheSeconds(3600); // Cache for an hour
    return messageSource;
  }

  @Bean
  public LocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver localeResolver =
        new AcceptHeaderLocaleResolver() {
          @Override
          public Locale resolveLocale(HttpServletRequest request) {
            String locale = request.getParameter("lang");
            return locale != null
                ? StringUtils.parseLocaleString(locale)
                : super.resolveLocale(request);
          }
        };
    localeResolver.setDefaultLocale(Locale.US);
    return localeResolver;
  }
}
