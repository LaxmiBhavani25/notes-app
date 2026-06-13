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
public ResponseEntity<?> login(@RequestBody User loginUser) {

    try {
        User user = userRepository.findByEmail(loginUser.getEmail());

        if (user == null) {
            return ResponseEntity.status(401).body("Invalid login");
        }

        if (!user.getPassword().equals(loginUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid login");
        }

        return ResponseEntity.ok(user);

    } catch (Exception e) {
        return ResponseEntity.status(401).body("Invalid login");
    }
}
}
