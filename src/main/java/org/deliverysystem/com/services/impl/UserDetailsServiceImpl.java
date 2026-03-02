package org.deliverysystem.com.services.impl;

import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.repositories.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;
    private final SuperAdminRepository superAdminRepository;
    private final CourierRepository courierRepository;
    private final DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return employeeRepository.findByEmail(email).map(u -> (UserDetails) u)
                .or(() -> adminRepository.findByEmail(email))
                .or(() -> superAdminRepository.findByEmail(email))
                .or(() -> courierRepository.findByEmail(email))
                .or(() -> driverRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено: " + email));
    }
}