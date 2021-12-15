package com.api.login.config;

import com.api.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class JwtConfig extends WebSecurityConfigurerAdapter {

    private final UserService service;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public JwtConfig(UserService service, BCryptPasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //EM AMBIENTE PRODUTIVO DEIXAR ABILITADO O CSRF

       http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/login")
               .permitAll().anyRequest().authenticated().and()
               .addFilter(new JwtAutentificaFilter(authenticationManager()))
               .addFilter(new JwtValidFilter(authenticationManager()))
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
   }


//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/**");
//    }

    @Bean
    public CorsConfigurationSource configuration() {
        //APENAS PARA CONSULTAS EXTERNAS
        //CASO NAO PRECISE, APENAS IGNORE ESTA CONFIGURAÇÃO

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
