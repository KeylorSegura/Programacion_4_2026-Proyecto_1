package progra4.proyecto_1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
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
                                "/login",
                                "/notAuthorized",
                                "/presentation/publico",
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
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            for (GrantedAuthority authority : authentication.getAuthorities()) {
                                String role = authority.getAuthority();

                                if ("Administrador".equals(role)) {
                                    response.sendRedirect("/presentation/admin/dashboard");
                                    return;
                                }

                                if ("Empresa".equals(role)) {
                                    response.sendRedirect("/presentation/empresa/dashboard");
                                    return;
                                }
                                if ("Oferente".equals(role)) {
                                    response.sendRedirect("/presentation/publico/principal");
                                    return;
                                }
                            }

                            response.sendRedirect("/presentation/publico/principal");
                        })
                        .failureHandler((request, response, exception) -> {
                            String message = exception.getClass().getSimpleName();

                            if ("DisabledException".equals(message)) {
                                response.sendRedirect("/login?pendingApproval=true");
                                return;
                            }

                            response.sendRedirect("/login?error=true");
                        })
                        .permitAll()
                )
                .logout(customizer -> customizer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
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
