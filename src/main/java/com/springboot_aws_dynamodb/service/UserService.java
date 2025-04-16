package com.springboot_aws_dynamodb.service;

import com.springboot_aws_dynamodb.model.User;
import com.springboot_aws_dynamodb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public Optional<User> get(String userId) {
        return userRepo.getById(userId);
    }

    public void delete(String userId) {
        userRepo.delete(userId);
    }

    public List<User> getAllUsers() {
        return  userRepo.getAllUsers();
    }
}