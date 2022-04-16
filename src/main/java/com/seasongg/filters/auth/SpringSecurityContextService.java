package com.seasongg.filters.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Sets spring security contextual information.
 */
@Component
public class SpringSecurityContextService {

    @Autowired
    private UserDetailsService userDetailsService;

	void setSecurityContext(HttpServletRequest request, String username) {

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities()
		);

		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authToken);

	}

}
