package progra4.proyecto_1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(
                                "/",
                                "/presentation/publico",
                                "/presentation/login/form",
                                "/presentation/publico/principal",
                                "/presentation/publico/puestos",
                                "/presentation/publico/filtrar",
                                "/css/**",
                                "/images/**",
                                "/presentation/empresa/registrar",
                                "/presentation/oferente/registrar"
                        ).permitAll()
                        .requestMatchers(
                                "/presentation/admin/**"
                        ).hasAuthority("Administrador")
                        .requestMatchers(
                                "/presentation/empresa",
                                "/presentation/empresa/dashboard",
                                "/presentation/empresa/puesto/registrar",
                                "/presentation/empresa/puesto/toggle-activo/**",
                                "/presentation/empresa/puestos",
                                "/presentation/empresa/candidatos/**"
                        ).hasAuthority("Empresa")
                        .anyRequest().authenticated()
                )
                .formLogin(customizer -> customizer
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/presentation/publico/principal", true)
                        .failureUrl("/presentation/login/form?error=true")
                        .permitAll()
                )
                .logout(customizer -> customizer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/presentation/login/form?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(customizer -> customizer
                        .accessDeniedPage("/notAuthorized")
                )
                .csrf(customizer -> customizer.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
