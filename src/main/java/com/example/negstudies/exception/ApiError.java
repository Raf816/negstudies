package com.example.negstudies.exception;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiError {
    OffsetDateTime timestamp;
    int status;
    String error;
    String message;
    String path;
    List<String> details;
}