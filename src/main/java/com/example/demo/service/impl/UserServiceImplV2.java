package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userServiceV2")
public class UserServiceImplV2 implements UserService {

    private final UserRepository repo;

    public UserServiceImplV2(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public User createUser(User user) {
        
        user.setName(user.getName().toUpperCase());
        user.setEmail(user.getEmail().toLowerCase());

        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        return repo.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return repo.findById(id).orElseThrow(
            () -> new RuntimeException("User not found")
        );
    }

    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        User existing = getUserById(id);

        existing.setName(user.getName().toUpperCase());

        if (!existing.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (repo.findByEmail(user.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }
            existing.setEmail(user.getEmail().toLowerCase());
        }

        return repo.save(existing);
    }

@Override
public User patchUser(Long id, User user) {

    User existing = getUserById(id);

    if (user.getName() != null) {
        existing.setName(user.getName().toUpperCase());
    }

    if (user.getEmail() != null) {
        String newEmail = user.getEmail().toLowerCase();

        if (!existing.getEmail().equalsIgnoreCase(newEmail)) {
            if (repo.findByEmail(newEmail).isPresent()) {
                throw new RuntimeException("Email already exists");
            }
            existing.setEmail(newEmail);
        }
    }
    return repo.save(existing);
}


    @Override
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }
}
