package com.accesodatos.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
		info = @Info(
				title = "Chrono's Dairy",
				description = "Our app provides a Dairy to control our time and events",
				termsOfService = "www.accesodatos/terminos_y_condiciones",
				version = "1.0.0",
				contact = @Contact(
						name = "Diego Paez - Sebastian Paez",
						url = "https://aulasalesianoslacuesta.com/",
						email = "usr3859@salesianos-lacuesta.net - usr3766@salesianos-lacuesta.net"
				),
				license = @License(
						name = "Standard Software Use License for DAM salesinaos la cuesta",
						url = "http://lacuesta.salesianos.edu/licence"
				)
		),
		servers = {			
			@Server(
					description = "Server URL in Development enviroment",
					url = "http://localhost:8081"
			),
			@Server(
					description = "Server URL in Production enviroment",
					url = "http://localhost:8081"
			)
		} 
)
public class OpenAPIConfig {

}