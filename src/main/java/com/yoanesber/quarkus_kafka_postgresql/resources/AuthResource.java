package com.yoanesber.quarkus_kafka_postgresql.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import com.yoanesber.quarkus_kafka_postgresql.context.RequestContext;
import com.yoanesber.quarkus_kafka_postgresql.dto.AuthLoginDTO;
import com.yoanesber.quarkus_kafka_postgresql.dto.HttpResponseDTO;
import com.yoanesber.quarkus_kafka_postgresql.entity.User;
import com.yoanesber.quarkus_kafka_postgresql.entity.Role;
import com.yoanesber.quarkus_kafka_postgresql.entity.SecurityEventType;
import com.yoanesber.quarkus_kafka_postgresql.repository.UserRepository;
import com.yoanesber.quarkus_kafka_postgresql.service.PasswordService;
import com.yoanesber.quarkus_kafka_postgresql.service.TokenService;
import com.yoanesber.quarkus_kafka_postgresql.service.kafka.SecurityEventProducerService;


@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    @Context
    private UriInfo uriInfo;

    @Inject
    private RequestContext requestContext;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordService passwordService;

    @Inject
    private TokenService tokenService;

    @Inject
    private SecurityEventProducerService eventProducer;

    @POST
    @Path("/login")
    public Response login(AuthLoginDTO authLoginDTO) {
        try {
            // Validate the input
            if (authLoginDTO == null) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.BAD_REQUEST.toString(), 
                    "Unknown user",
                    "Invalid request: authLoginDTO is null");

                // Return a 400 Bad Request response with a custom error message
                return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                    "Invalid request", 
                    "Bad Request", 
                    uriInfo.getPath(), 
                    Response.Status.BAD_REQUEST.getStatusCode(), 
                    null
                )).build();
            }

            // Check if username and password are not empty
            if (authLoginDTO.getUserName() == null || authLoginDTO.getPassword() == null ||
                authLoginDTO.getUserName().isEmpty() || authLoginDTO.getPassword().isEmpty()) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.BAD_REQUEST.toString(), 
                    (authLoginDTO.getUserName() == null ? "Unknown user" : authLoginDTO.getUserName()),
                    "Invalid request: username or password is empty");

                // Return a 400 Bad Request response with a custom error message
                return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                    "Invalid request",
                    "Bad Request", 
                    uriInfo.getPath(), 
                    Response.Status.BAD_REQUEST.getStatusCode(), 
                    null
                )).build();
            }

            // Get the user from the database
            Optional<User> LoggedInUser = userRepository.findByUserName(authLoginDTO.getUserName());

            // Store the attempted username in the request context
            // This is useful for logging or tracking purposes
            requestContext.setAttemptedUsername(authLoginDTO.getUserName());

            // Check if the user exists
            if (LoggedInUser == null || LoggedInUser.isEmpty()) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.UNAUTHORIZED.toString(), 
                    authLoginDTO.getUserName(),
                    "Invalid username or password");

                // Return a 401 Unauthorized response with a custom error message
                return Response.status(Response.Status.UNAUTHORIZED).entity(new HttpResponseDTO(
                    "Invalid username or password", 
                    "Unauthorized", 
                    uriInfo.getPath(), 
                    Response.Status.UNAUTHORIZED.getStatusCode(), 
                    null
                )).build();
            }

            // Check if the user is enabled
            if (!LoggedInUser.get().isEnabled()) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.UNAUTHORIZED.toString(), 
                    authLoginDTO.getUserName(),
                    "User has been disabled");

                // Return a 401 Unauthorized response with a custom error message
                return Response.status(Response.Status.UNAUTHORIZED).entity(new HttpResponseDTO(
                    "User has been disabled", 
                    "Unauthorized", 
                    uriInfo.getPath(), 
                    Response.Status.UNAUTHORIZED.getStatusCode(), 
                    null
                )).build();
            }

            // Check if the user account is expired
            if (!LoggedInUser.get().isAccountNonExpired()) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.UNAUTHORIZED.toString(), 
                    authLoginDTO.getUserName(),
                    "User account has been expired");

                // Return a 401 Unauthorized response with a custom error message
                return Response.status(Response.Status.UNAUTHORIZED).entity(new HttpResponseDTO(
                    "User account has been expired", 
                    "Unauthorized", 
                    uriInfo.getPath(), 
                    Response.Status.UNAUTHORIZED.getStatusCode(), 
                    null
                )).build();
            }

            // Check if the user account is locked
            if (!LoggedInUser.get().isAccountNonLocked()) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.UNAUTHORIZED.toString(), 
                    authLoginDTO.getUserName(),
                    "User account has been locked");

                // Return a 401 Unauthorized response with a custom error message
                return Response.status(Response.Status.UNAUTHORIZED).entity(new HttpResponseDTO(
                    "User account has been locked", 
                    "Unauthorized", 
                    uriInfo.getPath(), 
                    Response.Status.UNAUTHORIZED.getStatusCode(), 
                    null
                )).build();
            }

            // Check if the user credentials are expired
            if (!LoggedInUser.get().isCredentialsNonExpired()) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.UNAUTHORIZED.toString(), 
                    authLoginDTO.getUserName(),
                    "User credentials have been expired");

                // Return a 401 Unauthorized response with a custom error message
                return Response.status(Response.Status.UNAUTHORIZED).entity(new HttpResponseDTO(
                    "User credentials have been expired", 
                    "Unauthorized", 
                    uriInfo.getPath(), 
                    Response.Status.UNAUTHORIZED.getStatusCode(), 
                    null
                )).build();
            }

            // Check if the user is deleted
            if (LoggedInUser.get().isDeleted()) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.UNAUTHORIZED.toString(), 
                    authLoginDTO.getUserName(),
                    "User has been deleted");

                // Return a 401 Unauthorized response with a custom error message
                return Response.status(Response.Status.UNAUTHORIZED).entity(new HttpResponseDTO(
                    "User has been deleted", 
                    "Unauthorized", 
                    uriInfo.getPath(), 
                    Response.Status.UNAUTHORIZED.getStatusCode(), 
                    null
                )).build();
            }

            // Check if the user account expiration date is reached
            if (LoggedInUser.get().getAccountExpirationDate() != null && 
                LoggedInUser.get().getAccountExpirationDate().isBefore(Instant.now())) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.UNAUTHORIZED.toString(), 
                    authLoginDTO.getUserName(),
                    "User account has been expired");

                // Return a 401 Unauthorized response with a custom error message
                return Response.status(Response.Status.UNAUTHORIZED).entity(new HttpResponseDTO(
                    "User account has been expired", 
                    "Unauthorized", 
                    uriInfo.getPath(), 
                    Response.Status.UNAUTHORIZED.getStatusCode(), 
                    null
                )).build();
            }

            // Check if the user credentials expiration date is reached
            if (LoggedInUser.get().getCredentialsExpirationDate() != null && 
                LoggedInUser.get().getCredentialsExpirationDate().isBefore(Instant.now())) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.UNAUTHORIZED.toString(), 
                    authLoginDTO.getUserName(),
                    "User credentials have been expired");

                // Return a 401 Unauthorized response with a custom error message
                return Response.status(Response.Status.UNAUTHORIZED).entity(new HttpResponseDTO(
                    "User credentials have been expired", 
                    "Unauthorized", 
                    uriInfo.getPath(), 
                    Response.Status.UNAUTHORIZED.getStatusCode(), 
                    null
                )).build();
            }

            // Validate the username and password
            if (LoggedInUser.get().getUserName().equals(authLoginDTO.getUserName()) && 
                passwordService.verifyPassword(authLoginDTO.getPassword(), LoggedInUser.get().getPassword())) {
                
                // Map the user roles to a set of strings
                Set<String> roles = Set.of(LoggedInUser.get().getRoles().stream()
                    .map(Role::getName)
                    .toArray(String[]::new));

                // Generate a JWT token
                String token = tokenService.generateToken(LoggedInUser.get().getUserName(), roles);

                // Update the last login time
                userRepository.updateLastLogin(LoggedInUser.get().getId());

                return Response.ok().entity(new HttpResponseDTO(
                    "Login successful", 
                    null, 
                    uriInfo.getPath(), 
                    Response.Status.OK.getStatusCode(), 
                    token
                )).build();
            } else {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.UNAUTHORIZED.toString(), 
                    authLoginDTO.getUserName(),
                    "Invalid username or password");

                // Return a 401 Unauthorized response with a custom error message
                return Response.status(Response.Status.UNAUTHORIZED).entity(new HttpResponseDTO(
                    "Invalid username or password", 
                    "Unauthorized", 
                    uriInfo.getPath(), 
                    Response.Status.UNAUTHORIZED.getStatusCode(), 
                    null
                )).build();
            }
        } catch (Exception e) {
            // Log the exception
            eventProducer.sendSecurityEvent(SecurityEventType.INTERNAL_SERVER_ERROR.toString(), 
                (authLoginDTO != null ? authLoginDTO.getUserName() : "Unknown user"),
                "Failed to login: " + e.getMessage());

            // Return a 500 Internal Server Error response with a custom error message
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new HttpResponseDTO(
                "Failed to login",
                e.getMessage(),
                uriInfo.getPath(), 
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 
                null
            )).build();
        }
    }
}
