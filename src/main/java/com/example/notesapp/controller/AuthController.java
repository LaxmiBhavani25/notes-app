
package com.example.notesapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.notesapp.model.User;
import com.example.notesapp.repository.UserRepository;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        userRepo.save(user);
        return "Registered Successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

    User existing = userRepo.findByEmail(user.getEmail());

    
    if (existing == null) {
        return "User not found";
    }

    if (existing.getPassword().equals(user.getPassword())) {
        return "Login Successful";
    } else {
        return "Invalid Credentials";
    }
}
}