package com.jarmangani.notes.notes_api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping(path="/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            user = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch(DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        }
    }
}
