package ru.javawebinar.topjava_docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableCaching
@RestController
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

    @GetMapping("/api/user")
    public Map<String, String> getUserInfo(@AuthenticationPrincipal OAuth2User user) {

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", user.getAttribute("email"));
        userInfo.put("id", user.getAttribute("sub"));

        return userInfo;
    }
}