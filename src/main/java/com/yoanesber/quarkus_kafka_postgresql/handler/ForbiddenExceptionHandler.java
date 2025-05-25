package com.yoanesber.quarkus_kafka_postgresql.handler;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import io.quarkus.security.ForbiddenException;

import com.yoanesber.quarkus_kafka_postgresql.context.RequestContext;
import com.yoanesber.quarkus_kafka_postgresql.dto.HttpResponseDTO;
import com.yoanesber.quarkus_kafka_postgresql.entity.SecurityEventType;
import com.yoanesber.quarkus_kafka_postgresql.service.kafka.SecurityEventProducerService;

@Provider
public class ForbiddenExceptionHandler implements ExceptionMapper<ForbiddenException> {
    @Context
    private UriInfo uriInfo;

    @Inject
    private RequestContext requestContext;

    @Inject
    private SecurityEventProducerService eventProducer;

    @Override
    public Response toResponse(ForbiddenException exception) {
        // Get the attempted username from the request context
        String username = requestContext.getAttemptedUsername();
        if (username == null) {
            username = "Unknown user";
        }

        // Log the invalid parameters
        eventProducer.sendSecurityEvent(SecurityEventType.FORBIDDEN.toString(), 
            username, exception.getMessage());

        // Return a 403 Forbidden response with a custom error message
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new HttpResponseDTO(
                        "Forbidden",
                        exception.getMessage(),
                        uriInfo.getPath(),
                        Response.Status.FORBIDDEN.getStatusCode(),
                        null
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}