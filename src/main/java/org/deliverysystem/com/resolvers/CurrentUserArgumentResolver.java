package org.deliverysystem.com.resolvers;

import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.annotations.CurrentUser;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.dtos.users.UserDbDataDto;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.services.strategy.UserPersistenceStrategyRegistry;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserPersistenceStrategyRegistry strategyRegistry;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
               && (parameter.getParameterType().equals(Integer.class)
                   || parameter.getParameterType().equals(CurrentUserDto.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        Principal principal = webRequest.getUserPrincipal();
        if (!(principal instanceof JwtAuthenticationToken jwtAuth)) return null;

        String keycloakId = jwtAuth.getToken().getSubject();
        String roleStr    = extractRole(jwtAuth);
        if (roleStr == null) return null;

        try {
            Role role = Role.valueOf(roleStr);
            Integer id = strategyRegistry.getStrategy(role)
                    .findByKeycloakId(keycloakId)
                    .map(UserDbDataDto::id)
                    .orElse(null);

            if (parameter.getParameterType().equals(Integer.class)) {
                return id;
            }

            return new CurrentUserDto(id, keycloakId, roleStr);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractRole(JwtAuthenticationToken jwtAuth) {
        var realmAccess = jwtAuth.getToken().getClaimAsMap("realm_access");
        if (realmAccess == null) return null;
        var roles = (List<String>) realmAccess.get("roles");
        if (roles == null) return null;
        return roles.stream()
                .filter(r -> List.of("EMPLOYEE", "COURIER", "DRIVER", "ADMIN").contains(r))
                .findFirst()
                .orElse(null);
    }
}