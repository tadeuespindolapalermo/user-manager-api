package br.com.gs3tecnologia.usermanager.config.security;

import br.com.gs3tecnologia.usermanager.config.security.service.JWTTokenAuthenticationService;
import br.com.gs3tecnologia.usermanager.domain.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import static br.com.gs3tecnologia.usermanager.util.Constants.METHOD_HTTP_OPTIONS;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

	protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {

		if (request.getMethod().equals(METHOD_HTTP_OPTIONS)) {
			return null;
		}

		User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
		
		return getAuthenticationManager()
			.authenticate(
				new UsernamePasswordAuthenticationToken(
					user.getEmail(), user.getPassword()
				)
			);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult){
		try {
			new JWTTokenAuthenticationService().addAuthentication(response, authResult.getName());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException {
		if (failed instanceof BadCredentialsException) {
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"data\": null, \"message\": \"Invalid username or password\", \"success\": false}");
		} else {
			response.getWriter().write("{\"data\": null, \"message\": \"" + failed.getMessage() +"\", \"success\": false}");
		}
	}

}
