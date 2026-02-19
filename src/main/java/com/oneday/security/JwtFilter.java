package com.oneday.security;

import com.oneday.entity.AppUser;
import com.oneday.reposiratory.AppUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            final String accessToken  = request.getHeader("Authorization");
            if(accessToken==null || !accessToken.startsWith("Bearer")){
                filterChain.doFilter(request,response);
                return;
            }
            String jwtToken = accessToken.split("Bearer ")[1];
            log.info("going to verify token");
            String username = jwtUtil.verifyToken(jwtToken);
            log.info("token verified");
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                AppUser principle = appUserRepository.findByUsername(username).orElseThrow(
                        () -> new UsernameNotFoundException("couldn't find user to update the context holder!!")
                );
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(principle.getUsername(),null,null);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request,response);
        } catch (Exception ex){
            handlerExceptionResolver.resolveException(request,response,null,ex);
        }
    }
}
