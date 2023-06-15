package com.reto.usuario.infrastructure.configurations.security.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.usuario.domain.exceptions.EmailNotFoundException;
import com.reto.usuario.infrastructure.configurations.security.utils.TokenUtils;
import com.reto.usuario.infrastructure.configurations.security.UserDetailsServiceImpl;
import com.reto.usuario.infrastructure.exceptionhandler.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ") &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = bearerToken.replace("Bearer ", "").trim();

            if (!this.tokenUtils.validateToken(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(new ObjectMapper()
                        .writeValueAsString(Collections.
                                singletonMap("message", ExceptionResponse.TOKEN_INVALID.getMessage())));
                return;
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = TokenUtils.getAuthentication(token);
            String role = usernamePasswordAuthenticationToken.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()).get(0).replace("ROLE_", "");
            try {
                if (!this.userDetailsService.isValidateRoles(usernamePasswordAuthenticationToken.getName(), role)) {
                    handleUnauthorizedRoleException(response);
                    return;
                }
            } catch (EmailNotFoundException e) {
                handleEmailNotFoundException(response, e.getMessage());
                return;
            }
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private void handleUnauthorizedRoleException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper()
                .writeValueAsString(Collections.singletonMap("message", ExceptionResponse.ROLE_IN_TOKEN_IS_INVALID.getMessage())));
    }

    private void handleEmailNotFoundException(HttpServletResponse response, String messageException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper()
                .writeValueAsString(Collections.singletonMap("message", messageException)));
    }
}
