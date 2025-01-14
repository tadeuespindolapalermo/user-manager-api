package br.com.gs3tecnologia.usermanager.config.security.service;

import br.com.gs3tecnologia.usermanager.config.configuration.ApplicationContextLoad;
import br.com.gs3tecnologia.usermanager.domain.model.User;
import br.com.gs3tecnologia.usermanager.domain.repository.UserRepository;
import br.com.gs3tecnologia.usermanager.domain.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class JWTTokenAutenticacaoService {

	@Value("${jwt.secret}")
	private String jwtSecret;
	
	// 24h
	private static final long EXPIRATION_TIME = 86400000;
	
	private static final String SECRET = "8z6EwPYz3nLwzgNqwgMKgDYhIWipy5a95DUaE39XoQ2nuCvK7PAB3aXcvE9hTQgc8z6EwPYz3nLwzgNqwgMKgDYhIWipy5a95DUaE39XoQ2nuCvK7PAB3aXcvE9hTQgc";
	
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		User user = ApplicationContextLoad.getApplicationContext()
			.getBean(UserRepository.class)
			.findByEmail(username);

		List<String> profiles = Collections.singletonList(user.getProfile().getName());

		// Montagem do Token
		String jwt = 
			Jwts.builder()
				.setSubject(username)
				.claim("roles", profiles)
				.claim("id", user.getId())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
				.compact();

		String token = TOKEN_PREFIX + " " + jwt;
		response.addHeader(HEADER_STRING, token);
		liberarCorsPolicy(response);

		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}
	
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = request.getHeader(HEADER_STRING);
		try {
			if (nonNull(token)) {
				String tokenLimpo = token.replace(TOKEN_PREFIX, "");
				Integer userId = (Integer) Jwts.parserBuilder()
					.setSigningKey(SECRET.getBytes())
					.build()
					.parseClaimsJws(tokenLimpo)
					.getBody()
					.get("id");

				if (nonNull(userId)){
					User user = ApplicationContextLoad.getApplicationContext()
						.getBean(UserService.class)
						.findById(Long.parseLong(userId.toString()));
					
					if (nonNull(user)) {
						return new UsernamePasswordAuthenticationToken(
							user.getEmail(), user.getPassword(), user.getAuthorities()
						);
					}
				}
			}
		} catch(ExpiredJwtException e) {
			response.getWriter().write("O token está expirado! Efetue o login novamente!");
		} catch(MalformedJwtException e) {
			response.getWriter().write("O token está mal formado! Utilize um outro token!");
		} catch(JwtException e) {
			response.getWriter().write("O token está inválido!");
		} finally {
			liberarCorsPolicy(response);
		}
		return null;
	}
	
	// CORS policy
	private void liberarCorsPolicy(HttpServletResponse response) {
		if (isNull(response.getHeader("Access-Control-Allow-Origin"))) {
			response.addHeader("Access-Control-Allow-Origin", "*");		
		}
		if (isNull(response.getHeader("Access-Control-Allow-Headers"))) {
			response.addHeader("Access-Control-Allow-Headers", "*");		
		}
		if (isNull(response.getHeader("Access-Control-Request-Headers"))) {
			response.addHeader("Access-Control-Request-Headers", "*");		
		}
		if (isNull(response.getHeader("Access-Control-Allow-Methods"))) {
			response.addHeader("Access-Control-Allow-Methods", "*");		
		}
	}

}
