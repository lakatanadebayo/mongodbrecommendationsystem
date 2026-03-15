package com.mongodbrecommendationsystem.controller;

import com.mongodbrecommendationsystem.entity.User;
import com.mongodbrecommendationsystem.entity.UserCredentials;
import com.mongodbrecommendationsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/etudiant")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/save/one")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/save/all")
    public ResponseEntity<List<User>> saveUsers(@RequestBody List<User> users) {
        return ResponseEntity.ok(userRepository.saveAll(users));
    }

    @PutMapping("/update/{idUser}")
    public ResponseEntity<User> updateUser(@PathVariable String idUser, @RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/find/all")
    public List<User> getUserByUsername() {
        return userRepository.findAll();
    }

    @GetMapping("/find/by/{username}")
    public ResponseEntity<Optional<User>> getAllUsers(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent()
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> findByUsernameAndPassword(@RequestBody UserCredentials userCredentials){
        Optional<User> userByUsername = userRepository.findByUsername(userCredentials.getUsername());

        if(userByUsername.isPresent()){
            if(userCredentials.getPassword().equals(userByUsername.get().getPassword())) {
                return new ResponseEntity<>(userByUsername.get(), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new User(), HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>(new User(), HttpStatus.OK);
        }
    }
}
