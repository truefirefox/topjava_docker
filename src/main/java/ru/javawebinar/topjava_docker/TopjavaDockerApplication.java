package ru.javawebinar.topjava_docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@EnableCaching
public class TopjavaDockerApplication extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TopjavaDockerApplication.class);

    public static void main(String[] args) {
        log.info("started");
        SpringApplication.run(TopjavaDockerApplication.class, args);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(a -> a
                        .antMatchers("/")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .oauth2Login();
    }
}