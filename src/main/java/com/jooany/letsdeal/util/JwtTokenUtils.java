package com.jooany.letsdeal.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
	// 토큰에서 사용자 이름을 추출하는 메서드
	public static String getUserName(String token, String key) {
		return extractClaims(token, key).get("userName", String.class);
	}

	// 토큰이 만료되었는지 확인하는 메서드
	public static boolean isExpired(String token, String key) {
		try {
			// 토큰의 만료 시간을 가져옴
			Date expiredDate = extractClaims(token, key).getExpiration();
			// 만료 시간이 현재 시간 이전이면 토큰이 만료됨
			return expiredDate.before(new Date());
		} catch (ExpiredJwtException e) {
			// 만료 예외가 발생하면 토큰이 만료됨
			return true;
		}
	}

	// 토큰에서 클레임(정보)을 추출하는 메서드
	private static Claims extractClaims(String token, String key) {
		return Jwts.parserBuilder().setSigningKey(getKey(key))
			.build().parseClaimsJws(token).getBody();
	}

	// JWT 토큰 생성 메서드
	public String generateToken(String userName, String key, long expiredTimeMs) {
		Claims claims = Jwts.claims();
		claims.put("userName", userName);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
			.signWith(getKey(key), SignatureAlgorithm.HS256)
			.compact();
	}

	// 주어진 문자열을 기반으로 서명 검증을 위한 키 생성
	public static Key getKey(String key) {
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
