package com.jarmangani.notes.notes_api.user;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(path="/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path="/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User exists: " + user.getEmail());
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = userRepository.save(user);
            user.setPassword("**********");
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch(DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to create user: " + user.getEmail());
        }
    }

    @GetMapping(path="")
    public ResponseEntity<List<String>> getAllUsers() {
        List<String> emails = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(u -> u.getEmail())
                .filter(s -> !s.equals("admin"))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(emails);
    }
}
