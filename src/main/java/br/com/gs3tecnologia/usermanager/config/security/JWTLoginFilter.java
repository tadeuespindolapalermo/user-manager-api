package br.com.gs3tecnologia.usermanager.config.security;

import br.com.gs3tecnologia.usermanager.config.security.service.JWTTokenAutenticacaoService;
import br.com.gs3tecnologia.usermanager.domain.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

//Estabelece gerenciador de token
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	// Configurando o gerenciador de autenticação
	protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		
		// Obriga a autenticar a url
		super(new AntPathRequestMatcher(url));
		
		// Gerenciador de autenticação
		setAuthenticationManager(authenticationManager);
	}

	// Retorna o usuário ao processar a autenticação
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {

		// Está pegando o token para validar
		if (request.getMethod().equals("OPTIONS")) {
			return null;
		}
		User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
		
		// Retorna o usuário login, senha e acesso		
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(
					user.getEmail(), user.getPassword()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult){
		try {
			new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
		} catch (Exception e) {
			e.printStackTrace();
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
