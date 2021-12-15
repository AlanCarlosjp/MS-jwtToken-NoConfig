package com.api.login.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Configuration
public class JwtValidFilter extends BasicAuthenticationFilter {

    public static final String HEADER_ATRIBUTO = "Authorization";
    public static final String ATRIBUTO_PREFIX = "Bearer ";


    public JwtValidFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String atributo = request.getHeader(HEADER_ATRIBUTO);

        if (atributo == null || !atributo.startsWith(ATRIBUTO_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = atributo.replace(ATRIBUTO_PREFIX, "");

        UsernamePasswordAuthenticationToken authToken = getAuthToken(token);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(String token){
        String usuario = JWT.require(Algorithm.HMAC512(JwtAutentificaFilter.TOKE_KEY))
                .build().verify(token).getSubject();

        if(usuario == null){
            return null;
        }
        return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
    }

}
