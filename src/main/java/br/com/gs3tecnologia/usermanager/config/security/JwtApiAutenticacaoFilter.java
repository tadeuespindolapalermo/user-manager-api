package br.com.gs3tecnologia.usermanager.config.security;

import br.com.gs3tecnologia.usermanager.config.security.service.JWTTokenAutenticacaoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

// Filtro onde todas as requisições serão capturadas para autenticar
public class JwtApiAutenticacaoFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
		try {
			// Estabelece autenticação para a requisição
			Authentication authentication = new JWTTokenAutenticacaoService().getAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
			
			// Coloca o processo de autenticação no spring security
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			// Continua o processo
			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("Ocorreu um erro no sistema, avise o administrador: \n" + e.getMessage());
		}
	}

}
