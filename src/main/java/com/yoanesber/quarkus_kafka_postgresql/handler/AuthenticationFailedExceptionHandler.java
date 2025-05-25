package com.yoanesber.quarkus_kafka_postgresql.handler;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import io.quarkus.security.AuthenticationFailedException;

import com.yoanesber.quarkus_kafka_postgresql.context.RequestContext;
import com.yoanesber.quarkus_kafka_postgresql.dto.HttpResponseDTO;
import com.yoanesber.quarkus_kafka_postgresql.entity.SecurityEventType;
import com.yoanesber.quarkus_kafka_postgresql.service.kafka.SecurityEventProducerService;

@Provider
public class AuthenticationFailedExceptionHandler implements ExceptionMapper<AuthenticationFailedException> {
    @Context
    private UriInfo uriInfo;    

    @Inject
    private RequestContext requestContext;

    @Inject
    private SecurityEventProducerService eventProducer;

    @Override
    public Response toResponse(AuthenticationFailedException exception) {
        // Get the attempted username from the request context
        String username = requestContext.getAttemptedUsername();
        if (username == null) {
            username = "Unknown user";
        }

        // Log the invalid parameters
        eventProducer.sendSecurityEvent(SecurityEventType.UNAUTHORIZED.toString(), 
            username, exception.getMessage());

        // Return a 401 Unauthorized response with a custom error message
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new HttpResponseDTO(
                        "Authentication failed",
                        exception.getMessage(),
                        uriInfo.getPath(),
                        Response.Status.UNAUTHORIZED.getStatusCode(),
                        null
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}