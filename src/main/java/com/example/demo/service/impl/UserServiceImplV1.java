package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userServiceImplV1")
public class UserServiceImplV1 implements UserService {

    private final UserRepository repo;

    public UserServiceImplV1(UserRepository repo) {
        this.repo = repo;
    }

    
    @Override
    public User createUser(User user) {

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email cannot be empty");
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new RuntimeException("Invalid email format");
        }

        repo.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already exists");
        });

        repo.findByName(user.getName()).ifPresent(u -> {
            throw new RuntimeException("Name already exists");
        });

        return repo.save(user);
    }


    @Override
    public User getUserById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));
    }


    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {

        User existing = getUserById(id);

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email cannot be empty");
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new RuntimeException("Invalid email format");
        }

        repo.findByName(user.getName()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new RuntimeException("Name already exists");
            }
        });

        repo.findByEmail(user.getEmail()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new RuntimeException("Email already exists");
            }
        });

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());

        return repo.save(existing);
    }
    @Override
    public User patchUser(Long id, User user) {

        User existing = getUserById(id);

        if (user.getName() != null) {
            if (user.getName().trim().isEmpty()) {
                throw new RuntimeException("Name cannot be empty");
            }

            repo.findByName(user.getName()).ifPresent(u -> {
                if (!u.getId().equals(id)) {
                    throw new RuntimeException("Name already exists");
                }
            });

            existing.setName(user.getName());
        }

        if (user.getEmail() != null) {
            if (user.getEmail().trim().isEmpty()) {
                throw new RuntimeException("Email cannot be empty");
            }

            if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new RuntimeException("Invalid email format");
            }

            repo.findByEmail(user.getEmail()).ifPresent(u -> {
                if (!u.getId().equals(id)) {
                    throw new RuntimeException("Email already exists");
                }
            });

            existing.setEmail(user.getEmail());
        }

        return repo.save(existing);
    }

    @Override
    public void deleteUser(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        repo.deleteById(id);
    }
}
