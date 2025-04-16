package com.springboot_aws_dynamodb.model.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    private String message;
    private String developerMessage;
    private int httpStatus;
}
