package com.seasongg.user.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserUtilsTest {

	@InjectMocks
	private UserUtils userUtilsTest;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	void should_setPwRules_When_NoneAreSet() {

		// given
		ArrayList<CharacterRule> expectedRules = setupExpectedPasswordRules();

		// when
		ArrayList<CharacterRule> actualRules = UserUtils.getPwCharacterRules();

		// then
		assertEquals(expectedRules.size(), actualRules.size());

		for (int i=0; i < expectedRules.size(); i++) {

			String expectedRule = expectedRules.get(i).toString().split("::")[1];
			String actualRule = actualRules.get(i).toString().split("::")[1];

			assertEquals(expectedRule, actualRule);

		}

	}

	private ArrayList<CharacterRule> setupExpectedPasswordRules() {

		// these must be given in the same order as they appear in the UserUtils class
		return new ArrayList<>(
			Arrays.asList(
				new CharacterRule(EnglishCharacterData.UpperCase, 1),
				new CharacterRule(EnglishCharacterData.LowerCase, 1),
				new CharacterRule(EnglishCharacterData.Digit, 1)
			)
		);
	}

	@Test
	void should_hashPassword() {

		// given
		given(passwordEncoder.encode(anyString())).willReturn("hashed-password");

		// when
		String actual = userUtilsTest.hashPassword("plaintext-password");

		// then
		then(passwordEncoder).should(times(1)).encode(anyString());
		assertEquals("hashed-password", actual);

	}

}