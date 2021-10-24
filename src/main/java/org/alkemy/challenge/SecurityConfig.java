package org.alkemy.challenge;

import org.alkemy.challenge.services.DisneyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public DisneyUserService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin().and()
                .authorizeRequests()
                    .antMatchers("/characters/**", "/movies/**").hasAnyAuthority("CHARACTERS_MODULE", "MOVIES_MODULE")
                    .antMatchers("/**","/css/", "/js/", "/img/")
                    .permitAll()
                .and().formLogin()
                    .loginPage("/auth")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/auth/success")
                        .failureUrl("/auth/error")
                        .permitAll()
                    .and().logout()
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/")
                    .permitAll().and().csrf().disable();
    }
    
    
}
