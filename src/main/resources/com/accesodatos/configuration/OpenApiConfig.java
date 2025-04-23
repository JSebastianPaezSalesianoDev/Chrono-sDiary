package com.accesodatos.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(
		title = "WRITER_SERVICE", 
		description = "Our app provides a consece listing of wirters name", 
		termsOfService = "www-accesodatos/terminos_v_condiciones", 
		version = "1.0.0", 
		contact = @Contact(
				name = "Luis A.", 
				url = "https://salesianos-lacuesta.com", 
				email = "luis@salesianos-lacuesta.net"), 
		license = @License(
				name = "Standard Software Use License for Acceso a Datos", 
				url = "https://lacuesta.salesianos.edu/license")), 
	servers = {
			@Server(
					description = "Server URL in Development eviorment", 
					url = "http://localhost:8081"), 
			@Server(
					description = "Server URL in Development eviorment", 
					url = "http://localhost:8081" )
			}
)
public class OpenApiConfig {

}