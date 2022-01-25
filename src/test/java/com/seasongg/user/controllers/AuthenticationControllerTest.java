package com.seasongg.user.controllers;

import com.seasongg.common.RestResponse;
import com.seasongg.common.SggService;
import com.seasongg.user.models.AuthenticationRequest;
import com.seasongg.user.models.AuthenticationResponse;
import com.seasongg.user.models.RegistrationRequest;
import com.seasongg.user.models.RegistrationResponse;
import com.seasongg.user.services.UserLoginService;
import com.seasongg.user.services.UserRegisterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

	@InjectMocks
	private AuthenticationController authenticationController;

	@Mock
	private UserLoginService userLoginServiceMock;

	@Mock
	private UserRegisterService userRegisterServiceMock;

	@Test
	void should_Login_When_InputOK() {
		// given
		given(userLoginServiceMock.loginUser(any())).willReturn(new AuthenticationResponse("test-jwt"));
		ResponseEntity<AuthenticationResponse> expected = ResponseEntity.ok(
				new AuthenticationResponse("test-jwt"));

		// when
		ResponseEntity<?> actual = authenticationController.login(
				new AuthenticationRequest("test-user", "test-password"));

		//then
		assertEquals(expected, actual);
	}

	@Test
	void should_ThrowBadCredentialsException_When_LoginFailed() {
		// given
		given(userLoginServiceMock.loginUser(any())).willThrow(BadCredentialsException.class);

		// when
		Executable executable = () -> authenticationController.login(
				new AuthenticationRequest("test-user", "wrong-password"));

		//then
		assertThrows(BadCredentialsException.class, executable);
	}

	@Test
	void should_ReturnUnknownErrorResponse_When_UnexpectedErrorOccursDuringLogin() {
		// given
		given(userLoginServiceMock.loginUser(any())).willThrow(RuntimeException.class);
		ResponseEntity<RestResponse> expected = new ResponseEntity<>(new RestResponse(SggService.UNKNOWN_ERROR,
				SggService.UNKNOWN_ERROR_TEXT), HttpStatus.INTERNAL_SERVER_ERROR);

		// when
		ResponseEntity<?> actual = authenticationController.login(
				new AuthenticationRequest("test-user", "test-password"));

		//then
		assertEquals(expected, actual);
	}

	@Test
	void should_Register_When_InputOK() throws UserRegisterService.RegistrationException {
		// given
		given(userRegisterServiceMock.registerUser(any())).willReturn(new RegistrationResponse("test-user"));
		ResponseEntity<RegistrationResponse> expected = ResponseEntity.ok(
				new RegistrationResponse("test-user"));

		// when
		ResponseEntity<?> actual = authenticationController.register(
				new RegistrationRequest("test-user", "test-password", "test-password"));

		//then
		assertEquals(expected, actual);
	}

	@Test
	void should_ReturnApplicationErrorResponse_When_ExpectedErrorOccursDuringRegistration()
			throws UserRegisterService.RegistrationException {
		// given
		given(userRegisterServiceMock.registerUser(any())).willThrow(
				new UserRegisterService.RegistrationException("An expected error occurred."));
		ResponseEntity<RestResponse> expected = new ResponseEntity<>(new RestResponse(SggService.APPLICATION_ERROR,
				"An expected error occurred."), HttpStatus.BAD_REQUEST);

		// when
		ResponseEntity<?> actual = authenticationController.register(
				new RegistrationRequest("test-user", "test-password", "test-password"));

		//then
		assertEquals(expected, actual);
	}

	@Test
	void should_ReturnUnknownErrorResponse_When_UnexpectedErrorOccursDuringRegistration()
			throws UserRegisterService.RegistrationException {
		// given
		given(userRegisterServiceMock.registerUser(any())).willThrow(RuntimeException.class);
		ResponseEntity<RestResponse> expected = new ResponseEntity<>(new RestResponse(SggService.UNKNOWN_ERROR,
				SggService.UNKNOWN_ERROR_TEXT), HttpStatus.INTERNAL_SERVER_ERROR);

		// when
		ResponseEntity<?> actual = authenticationController.register(
				new RegistrationRequest("test-user", "test-password", "test-password"));

		//then
		assertEquals(expected, actual);
	}

}