package com.nickblogsite.springboot_backend.security;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.web.*;
import org.springframework.security.web.csrf.*;

import lombok.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

	private MyUserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain( final HttpSecurity httpSecurity ) throws Exception {
		return httpSecurity
			.csrf((csrf) -> csrf
					  .csrfTokenRepository( CookieCsrfTokenRepository.withHttpOnlyFalse() )
					  .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
				 )
			.authorizeHttpRequests(registry -> {
				registry.requestMatchers( "/api/register" ).permitAll();
				registry.requestMatchers( "/api/login" ).permitAll();
				registry.requestMatchers( "/api/**" ).permitAll();
				registry.anyRequest().anonymous();
			})
			.logout( LogoutConfigurer::permitAll )
			.build();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService( userDetailsService );
		provider.setPasswordEncoder( bCryptPasswordEncoder() );
		return provider;
	}
}
