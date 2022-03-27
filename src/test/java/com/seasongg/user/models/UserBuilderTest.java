package com.seasongg.user.models;

import com.seasongg.user.services.ReguserRepository;
import com.seasongg.user.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserBuilderTest {

	private UserBuilder userBuilder;

	private ReguserRepository reguserRepositoryMock;
	private UserUtils userUtilsMock;

	@BeforeEach
	void setup() {
		reguserRepositoryMock = mock(ReguserRepository.class);
		userUtilsMock = mock(UserUtils.class);

		userBuilder = new UserBuilder("placeholder", "placeholder", "placeholder");
		userBuilder.setReguserRepository(reguserRepositoryMock);
		userBuilder.setUserUtils(userUtilsMock);
	}

	@Test
	void should_SetUsername_When_InputOK() {
		// given
		String testUsername = "ValidUserName";

		// when
		userBuilder.username(testUsername);

		// then
		assertEquals(testUsername, userBuilder.getUsername());
	}

	@Test
	void should_ThrowException_When_UsernameNull() {
		// given
		String testUsername = null;

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userBuilder.username(testUsername);
		});

		// then
		assertEquals(UserBuilder.EMPTY_USERNAME_ERROR_TEXT, exception.getMessage());
	}

	@Test
	void should_ThrowException_When_UsernameEmpty() {
		// given
		String testUsername = "   ";

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userBuilder.username(testUsername);
		});

		// then
		assertEquals(UserBuilder.EMPTY_USERNAME_ERROR_TEXT, exception.getMessage());
	}

	@Test
	void should_ThrowException_When_UsernameTooLong() {
		// given
		String testUsername = "aLongLongLongLongLongLongLongUsername";

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userBuilder.username(testUsername);
		});

		// then
		assertEquals(UserBuilder.USERNAME_TOO_LONG_ERROR_TEXT, exception.getMessage());
	}

	@Test
	void should_ThrowException_When_UsernameContainsInvalidChars() {
		// given
		String testUsername = "special#?--Character$";

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userBuilder.username(testUsername);
		});

		// then
		assertEquals(UserBuilder.INVALID_USERNAME_ERROR_TEXT, exception.getMessage());
	}

	@Test
	void should_ThrowException_When_UsernameExists() {
		// given
		String testUsername = "already-existing-username";
		given(reguserRepositoryMock.findByUsernameIgnoreCase(anyString())).willReturn(new Reguser());

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userBuilder.username(testUsername);
		});

		// then
		assertEquals(UserBuilder.USERNAME_ALREADY_EXISTS_ERROR_TEXT, exception.getMessage());
	}

	@Test
	void should_TrimUsername() {
		// given
		String testUsername = "   needsToBeTrimmed 	 ";
		given(reguserRepositoryMock.findByUsernameIgnoreCase(anyString())).willReturn(null);

		// when
		userBuilder.username(testUsername);

		// then
		assertEquals("needsToBeTrimmed", userBuilder.getUsername());
	}

	@Test
	void should_HashAndSetPassword_When_InputOK() {
		// given
		String testPassword = "ValidPassword01";
		String testPasswordVerify = "ValidPassword01";
		given(userUtilsMock.hashPassword(testPassword)).willReturn("hashed-password");

		// when
		userBuilder.password(testPassword, testPasswordVerify);

		// then
		assertEquals("hashed-password", userBuilder.getPassword());
	}

	@Test
	void should_ThrowException_When_PasswordMissing() {
		// given
		List<String> missingPasswords = Arrays.asList(null, " 	  ", "");

		for (String missingPassword : missingPasswords) {

			// when
			Exception exception = assertThrows(IllegalArgumentException.class, () -> {
				userBuilder.password(missingPassword, "ValidPassword01");
			});

			// then
			assertEquals(UserBuilder.EMPTY_PASSWORD_ERROR_TEXT, exception.getMessage());

		}

	}

	@Test
	void should_ThrowException_When_PasswordVerifyMissing() {
		// given
		List<String> missingPasswordVerifyList = Arrays.asList(null, " 	  ", "");

		for (String missingPasswordVerify : missingPasswordVerifyList) {

			// when
			Exception exception = assertThrows(IllegalArgumentException.class, () -> {
				userBuilder.password("ValidPassword01", missingPasswordVerify);
			});

			// then
			assertEquals(UserBuilder.EMPTY_PASSWORD_ERROR_TEXT, exception.getMessage());

		}

	}

	@Test
	void should_ThrowException_When_PasswordsDoNotMatch() {
		// given
		String testPassword = "ValidPassword01";
		String testPasswordVerify = "ValidPassword02";

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userBuilder.password(testPassword, testPasswordVerify);
		});

		// then
		assertEquals(UserBuilder.PASSWORD_MISMATCH_ERROR_TEXT, exception.getMessage());
	}

	@Test
	void should_ThrowException_When_PasswordInvalid() {
		// given
		String testPassword = "all_lowercase";
		String testPasswordVerify = "all_lowercase";

		// when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			userBuilder.password(testPassword, testPasswordVerify);
		});

		// then
		assertEquals("Password must contain 1 or more uppercase characters.", exception.getMessage());
	}

}