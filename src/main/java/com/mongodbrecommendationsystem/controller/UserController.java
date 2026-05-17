package com.mongodbrecommendationsystem.controller;

import com.mongodbrecommendationsystem.entity.LearningPath;
import com.mongodbrecommendationsystem.entity.User;
import com.mongodbrecommendationsystem.entity.UserCredentials;
import com.mongodbrecommendationsystem.entity.UserDto;
import com.mongodbrecommendationsystem.repository.LearningPathRepository;
import com.mongodbrecommendationsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/etudiant")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final LearningPathRepository learningPathRepository;

    @PostMapping("/save/one")
    public ResponseEntity<User> saveUser(@RequestBody User user) {

        List<String> tags = user.getCompletedCoursesIds().stream()
                .map(learningPathRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(lp -> lp.getTags().stream())
                .distinct()
                .toList();

        user.setCompletedCoursesTags(tags);

        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/save/all")
    public ResponseEntity<List<User>> saveUsers(@RequestBody List<User> users) {
        return ResponseEntity.ok(userRepository.saveAll(users));
    }

    @PutMapping("/update/{idUser}")
    public ResponseEntity<User> updateUser(@PathVariable String idUser, @RequestBody User user) {

        Optional<User> existingUser = userRepository.findById(idUser);

        // Récupération des anciens cours et tags
        Set<String> completedCoursesIds = new HashSet<>();
        Set<String> completedCoursesTags = new HashSet<>();

        if (existingUser.isPresent()) {
            completedCoursesIds.addAll(existingUser.get().getCompletedCoursesIds());
            completedCoursesTags.addAll(existingUser.get().getCompletedCoursesTags());
        }

        // Ajout des nouveaux cours
        completedCoursesIds.addAll(user.getCompletedCoursesIds());

        // Génération des tags associés aux cours
        for (String courseId : completedCoursesIds) {
            learningPathRepository.findById(courseId)
                    .ifPresent(learningPath ->
                            completedCoursesTags.addAll(learningPath.getTags()));
        }

        user.setCompletedCoursesIds(new ArrayList<>(completedCoursesIds));
        user.setCompletedCoursesTags(new ArrayList<>(completedCoursesTags));

        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/find/all")
    public List<UserDto> getAllUsers() {

        return userRepository.findAll().stream().map(u -> {

            List<LearningPath> learningPaths = u.getCompletedCoursesIds().stream()
                    .map(learningPathRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            UserDto userDto = new UserDto();
            userDto.setId(u.getId());
            userDto.setUsername(u.getUsername());
            userDto.setPassword(u.getPassword());
            userDto.setLevel(u.getLevel());
            userDto.setDomain(u.getDomain());
            userDto.setLanguage(u.getLanguage());
            userDto.setCompletedLearningPath(learningPaths);

            return userDto;

        }).toList();
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
    public ResponseEntity<?> findByUsernameAndPassword(@RequestBody UserCredentials userCredentials) {
        Optional<User> userByUsername = userRepository.findByUsername(userCredentials.getUsername());

        if (userByUsername.isPresent()) {
            if (userCredentials.getPassword().equals(userByUsername.get().getPassword())) {
                return new ResponseEntity<>(userByUsername.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new User(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new User(), HttpStatus.OK);
        }
    }
}
