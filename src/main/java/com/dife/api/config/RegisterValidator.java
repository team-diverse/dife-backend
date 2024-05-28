package com.dife.api.config;

import com.dife.api.exception.RegisterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegisterValidator {

	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|kookmin\\.ac\\.kr)$";
	private static final String PASSWORD_REGEX = "(?=.*[0-9a-zA-Z\\\\W]).{8,20}";

	public void registerValidate(String email, String password) {
		if (!isValidEmail(email) || !isValidPassword(password))
			throw new RegisterException("옳지 않은 등록 형식입니다!");
	}

	public boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public boolean isValidPassword(String password) {
		Pattern pattern = Pattern.compile(PASSWORD_REGEX);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
}
