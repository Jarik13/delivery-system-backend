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
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api-docs/**",
                                "/api-docs",
                                "/api-docs/swagger-config"
                        ).permitAll()
                        .requestMatchers("/ws/**", "/ws").permitAll()

                        .requestMatchers("/api/v1/users/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/api/v1/profile/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "EMPLOYEE", "DRIVER", "COURIER")

                        .requestMatchers(HttpMethod.GET, "/api/v1/regions/**").hasAnyRole("SUPER_ADMIN", "EMPLOYEE", "DRIVER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/districts/**").hasAnyRole("SUPER_ADMIN", "EMPLOYEE", "DRIVER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/cities/**").hasAnyRole("SUPER_ADMIN", "EMPLOYEE", "DRIVER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/branches/**").hasAnyRole("SUPER_ADMIN", "EMPLOYEE", "DRIVER")

                        .requestMatchers(HttpMethod.POST, "/api/v1/regions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/regions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/regions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/districts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/districts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/districts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/cities/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/cities/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/cities/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/branches/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/branches/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/branches/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/box-types/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/box-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/box-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/box-types/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/box-variants/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/box-variants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/box-variants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/box-variants/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/parcel-types/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/parcel-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/parcel-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/parcel-types/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/shipment-types/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/shipment-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/shipment-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/shipment-types/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/shipment-statuses/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/shipment-statuses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/shipment-statuses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/shipment-statuses/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/storage-conditions/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/storage-conditions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/storage-conditions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/storage-conditions/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/return-reasons/**").hasAnyRole("EMPLOYEE", "COURIER", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/return-reasons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/return-reasons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/return-reasons/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/route-list-statuses/**").hasAnyRole("EMPLOYEE", "COURIER", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/route-list-statuses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/route-list-statuses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/route-list-statuses/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/branch-types/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/branch-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/branch-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/branch-types/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/payment-types/**").hasAnyRole("EMPLOYEE", "COURIER", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/payment-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/payment-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/payment-types/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/fleet-brands/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/fleet-brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/fleet-brands/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/fleet-brands/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/fleet-body-types/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/fleet-body-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/fleet-body-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/fleet-body-types/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/fleet-fuel-types/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/fleet-fuel-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/fleet-fuel-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/fleet-fuel-types/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/fleet-transmission-types/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/fleet-transmission-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/fleet-transmission-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/fleet-transmission-types/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/fleet-drive-types/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/fleet-drive-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/fleet-drive-types/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/fleet-drive-types/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/vehicle-activity-statuses/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/vehicle-activity-statuses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/vehicle-activity-statuses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/vehicle-activity-statuses/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/v1/trip-statuses/**").hasAnyRole("EMPLOYEE", "DRIVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/trip-statuses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/trip-statuses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/trip-statuses/**").hasRole("ADMIN")

                        .requestMatchers("/api/v1/trips/**").hasAnyRole("EMPLOYEE", "DRIVER")
                        .requestMatchers("/api/v1/waybills/**").hasAnyRole("EMPLOYEE", "DRIVER")
                        .requestMatchers("/api/v1/routes/**").hasAnyRole("EMPLOYEE", "DRIVER")
                        .requestMatchers("/api/v1/vehicles/**").hasAnyRole("EMPLOYEE", "DRIVER")
                        .requestMatchers("/api/v1/drivers/**").hasAnyRole("EMPLOYEE", "DRIVER")
                        .requestMatchers("/api/v1/fleets/**").hasAnyRole("EMPLOYEE", "DRIVER")

                        .requestMatchers("/api/v1/route-lists/**").hasAnyRole("EMPLOYEE", "COURIER", "DRIVER")

                        .requestMatchers("/api/v1/shipments/**").hasAnyRole("EMPLOYEE", "DRIVER")
                        .requestMatchers("/api/v1/parcels/**").hasAnyRole("EMPLOYEE", "DRIVER")
                        .requestMatchers("/api/v1/payments/**").hasAnyRole("EMPLOYEE", "COURIER", "DRIVER")
                        .requestMatchers("/api/v1/returns/**").hasAnyRole("EMPLOYEE", "COURIER", "DRIVER")
                        .requestMatchers("/api/v1/postomats/**").hasAnyRole("EMPLOYEE", "DRIVER")
                        .requestMatchers("/api/v1/streets/**").hasAnyRole("EMPLOYEE", "DRIVER")
                        .requestMatchers("/api/v1/address-houses/**").hasAnyRole("EMPLOYEE", "DRIVER")

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