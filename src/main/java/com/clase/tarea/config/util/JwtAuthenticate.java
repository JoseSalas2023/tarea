package com.clase.tarea.config.util;

import com.clase.tarea.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class JwtAuthenticate extends OncePerRequestFilter {//Aqui nada mas estoy autenticando

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filter
    ) throws ServletException, IOException {

        Optional<String> tokenOptional = Optional.ofNullable(getTokenFromRequest(request));
        tokenOptional.ifPresent(token -> {
            String username = jwtService.getUserNameFromToken(token);
            Optional.ofNullable(username)
                    .filter(user -> SecurityContextHolder.getContext().getAuthentication() == null)
                    .ifPresent(user -> {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(user);

                        Optional.of(jwtService.isTokenValid(token, userDetails))
                                .filter(isValid -> isValid)
                                .ifPresent(isValid -> {
                                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                                    null,
                                            userDetails.getAuthorities()

                                    );
                                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                                    SecurityContextHolder.getContext().setAuthentication(authToken);
                                });
                    });
        });
        filter.doFilter(request, response);
    }


    private String getTokenFromRequest(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(s -> s.substring(7))
                .orElse(null);
    }

}
