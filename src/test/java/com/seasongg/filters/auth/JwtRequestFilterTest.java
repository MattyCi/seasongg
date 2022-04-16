package com.seasongg.filters.auth;

import com.seasongg.config.security.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

	@InjectMocks
	private JwtRequestFilter jwtRequestFilter;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private SpringSecurityContextService springSecurityContextService;

	HttpServletRequest mockRequest;

	@BeforeEach
	public void setup() {
		mockRequest = mock(HttpServletRequest.class);
	}

	@Test
	void should_SetSpringContext_When_InputOK() throws ServletException, IOException {
		// given
		String authBearer = "Bearer mock-jwt-string-here";
		given(mockRequest.getHeader("Authorization")).willReturn(authBearer);

		given(jwtUtil.getUsernameFromAuthHeader(mockRequest, authBearer)).willReturn("test-user");

		try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {

			SecurityContext securityContextMock = mock(SecurityContext.class);
			mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

		}

		// when
		jwtRequestFilter.doFilterInternal(mockRequest, mock(HttpServletResponse.class), mock(FilterChain.class));

		// then
		verify(springSecurityContextService).setSecurityContext(any(), eq("test-user"));

	}

	@Test
	void should_NotSetSpringContext_When_NoUsernameResolved() throws ServletException, IOException {
		// given
		String authBearer = "Bearer mock-jwt-string-here";
		given(mockRequest.getHeader("Authorization")).willReturn(authBearer);

		given(jwtUtil.getUsernameFromAuthHeader(mockRequest, authBearer)).willReturn(null);

		try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {

			SecurityContext securityContextMock = mock(SecurityContext.class);
			mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

		}

		// when
		jwtRequestFilter.doFilterInternal(mockRequest, mock(HttpServletResponse.class), mock(FilterChain.class));

		// then
		verify(springSecurityContextService, never()).setSecurityContext(any(), any());
	}

	@Test
	void should_DoFilter_Always() throws ServletException, IOException {
		// given
		var filterChain = mock(FilterChain.class);
		var mockResponse = mock(HttpServletResponse.class);

		// when
		jwtRequestFilter.doFilterInternal(mockRequest, mockResponse, filterChain);

		// then
		verify(filterChain).doFilter(mockRequest, mockResponse);

	}

}