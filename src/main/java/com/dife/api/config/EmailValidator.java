package com.dife.api.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailValidator {

	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|kookmin\\.ac\\.kr)$";

	public boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
