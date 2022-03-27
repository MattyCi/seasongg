package com.seasongg.user.models;

import com.seasongg.user.services.ReguserRepository;
import com.seasongg.user.utils.UserUtils;
import org.passay.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserBuilder implements InitializingBean {

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private ReguserRepository reguserRepository;

	private String username;
	private String password;
	private final String passwordVerify;
	private Timestamp registrationTime;

	public static final String EMPTY_USERNAME_ERROR_TEXT = "No username was provided.";
	public static final String INVALID_USERNAME_ERROR_TEXT = "Your username can only contain alphanumeric characters.";
	public static final String USERNAME_TOO_LONG_ERROR_TEXT = "The username provided was too long.";
	public static final String USERNAME_ALREADY_EXISTS_ERROR_TEXT = "The username provided is already in use.";

	public static final String EMPTY_PASSWORD_ERROR_TEXT = "One of the provided passwords was empty.";
	public static final String PASSWORD_MISMATCH_ERROR_TEXT = "The passwords provided do not match.";

	public UserBuilder(String username, String password, String passwordVerify) throws IllegalArgumentException {
		this.username = username;
		this.password = password;
		this.passwordVerify = passwordVerify;
	}

	@Override
	public void afterPropertiesSet() throws IllegalArgumentException {
		username(username);
		password(password, passwordVerify);
		this.registrationTime = new Timestamp(System.currentTimeMillis());
	}

	public void username(String username) throws IllegalArgumentException {

		if (username == null || username.trim().isEmpty())
			throw new IllegalArgumentException(EMPTY_USERNAME_ERROR_TEXT);

		if (username.length() > 35)
			throw new IllegalArgumentException(USERNAME_TOO_LONG_ERROR_TEXT);

		username = username.trim();

		Pattern specialCharRegex = Pattern.compile("[^A-Za-z0-9'-]");

		Matcher specialCharMatcher = specialCharRegex.matcher(username);

		if (specialCharMatcher.find()) {
			throw new IllegalArgumentException(INVALID_USERNAME_ERROR_TEXT);
		}

		if (reguserRepository.findByUsernameIgnoreCase(username) != null)
			throw new IllegalArgumentException(USERNAME_ALREADY_EXISTS_ERROR_TEXT);

		this.username = username;

	}

	public void password(String password, String passwordVerify) throws IllegalArgumentException {

		if (password == null || passwordVerify == null || password.trim().isEmpty() || passwordVerify.trim().isEmpty())
			throw new IllegalArgumentException(EMPTY_PASSWORD_ERROR_TEXT);

		if (!password.equals(passwordVerify))
			throw new IllegalArgumentException(PASSWORD_MISMATCH_ERROR_TEXT);

		PasswordValidator passwordValidator = UserUtils.createPasswordValidator();
		RuleResult passwordValidatorResult = passwordValidator.validate(new PasswordData(password));

		if (!passwordValidatorResult.isValid()) {
			throw new IllegalArgumentException(passwordValidator.getMessages(passwordValidatorResult).get(0));
		}

		this.password = userUtils.hashPassword(password);

	}

	public Reguser build() {
		return new Reguser(this);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Timestamp getRegistrationTime() {
		return registrationTime;
	}

	/** Needed for unit testing only. */
	public void setReguserRepository(ReguserRepository reguserRepository) {
		this.reguserRepository = reguserRepository;
	}

	/** Needed for unit testing only. */
	public void setUserUtils(UserUtils userUtils) {
		this.userUtils = userUtils;
	}

}
