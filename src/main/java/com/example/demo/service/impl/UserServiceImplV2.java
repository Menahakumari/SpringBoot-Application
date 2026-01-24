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

        if (user.getName() == null || user.getName().trim().isEmpty())
            throw new RuntimeException("Name cannot be empty");

        if (user.getEmail() == null || user.getEmail().trim().isEmpty())
            throw new RuntimeException("Email cannot be empty");

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            throw new RuntimeException("Invalid email format");

        if (user.getName().length() > 20) 
            throw new RuntimeException("Name cannot exceed 20 characters");

        user.setName(user.getName().toUpperCase());
        user.setEmail(user.getEmail().toLowerCase());

        if (repo.findByEmail(user.getEmail()).isPresent())
            throw new RuntimeException("Email already exists");

        return repo.save(user);
    }

    @Override
    public void deleteAllUsers() {
        long count = repo.count();

        if (count == 0) {
            throw new RuntimeException("No users found to delete");
        }

        repo.deleteAll();
    }

    @Override
    public User getUserById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {

        User existing = getUserById(id);

        String newName = user.getName().toUpperCase();
        String newEmail = user.getEmail().toLowerCase();

        if (!existing.getEmail().equalsIgnoreCase(newEmail)) {
            if (repo.findByEmail(newEmail).isPresent())
                throw new RuntimeException("Email already exists");
            existing.setEmail(newEmail);
        }

        existing.setName(newName);
        return repo.save(existing);
    }

    @Override
    public User patchUser(Long id, User user) {

        User existing = getUserById(id);

        if (user.getName() != null)
            existing.setName(user.getName().toUpperCase());

        if (user.getEmail() != null) {
            String newEmail = user.getEmail().toLowerCase();
            if (!existing.getEmail().equalsIgnoreCase(newEmail)) {
                if (repo.findByEmail(newEmail).isPresent())
                    throw new RuntimeException("Email already exists");
                existing.setEmail(newEmail);
            }
        }
        return repo.save(existing);
    }

    @Override
    public void deleteUser(Long id) {
        if (!repo.existsById(id))
            throw new RuntimeException("User not found with id " + id);
        repo.deleteById(id);
    }
}
