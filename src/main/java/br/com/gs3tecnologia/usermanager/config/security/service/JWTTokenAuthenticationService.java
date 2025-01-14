package br.com.gs3tecnologia.usermanager.config.security.service;

import br.com.gs3tecnologia.usermanager.config.configuration.ApplicationContextLoad;
import br.com.gs3tecnologia.usermanager.domain.model.User;
import br.com.gs3tecnologia.usermanager.domain.repository.UserRepository;
import br.com.gs3tecnologia.usermanager.domain.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
public class JWTTokenAuthenticationService {

	private static final long EXPIRATION_TIME = 86400000;
	private static final String SECRET = "8z6EwPYz3nLwzgNqwgMKgDYhIWipy5a95DUaE39XoQ2nuCvK7PAB3aXcvE9hTQgc8z6EwPYz3nLwzgNqwgMKgDYhIWipy5a95DUaE39XoQ2nuCvK7PAB3aXcvE9hTQgc";
	private static final String TOKEN_PREFIX = "Bearer";
	private static final String HEADER_STRING = "Authorization";
	
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		User user = ApplicationContextLoad.getApplicationContext()
			.getBean(UserRepository.class)
			.findByEmail(username);

		List<String> profiles = Collections.singletonList(user.getProfile().getName());

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
		addCorsPolicyToResponse(response);

		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}
	
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = request.getHeader(HEADER_STRING);

		try {
			if (nonNull(token)) {
				String cleanToken = token.replace(TOKEN_PREFIX, EMPTY);
				Integer userId = (Integer) Jwts.parserBuilder()
					.setSigningKey(SECRET.getBytes())
					.build()
					.parseClaimsJws(cleanToken)
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
			response.getWriter().write("The token has expired! Please log in again!");
		} catch(MalformedJwtException e) {
			response.getWriter().write("The token is malformed! Use another token!");
		} catch(JwtException e) {
			response.getWriter().write("The token is invalid!");
		} finally {
			addCorsPolicyToResponse(response);
		}
		return null;
	}
	
	private void addCorsPolicyToResponse(HttpServletResponse response) {
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
