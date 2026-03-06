package org.deliverysystem.com.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/ws/**", "/ws").permitAll()

                        // Тільки SUPER_ADMIN
                        .requestMatchers("/api/v1/users/**").hasRole("SUPER_ADMIN")

                        // Логістика — EMPLOYEE, DISPATCHER, ADMIN
                        .requestMatchers("/api/v1/shipments/**").hasAnyRole("EMPLOYEE", "DISPATCHER", "ADMIN")
                        .requestMatchers("/api/v1/parcels/**").hasAnyRole("EMPLOYEE", "DISPATCHER", "ADMIN")
                        .requestMatchers("/api/v1/payments/**").hasAnyRole("EMPLOYEE", "DISPATCHER", "ADMIN")
                        .requestMatchers("/api/v1/returns/**").hasAnyRole("EMPLOYEE", "DISPATCHER", "ADMIN")
                        .requestMatchers("/api/v1/waybills/**").hasAnyRole("EMPLOYEE", "DISPATCHER", "ADMIN")
                        .requestMatchers("/api/v1/trips/**").hasAnyRole("DISPATCHER", "ADMIN")
                        .requestMatchers("/api/v1/route-lists/**").hasAnyRole("COURIER", "DISPATCHER", "ADMIN")

                        // Мережа доставки — EMPLOYEE, ADMIN
                        .requestMatchers("/api/v1/branches/**").hasAnyRole("EMPLOYEE", "DISPATCHER", "ADMIN")
                        .requestMatchers("/api/v1/postomats/**").hasAnyRole("EMPLOYEE", "DISPATCHER", "ADMIN")
                        .requestMatchers("/api/v1/regions/**", "/api/v1/cities/**",
                                "/api/v1/districts/**", "/api/v1/streets/**",
                                "/api/v1/address-houses/**").hasAnyRole("EMPLOYEE", "DISPATCHER", "ADMIN")

                        // Автопарк — DISPATCHER, ADMIN
                        .requestMatchers("/api/v1/routes/**").hasAnyRole("DISPATCHER", "ADMIN")
                        .requestMatchers("/api/v1/fleets/**", "/api/v1/vehicles/**",
                                "/api/v1/drivers/**").hasAnyRole("DISPATCHER", "ADMIN")
                        .requestMatchers("/api/v1/fleet-brands/**", "/api/v1/fleet-body-types/**",
                                "/api/v1/fleet-fuel-types/**", "/api/v1/fleet-transmission-types/**",
                                "/api/v1/fleet-drive-types/**",
                                "/api/v1/vehicle-activity-statuses/**",
                                "/api/v1/trip-statuses/**").hasAnyRole("DISPATCHER", "ADMIN")

                        // Довідники — EMPLOYEE, DISPATCHER, ADMIN
                        .requestMatchers("/api/v1/box-types/**", "/api/v1/box-variants/**",
                                "/api/v1/parcel-types/**", "/api/v1/shipment-types/**",
                                "/api/v1/shipment-statuses/**", "/api/v1/storage-conditions/**",
                                "/api/v1/return-reasons/**", "/api/v1/route-list-statuses/**",
                                "/api/v1/branch-types/**", "/api/v1/payment-types/**")
                        .hasAnyRole("EMPLOYEE", "DISPATCHER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:3000"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null) return List.of();
            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles == null) return List.of();
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        });
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(
                "http://localhost:8180/realms/delivery-system"
        );
    }
}