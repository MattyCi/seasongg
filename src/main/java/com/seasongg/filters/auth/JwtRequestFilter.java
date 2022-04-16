package com.seasongg.filters.auth;

import com.seasongg.config.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

	@Autowired
	private SpringSecurityContextService springSecurityContextService;

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		String username = jwtUtil.getUsernameFromAuthHeader(request, authorizationHeader);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			springSecurityContextService.setSecurityContext(request, username);

		}

        filterChain.doFilter(request, response);

    }

}
