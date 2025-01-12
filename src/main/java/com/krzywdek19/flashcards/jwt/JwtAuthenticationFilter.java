package com.krzywdek19.flashcards.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);
            final String username = jwtService.extractUsername(token);
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if(username != null && authentication == null){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if(jwtService.isTokenValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                username, null, authentication.getAuthorities()
                        );

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }catch (ExpiredJwtException e){
                handlerExceptionResolver.resolveException(request, response, null,
                        new RuntimeException("Expired JWT token"));
            }catch (MalformedJwtException e) {
                handlerExceptionResolver.resolveException(request, response, null,
                        new RuntimeException("Token is malformed"));
            } catch (UnsupportedJwtException e) {
                handlerExceptionResolver.resolveException(request, response, null,
                        new RuntimeException("Token type is unsupported"));
            } catch (Exception e) {
                handlerExceptionResolver.resolveException(request, response, null, e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
