package com.department.Controller;

import com.department.Entity.Employee;
import com.department.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public Employee register(@RequestBody Employee user){

        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Employee user){
        return userService.verify(user);
    }

}
