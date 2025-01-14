package br.com.gs3tecnologia.usermanager.config.security;

import br.com.gs3tecnologia.usermanager.config.security.service.JWTTokenAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtApiAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
		try {
			Authentication authentication = new JWTTokenAuthenticationService().getAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("A system error has occurred, please notify the administrator: " + e.getMessage());
		}
	}

}
