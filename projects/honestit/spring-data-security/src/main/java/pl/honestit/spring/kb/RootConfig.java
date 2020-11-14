package pl.honestit.spring.kb;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@ComponentScan
@EnableWebMvc
@EnableJpaRepositories(basePackages = "pl.honestit.spring.kb.data.repository")
@EnableTransactionManagement
@EnableWebSecurity
public class RootConfig {

}
