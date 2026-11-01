package com.servicio.poliza.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

	@Value("${security.api-key}")
	private String apiKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String receivedKey = request.getHeader("x-api-key");

		if (!apiKey.equals(receivedKey)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			response.getWriter().write("{\"mensaje\":\"API Key inválida o no proporcionada\"}");

			return;
		}

		filterChain.doFilter(request, response);
	}
}
