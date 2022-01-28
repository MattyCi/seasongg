package com.seasongg.user.services;

import com.seasongg.config.security.util.JwtUtil;
import com.seasongg.user.models.AuthenticationRequest;
import com.seasongg.user.models.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserLoginServiceTest {

	@InjectMocks
	private UserLoginService userLoginService;

	@Mock
	private AuthenticationManager authenticationManagerMock;

	@Mock
	private JwtUtil jwtUtilMock;

	@Test
	void should_Login_When_InputOK() {
		// given
		given(authenticationManagerMock.authenticate(any())).willReturn(
				new UsernamePasswordAuthenticationToken("test-username", "test-password"));
		given(jwtUtilMock.generateToken(any())).willReturn("test-jwt");
		AuthenticationResponse expected = new AuthenticationResponse("test-jwt");

		// when
		AuthenticationResponse actual = userLoginService.loginUser(
				new AuthenticationRequest("test-username", "test-password"));

		//then
		assertEquals(expected, actual);
	}

	@Test
	void should_ThrowBadCredentialsException_When_WrongUsernameUsed() {
		// given
		given(authenticationManagerMock.authenticate(any())).willThrow(BadCredentialsException.class);

		// when
		Executable executable = () -> userLoginService.loginUser(
				new AuthenticationRequest("wrong-username", "test-password"));

		//then
		assertThrows(BadCredentialsException.class, executable);

	}

}
