package com.seasongg.user.services;

import com.seasongg.user.models.Reguser;
import com.seasongg.user.models.permissions.Permission;
import com.seasongg.user.models.permissions.UserPermission;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SggUserDetailsServiceTest {

	@InjectMocks
	private SggUserDetailsService sggUserDetailsService;

	@Mock
	private ReguserRepository reguserRepositoryMock;

	@Test
	void should_LoadUserDetails_When_InputOK() {

		try (MockedStatic<Hibernate> mockedHibernate = mockStatic(Hibernate.class)) {

			// given
			List<UserPermission> expectedPermissions = setupPermissions();

			Reguser expectedReguser = new Reguser();
			expectedReguser.setUsername("test-user");
			expectedReguser.setPassword("test-password");
			expectedReguser.setUserPermissions(expectedPermissions);

			given(reguserRepositoryMock.findByUsernameIgnoreCase("test-user")).willReturn(expectedReguser);

			// when
			UserDetails actual = sggUserDetailsService.loadUserByUsername("test-user");

			// then
			then(reguserRepositoryMock).should(times(1)).findByUsernameIgnoreCase("test-user");

			mockedHibernate.verify(
					() -> Hibernate.initialize(expectedPermissions),
					times(1)
			);

			assertEquals(expectedReguser, actual);

		}

	}

	private List<UserPermission> setupPermissions() {

		Permission expectedPermission = new Permission();
		expectedPermission.setPermId(-1);
		expectedPermission.setPermValue("test-value");

		UserPermission expectedUserPermission = new UserPermission();
		expectedUserPermission.setPermission(expectedPermission);

		return List.of(expectedUserPermission);

	}

	@Test
	void should_ThrowException_When_NoUserLoaded() {
		// given
		given(reguserRepositoryMock.findByUsernameIgnoreCase(any())).willReturn(null);

		// when
		Executable executable = () -> sggUserDetailsService.loadUserByUsername("test-user");

		// then
		assertThrows(UsernameNotFoundException.class, executable);

	}

}