package com.department.Service;

import com.department.Entity.Employee;
import com.department.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepo userRepo;
@Autowired
    private AuthenticationManager authmanager;

@Autowired
private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    // Inside UserService.java
    @Autowired
    private UserRepo usersRepository;

    // Master Login Method
    public String verify(Employee user) {
        Authentication authentication = authmanager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            // ALWAYS fetch the real user from DB to get the true role
            Employee realUser = usersRepository.findByEmail(user.getEmail());

            // Pass both username and role to the JWT Service
            return jwtService.generateToken(realUser.getEmail(), realUser.getRole());
        }
        return "Fail";
    }

    // Master Register Method
    public Employee register(Employee user) {
        user.setPassword(encoder.encode(user.getPassword()));

        // ALWAYS set a fallback default role if the frontend doesn't send one
        if (user.getRole() == null) {
            user.setRole(Employee.Role.EMPLOYEE);
        }
        return usersRepository.save(user);
    }
}
