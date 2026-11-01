package com.servicio.poliza.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CoreConfig {

	@Bean
	public RestClient coreRestClient(@Value("${integracion.core.url}") String url,
			@Value("${security.api-key}") String apiKey) {

		return RestClient.builder().baseUrl(url).defaultHeader("x-api-key", apiKey).build();
	}
}
