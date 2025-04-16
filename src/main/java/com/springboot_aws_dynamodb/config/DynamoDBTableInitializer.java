package com.springboot_aws_dynamodb.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.springboot_aws_dynamodb.model.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("local")
@Slf4j
public class DynamoDBTableInitializer {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;  // Injecting AmazonDynamoDB client

    @PostConstruct
    public void createTable() {
        try {
            // Create the DynamoDB document client
            DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

            // Create the CreateTableRequest from the User class
            CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(User.class);

            // Set provisioned throughput (optional, adjust as needed)
            createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

            // Create the table using the AmazonDynamoDB client
            Table table = dynamoDB.createTable(createTableRequest);

            // Wait for the table to be created
            table.waitForActive();

            log.info("✅ DynamoDB Local Table created successfully: {}", table.getTableName());
        } catch (ResourceInUseException e) {
            log.warn("⚠️ Table already exists: {}", e.getMessage());
        } catch (Exception e) {
            log.error("❌ Error creating table: {}", e.getMessage(), e);
        }
    }
}
