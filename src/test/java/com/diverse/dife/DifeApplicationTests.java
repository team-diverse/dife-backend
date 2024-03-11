package com.diverse.dife;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootTest
class DifeApplicationTests {

	// MySQL 테스트 코드
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConnection() {
		try (Connection con =
					 DriverManager.getConnection(
							 "jdbc:mysql://localhost:3306/dife_test?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&serverTimezone=Asia/Seoul",
							 "root",
							 "gusuyon04")) {
			System.out.println("DB Connection => " + con);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
