package com.aptc.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

	public static String createJwt(SecretKey key, Long ttlMillis, Map<String, String> claims){
		JwtBuilder jwt = Jwts.builder()
				.expiration(new Date(System.currentTimeMillis() + ttlMillis))
				.signWith(key)
				.claims(claims);
		return jwt.compact();
	}

	public static Claims parse(SecretKey key, String jwt){
		Jws<Claims> jws;
		Claims claims = null;

		jws = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(jwt);
		claims = jws.getPayload();

		return claims;
	}


}
