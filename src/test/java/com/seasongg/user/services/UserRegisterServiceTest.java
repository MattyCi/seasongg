package com.seasongg.user.services;

import com.seasongg.user.models.RegistrationRequest;
import com.seasongg.user.models.RegistrationResponse;
import com.seasongg.user.models.Reguser;
import com.seasongg.user.models.UserBuilder;
import com.seasongg.user.utils.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegisterServiceTest {

	@InjectMocks
	private UserRegisterService userRegisterService;

	@Mock
	private ReguserRepository reguserRepositoryMock;

	@Mock
	private ObjectProvider<UserBuilder> userBuildersMock;

	@Mock
	private UserUtils userUtilsMock;

	@Captor
	private ArgumentCaptor<Reguser> regUserArgumentCaptor;

	@Test
	void should_SaveUser_When_InputOK() throws Exception {
		// given
		RegistrationRequest registrationRequest = new RegistrationRequest(
				"test-user", "test-password", "test-password");

		given(userUtilsMock.isUserAuthenticated()).willReturn(false);

		Reguser expectedReguser = new Reguser();
		expectedReguser.setUsername("test-user");
		expectedReguser.setPassword("test-password");
		UserBuilder userBuilderMock = setupMockUserBuilder(expectedReguser);
		given(
				userBuildersMock.getObject("test-user", "test-password", "test-password")
		).willReturn(userBuilderMock);

		RegistrationResponse expected = new RegistrationResponse("test-user");

		// when
		RegistrationResponse actual = userRegisterService.registerUser(registrationRequest);

		//then
		then(userBuilderMock).should(times(1)).build();
		then(reguserRepositoryMock).should(times(1)).save(regUserArgumentCaptor.capture());
		Reguser capturedReguser = regUserArgumentCaptor.getValue();
		assertThat(expectedReguser).isEqualTo(capturedReguser);
		assertEquals(expected, actual);

	}

	private UserBuilder setupMockUserBuilder(Reguser expectedReguser) {
		UserBuilder userBuilderMock = mock(UserBuilder.class);
		when(userBuilderMock.build()).thenReturn(expectedReguser);
		return userBuilderMock;
	}

}