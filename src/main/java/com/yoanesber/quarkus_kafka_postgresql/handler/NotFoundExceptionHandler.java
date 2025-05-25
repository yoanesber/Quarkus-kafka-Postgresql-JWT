package com.yoanesber.quarkus_kafka_postgresql.handler;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.NotFoundException;

import com.yoanesber.quarkus_kafka_postgresql.context.RequestContext;
import com.yoanesber.quarkus_kafka_postgresql.dto.HttpResponseDTO;
import com.yoanesber.quarkus_kafka_postgresql.entity.SecurityEventType;
import com.yoanesber.quarkus_kafka_postgresql.service.kafka.SecurityEventProducerService;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {
    @Context
    private UriInfo uriInfo;

    @Inject
    private RequestContext requestContext;

    @Inject
    private SecurityEventProducerService eventProducer;

    @Override
    public Response toResponse(NotFoundException exception) {
        // Get the attempted username from the request context
        String username = requestContext.getAttemptedUsername();
        if (username == null) {
            username = "Unknown user";
        }

        // Log the invalid parameters
        eventProducer.sendSecurityEvent(SecurityEventType.NOT_FOUND.toString(), 
            username, exception.getMessage());

        // Return a 404 Not Found response with a custom error message
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new HttpResponseDTO(
                        "Not Found",
                        exception.getMessage(),
                        uriInfo.getPath(),
                        Response.Status.NOT_FOUND.getStatusCode(),
                        null
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}