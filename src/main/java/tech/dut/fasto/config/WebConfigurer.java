package tech.dut.fasto.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tech.dut.fasto.config.properties.FastoProperties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Configuration
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@EnableWebMvc
public class WebConfigurer implements ServletContextInitializer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    private final FastoProperties fastoProperties;

    private final Environment env;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }

        log.info("Web application fully configured");
    }

    @Bean
    public CorsFilter corsConfigurationSource() {
        log.debug("Registering CORS filter");
        CorsConfiguration corsConfiguration = fastoProperties.getCors();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if (!CollectionUtils.isEmpty(corsConfiguration.getAllowedOrigins()) || !CollectionUtils.isEmpty(corsConfiguration.getAllowedOriginPatterns())){
            log.debug("Registering CORS filter");
            source.registerCorsConfiguration("/**", corsConfiguration);
            source.registerCorsConfiguration("/v2/docs", corsConfiguration);
            source.registerCorsConfiguration("/swagger-ui/**", corsConfiguration);
        }
        return new CorsFilter(source);
    }
}
