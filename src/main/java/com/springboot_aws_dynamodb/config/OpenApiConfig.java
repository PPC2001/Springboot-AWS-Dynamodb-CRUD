package com.springboot_aws_dynamodb.config;

import com.springboot_aws_dynamodb.model.error.ErrorMessage;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String path;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private Content getContentDefault(ErrorMessage errorMessage) {
        return new Content()
                .addMediaType(
                        "application/json",
                        new MediaType().addExamples("Default", new Example().value(errorMessage)));
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("User Management API")
                .version("1.0")
                .description("API Documentation for managing users");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("api-key");

        Components components = new Components()
                .addSecuritySchemes(
                        "api-key",
                        new SecurityScheme()
                                .name("api-key")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("api-key"))
                .addResponses(
                        "BadRequestResponse",
                        new ApiResponse()
                                .description("Bad Request")
                                .content(getContentDefault(
                                        ErrorMessage.builder()
                                                .developerMessage("Errors:{ ... }")
                                                .message("The Request body does not have the correct structure")
                                                .httpStatus(400)
                                                .build())))
                .addResponses(
                        "NotFoundResponse",
                        new ApiResponse()
                                .description("Not Found")
                                .content(getContentDefault(
                                        ErrorMessage.builder()
                                                .developerMessage("404 NOT_FOUND \"Entity was not found\"")
                                                .message("Entity was not found")
                                                .httpStatus(404)
                                                .build())))
                .addResponses(
                        "UnauthorizedResponse",
                        new ApiResponse()
                                .description("Unauthorized")
                                .content(getContentDefault(
                                        ErrorMessage.builder()
                                                .developerMessage("The Api Key is incorrect")
                                                .message("Not authorized")
                                                .httpStatus(401)
                                                .build())));

        // Dynamically configure servers based on the profile
        String localUrl = String.format("http://localhost:%s%s", port, path);
        String devUrl = String.format("https://dev-internal-api.example.com%s", path);
        String uatUrl = String.format("https://uat-internal-api.example.com%s", path);
        String prodUrl = String.format("https://prod-internal-api.example.com%s", path);

        List<Server> servers = List.of(
                new Server().url(localUrl).description("Local Environment"),
                new Server().url(devUrl).description("Development Environment"),
                new Server().url(uatUrl).description("UAT Environment"),
                new Server().url(prodUrl).description("Production Environment")
        );

        // Add only relevant server URL based on active profile
        if ("local".equals(activeProfile)) {
            servers = List.of(new Server().url(localUrl).description("Local Environment"));
        } else if ("dev".equals(activeProfile)) {
            servers = List.of(new Server().url(devUrl).description("Development Environment"));
        } else if ("prod".equals(activeProfile)) {
            servers = List.of(new Server().url(prodUrl).description("Production Environment"));
        }

        return new OpenAPI()
                .info(info)
                .servers(servers)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

}
