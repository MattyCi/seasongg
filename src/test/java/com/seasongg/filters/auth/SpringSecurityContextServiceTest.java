package com.seasongg.filters.auth;

import com.seasongg.user.models.Reguser;
import com.seasongg.user.models.permissions.Permission;
import com.seasongg.user.models.permissions.UserPermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringSecurityContextServiceTest {

	@InjectMocks
	private SpringSecurityContextService springSecurityContextService;

	@Mock
	private UserDetailsService userDetailsService;

	Reguser mockReguser;
	HttpServletRequest mockRequest;

	@BeforeEach
	public void setup() {
		mockRequest = mock(HttpServletRequest.class);

		mockReguser = new Reguser();
		mockReguser.setUsername("test-user");

		Permission expectedPermission = new Permission();
		expectedPermission.setPermId(-1);
		expectedPermission.setPermValue("test-value");

		UserPermission expectedUserPermission = new UserPermission();
		expectedUserPermission.setPermission(expectedPermission);

		mockReguser.setUserPermissions(List.of(expectedUserPermission));

	}

	@Test
	void should_SetUsername_When_InputOK() {
		try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class)) {
			// given
			given(userDetailsService.loadUserByUsername(anyString())).willReturn(mockReguser);
			SecurityContext mockedSecurityContext = mock(SecurityContext.class);
			mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(mockedSecurityContext);

			// when
			springSecurityContextService.setSecurityContext(mockRequest, mockReguser.getUsername());

			// then
			verify(mockedSecurityContext).setAuthentication(any());

		}

	}

}