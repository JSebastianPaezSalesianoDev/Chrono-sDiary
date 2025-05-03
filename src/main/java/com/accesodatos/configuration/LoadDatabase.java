//package com.accesodatos.configuration;
//
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.accesodatos.entity.Role;
//import com.accesodatos.entity.UserEntity;
//import com.accesodatos.repository.security.UserRepository;
//
//@Configuration
//public class LoadDatabase {
//
//	@Bean
//	CommandLineRunner initDatabase(UserRepository userRepository) {
//		return arg -> {
//			Role roleAdmin = Role.builder().name("ADMIN").build();
//			
//			Role roleUser = Role.builder().name("USER").build();
//
//			UserEntity userAlex = UserEntity.builder().username("Alexis").email("ejemplito@gmail.com")
//					.password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC").isEnabled(true)
//					.accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true).roles(Set.of(roleAdmin))
//					.build();
//
//			UserEntity userJose = UserEntity.builder().username("Jose").email("ejemplito2@gmail.com")
//					.password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC").isEnabled(true)
//					.accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true).roles(Set.of(roleUser))
//					.build();
//
//			UserEntity userPepe = UserEntity.builder().username("Pepe").email("ejemplito3@gmail.com")
//					.password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC").isEnabled(true)
//					.accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true).roles(Set.of(roleUser))
//					.build();
//
//			UserEntity userIsrael = UserEntity.builder().username("Israel").email("ejemplito4@gmail.com")
//					.password("$2a$10$3S84.aE5GAxLMeXyDUFkruNnoQVE/UOM6iY35vtwirheoBfl7B9qC").isEnabled(true)
//					.accountNoExpired(true).accountNoLocked(true).credentialNoExpired(true).roles(Set.of(roleUser))
//					.build();
//
//			userRepository.saveAll(List.of(userAlex, userJose, userPepe, userIsrael));
//		};
//	}
//}