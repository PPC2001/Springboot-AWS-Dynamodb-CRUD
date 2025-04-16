package com.springboot_aws_dynamodb.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.springboot_aws_dynamodb.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public UserRepository(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }
    // Save a new user or update an existing one
    public User save(User user) {
        dynamoDBMapper.save(user);
        return user;
    }

    // Get user by ID
    public Optional<User> getById(String userId) {
        User user = dynamoDBMapper.load(User.class, userId);
        return Optional.ofNullable(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return new ArrayList<>(dynamoDBMapper.scan(User.class, new DynamoDBScanExpression()));
    }

    // Delete user by ID
    public void delete(String userId) {
        User user = new User();
        user.setUserId(userId);
        dynamoDBMapper.delete(user);
    }
}