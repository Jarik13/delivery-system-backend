package org.deliverysystem.com.services.strategy;

import org.deliverysystem.com.enums.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserPersistenceStrategyRegistry {
    private final Map<Role, UserPersistenceStrategy> strategies;

    public UserPersistenceStrategyRegistry(List<UserPersistenceStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(UserPersistenceStrategy::getSupportedRole, Function.identity()));
    }

    public UserPersistenceStrategy getStrategy(Role role) {
        UserPersistenceStrategy strategy = strategies.get(role);
        if (strategy == null)
            throw new IllegalArgumentException("Невідома роль: " + role);
        return strategy;
    }
}