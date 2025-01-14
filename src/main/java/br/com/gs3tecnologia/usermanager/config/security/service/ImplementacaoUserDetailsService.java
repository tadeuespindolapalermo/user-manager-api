package br.com.gs3tecnologia.usermanager.config.security.service;

import br.com.gs3tecnologia.usermanager.domain.model.User;
import br.com.gs3tecnologia.usermanager.domain.repository.UserRepository;
import br.com.gs3tecnologia.usermanager.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ImplementacaoUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@SneakyThrows
    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User usuario = userRepository.findByEmail(username);

		if (isNull(usuario)) {
			throw new UsernameNotFoundException("User not found");
		}

		if (isNull(usuario.getProfile())) {
			throw new BusinessException("The user does not have any registered profile!");
		}

		return new org.springframework.security.core.userdetails.User(
			usuario.getUsername(), usuario.getPassword(), usuario.getAuthorities()
		);
	}

}
