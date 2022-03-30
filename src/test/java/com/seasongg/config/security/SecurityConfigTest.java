package com.seasongg.config.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

	@Test
	void should_UsePasswordEncoder() {
		// given
		SecurityConfig securityConfig = new SecurityConfig();

		// when
		PasswordEncoder encoder = securityConfig.passwordEncoder();

		// then
		assertInstanceOf(BCryptPasswordEncoder.class, encoder);
	}

}