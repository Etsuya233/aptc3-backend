package com.aptc.configuration;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Data
@Component
@ConfigurationProperties(prefix = "ety.jwt")
@Slf4j
public class JwtProperties {

	private String adminSecretKey;
	private SecretKey adminKeyObj;
	private long adminTtl;
	private String adminTokenName;

	private String userSecretKey;
	private SecretKey userKeyObj;
	private long userTtl;
	private String userTokenName; //前端的Header的哪个东西存了Token

	public void setAdminSecretKey(String adminSecretKey) {
		this.adminSecretKey = adminSecretKey;
		adminKeyObj = Keys.hmacShaKeyFor(adminSecretKey.getBytes(StandardCharsets.UTF_8));
	}

	public void setUserSecretKey(String userSecretKey) {
		this.userSecretKey = userSecretKey;
		userKeyObj = Keys.hmacShaKeyFor(userSecretKey.getBytes(StandardCharsets.UTF_8));
	}

}
