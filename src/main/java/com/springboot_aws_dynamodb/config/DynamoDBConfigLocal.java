package com.springboot_aws_dynamodb.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local") // This bean loads only when 'local' profile is active
public class DynamoDBConfigLocal {

    private static final String AWS_DDB_ENDPOINT = "http://localhost:8000/";
    private static final String AWS_ACCESS_KEY_ID = "DUMMYIDEXAMPLE";
    private static final String AWS_SECRET_ACCESS_KEY = "DUMMYEXAMPLEKEY";
    private static final String AWS_DB_REGION = "us-east-1";

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDBClient());
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDBClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(AWS_DDB_ENDPOINT, AWS_DB_REGION)
                )
                .withCredentials(awsCredentialsProvider())
                .build();
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)
        );
    }
}