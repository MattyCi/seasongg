package com.seasongg.user.utils;

import java.util.ArrayList;

import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserUtils {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static final int pwMin = 8;
	public static final int pwMax = 64;
	
	private static final LengthRule lengthRule = new LengthRule(pwMin, pwMax);
	private static final CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 1);
	private static final CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 1);
	private static final CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 1);
	private static final WhitespaceRule whitespaceRule = new WhitespaceRule();
	
	private static ArrayList<CharacterRule> pwRules = null;

	public static PasswordValidator createPasswordValidator() {
		return new PasswordValidator(lengthRule, upperCaseRule, lowerCaseRule, digitRule, whitespaceRule);
	}
	
	public static ArrayList<CharacterRule> getPwCharacterRules() {

		if (pwRules == null) {
			
			pwRules = new ArrayList<CharacterRule>();
			
			pwRules.add(upperCaseRule);
			pwRules.add(lowerCaseRule);
			pwRules.add(digitRule);

		}

		return pwRules;

	}

	public String hashPassword(String plainTextPassword) {
		return passwordEncoder.encode(plainTextPassword);
	}

	public boolean isUserAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() != null &&
				SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
				!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
	}

	public String generateRandomPassword() {

		PasswordGenerator pwGenerator = new PasswordGenerator();

		ArrayList<CharacterRule> charRules = UserUtils.getPwCharacterRules();

		return pwGenerator.generatePassword(UserUtils.pwMax, charRules);

	}
	
}
