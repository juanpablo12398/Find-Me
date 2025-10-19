package edu.utn.proyecto.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final TokenService tokens;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    public SecurityConfig(TokenService tokens) {
        this.tokens = tokens;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(new JwtCookieFilter(tokens.secret()), AnonymousAuthenticationFilter.class)
                .authorizeHttpRequests(reg -> reg
                        // Estáticos y auth públicos
                        .requestMatchers("/", "/index.html", "/style.css", "/script.js", "/*.js", "/*.css").permitAll()
                        .requestMatchers(HttpMethod.GET, "/js/**", "/css/**", "/images/**", "/assets/**", "/favicon.ico").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Desaparecidos: GET público, POST autenticado
                        .requestMatchers(HttpMethod.GET, "/api/desaparecidos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/desaparecidos").authenticated()

                        // Registro de avistadores público (auto-login después)
                        .requestMatchers(HttpMethod.POST, "/api/avistadores").permitAll()

                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Parsear los orígenes permitidos desde la configuración
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
