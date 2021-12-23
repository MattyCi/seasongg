package com.seasongg.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seasongg.common.RestResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

@Component
public class JwtExceptionMessageHandler implements AuthenticationEntryPoint {

    @Autowired
    private MessageSource messageSource;

    private static final String _ERR_BAD_CREDENTIALS = "_ERR_BAD_CREDENTIALS";
    private static final String _ERR_INVALID_AUTHENTICATION = "_ERR_INVALID_AUTHENTICATION";
    private static final String _ERR_SESSION_EXPIRED = "_ERR_SESSION_EXPIRED";

    private final Logger LOG = LoggerFactory.getLogger(JwtExceptionMessageHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();

        String exceptionMessage = resolveExceptionMessage(request, authException);

        mapper.writeValue(out, new RestResponse(HttpServletResponse.SC_UNAUTHORIZED,
                exceptionMessage));
        out.flush();

    }

    private String resolveExceptionMessage(HttpServletRequest request, AuthenticationException authException) {

        JwtException jwtException = (JwtException) request.getAttribute("jwtException");

        if (jwtException != null) {
            if (jwtException instanceof ExpiredJwtException) {
                return messageSource.getMessage(_ERR_SESSION_EXPIRED, null, Locale.ENGLISH);
            } else {
                LOG.error("Possible JWT tampering detected: {}", jwtException.getMessage());
                return messageSource.getMessage(_ERR_INVALID_AUTHENTICATION, null, Locale.ENGLISH);
            }
        } else if (authException instanceof BadCredentialsException) {
            return messageSource.getMessage(_ERR_BAD_CREDENTIALS, null, Locale.ENGLISH);
        } else {
            return authException.getMessage();
        }

    }

}
