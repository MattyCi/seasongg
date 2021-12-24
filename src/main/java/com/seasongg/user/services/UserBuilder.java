package com.seasongg.user.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.seasongg.user.Reguser;
import com.seasongg.user.ReguserRepository;
import com.seasongg.user.utils.UserUtils;
import org.passay.CharacterRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;

public class UserBuilder {

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private ReguserRepository reguserRepository;

	private Reguser regUser;
	
	private static final String NULL_REGUSER_ERROR_TEXT = "The regUser model object provided was null.";
	
	private static final String EMPTY_USERNAME_ERROR_TEXT = "No username was provided.";
	private static final String INVALID_USERNAME_ERROR_TEXT = "Your username can only contain alphanumeric characters.";
	private static final String USERNAME_TOO_LONG_ERROR_TEXT = "The username provided was too long.";
	private static final String USERNAME_ALREADY_EXISTS_ERROR_TEXT = "The username provided is already in use.";
	
	public static final String EMPTY_PASSWORD_ERROR_TEXT = "One of the passwords provided was empty.";
	public static final String PASSWORD_MISMATCH_ERROR_TEXT = "The passwords provided do not match.";

	public UserBuilder(Reguser regUser) throws UserBuilderException {

		if (regUser == null)
			throw new UserBuilderException(NULL_REGUSER_ERROR_TEXT);

		this.regUser = regUser;

		buildRegistrationTime();
		
	}

	private void buildRegistrationTime() {
		
		Timestamp registrationTime = new Timestamp(System.currentTimeMillis());
		
		regUser.setRegistrationTime(registrationTime);
		
	}

	public void buildUsername(String username) throws UserBuilderException {
		
		if (username == null || username.trim().isEmpty())
			throw new UserBuilderException(EMPTY_USERNAME_ERROR_TEXT);
		
		if (username.length() > 35)
			throw new UserBuilderException(USERNAME_TOO_LONG_ERROR_TEXT);
		
		username = username.trim();
		
		Pattern specialCharRegex = Pattern.compile("[^A-Za-z0-9'-]");
		
		Matcher specialCharMatcher = specialCharRegex.matcher(username);
		
		if (specialCharMatcher.find()) {
			throw new UserBuilderException(INVALID_USERNAME_ERROR_TEXT);
		}
		
		if (reguserRepository.findByUsernameIgnoreCase(username) != null) {
			throw new UserBuilderException(USERNAME_ALREADY_EXISTS_ERROR_TEXT);
		}
				
		this.regUser.setUsername(username);
		
	}
	
	public void buildPassword(String password, String passwordVerify) throws UserBuilderException {
		
		if (password == null || passwordVerify == null || password.trim().isEmpty() || passwordVerify.trim().isEmpty())
			throw new UserBuilderException(EMPTY_PASSWORD_ERROR_TEXT);
		
		if (!password.equals(passwordVerify))
			throw new UserBuilderException(PASSWORD_MISMATCH_ERROR_TEXT);
		
		PasswordValidator passwordValidator = UserUtils.createPasswordValidator();
		RuleResult passwordValidatorResult = passwordValidator.validate(new PasswordData(password));
		
		if (!passwordValidatorResult.isValid()) {
			throw new UserBuilderException(passwordValidator.getMessages(passwordValidatorResult).get(0));
		}

		userUtils.encodeAndSetPassword(this.regUser, password);
		
	}
	
	public String generateRandomPassword() {
		
		PasswordGenerator pwGenerator = new PasswordGenerator();
		
		ArrayList<CharacterRule> charRules = UserUtils.getPwCharacterRules();

		return pwGenerator.generatePassword(UserUtils.pwMax, charRules);
		
	}
	
	public static class UserBuilderException extends Exception {
		private static final long serialVersionUID = 4612687637887735068L;

		public UserBuilderException(String message) {
			super(message);
		}
		
	}
	
	public Reguser getReguser() {
		return this.regUser;
	}

}
