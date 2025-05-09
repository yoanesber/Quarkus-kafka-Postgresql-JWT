package com.yoanesber.quarkus_kafka_postgresql.resources;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.yoanesber.quarkus_kafka_postgresql.dto.HttpResponseDTO;
import com.yoanesber.quarkus_kafka_postgresql.dto.DepartmentDTO;
import com.yoanesber.quarkus_kafka_postgresql.entity.Department;
import com.yoanesber.quarkus_kafka_postgresql.entity.SecurityEventType;
import com.yoanesber.quarkus_kafka_postgresql.exception.DepartmentNotFoundException;
import com.yoanesber.quarkus_kafka_postgresql.exception.DepartmentAlreadyExistsException;
import com.yoanesber.quarkus_kafka_postgresql.mapping.DepartmentMapper;
import com.yoanesber.quarkus_kafka_postgresql.repository.DepartmentRepository;
import com.yoanesber.quarkus_kafka_postgresql.service.DepartmentService;
import com.yoanesber.quarkus_kafka_postgresql.service.kafka.SecurityEventProducerService;

@Path("/api/v1/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentResource {

    private static final Integer PAGE_NUMBER_DEFAULT = 0;
    private static final Integer PAGE_SIZE_DEFAULT = 10;

    @Context
    private UriInfo uriInfo;

    @Inject
    private DepartmentMapper departmentMapper;

    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private DepartmentService departmentService;

    @Inject
    private SecurityEventProducerService eventProducer;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER" })
    public Response findAll(@QueryParam("page") Optional<Integer> page,
                            @QueryParam("size") Optional<Integer> size, Request request) {
        
        // Default values for page and size
        int pageNumber = page.orElse(PAGE_NUMBER_DEFAULT);
        int pageSize = size.orElse(PAGE_SIZE_DEFAULT);

        // Validate page and size parameters
        if (pageNumber < 0 || pageSize <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                "Invalid pagination parameters",
                null,
                uriInfo.getPath(),
                Response.Status.BAD_REQUEST.getStatusCode(),
                null
            )).build();
        }

        try {
            // Get all departments sorted by ID in ascending order
            // and map them to DepartmentDTO
            List<DepartmentDTO> departments = departmentRepository.findAll(Sort.ascending("id"))
                .page(Page.of(pageNumber, pageSize)).list()
                .stream()
                .map(d -> departmentMapper.toDTO(d))
                .toList();

            // Check if the list is empty
            if (departments.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity(new HttpResponseDTO(
                    "No departments found",
                    null,
                    uriInfo.getPath(),
                    Response.Status.NOT_FOUND.getStatusCode(),
                    null
                )).build();
            }

            return Response.status(Response.Status.OK).entity(new HttpResponseDTO(
                "All departments retrieved successfully",
                null,
                uriInfo.getPath(),
                Response.Status.OK.getStatusCode(),
                departments
            )).build();
        } catch (Exception e) {
            // Log the error
            eventProducer.sendSecurityEvent(SecurityEventType.INTERNAL_SERVER_ERROR.toString(), 
                jwt.getName(),
                e.getMessage());

            // Return an internal server error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new HttpResponseDTO(
                "Error retrieving departments",
                e.getMessage(),
                uriInfo.getPath(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                null
            )).build();
        }
    }

    @GET
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER" })
    @Path("/{id}")
    public Response findById(@PathParam("id") String id) {
        try {
            // Get the department by ID
            Optional<DepartmentDTO> department = departmentRepository.findById(id)
                .map(d -> departmentMapper.toDTO(d));

            // Check if the department exists
            if (department == null || department.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity(new HttpResponseDTO(
                    "Department with id " + id + " not found",
                    null,
                    uriInfo.getPath(),
                    Response.Status.NOT_FOUND.getStatusCode(),
                    null
                )).build();
            }

            return Response.status(Response.Status.OK).entity(new HttpResponseDTO(
                "Department retrieved successfully",
                null,
                uriInfo.getPath(),
                Response.Status.OK.getStatusCode(),
                department
            )).build();
        } catch (Exception e) {
            // Log the error
            eventProducer.sendSecurityEvent(SecurityEventType.INTERNAL_SERVER_ERROR.toString(), 
                jwt.getName(),
                e.getMessage());

            // Return an internal server error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new HttpResponseDTO(
                "Error retrieving department",
                e.getMessage(),
                uriInfo.getPath(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                null
            )).build();
        }
    }

    @GET
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER" })
    @Path("/dept-name")
    public Response findByDeptName(@QueryParam("deptName") String deptName) {
        try {
            // Get the department by name
            Optional<DepartmentDTO> department = departmentRepository.findByDeptName(deptName)
                .map(d -> departmentMapper.toDTO(d));

            // Check if the department exists
            if (department == null || department.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity(new HttpResponseDTO(
                    "Department with name " + deptName + " not found",
                    null,
                    uriInfo.getPath(),
                    Response.Status.NOT_FOUND.getStatusCode(),
                    null
                )).build();
            }

            return Response.status(Response.Status.OK).entity(new HttpResponseDTO(
                "Department retrieved successfully",
                null,
                uriInfo.getPath(),
                Response.Status.OK.getStatusCode(),
                department
            )).build();
        } catch (Exception e) {
            // Log the error
            eventProducer.sendSecurityEvent(SecurityEventType.INTERNAL_SERVER_ERROR.toString(), 
                jwt.getName(),
                e.getMessage());

            // Return an internal server error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new HttpResponseDTO(
                "Error retrieving department",
                e.getMessage(),
                uriInfo.getPath(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                null
            )).build();
        }
    }

    @GET
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER" })
    @Path("/exists-by-id/")
    public Response existsById(@QueryParam("id") String id) {
        try {
            // Check the existence of the department by name
            boolean exists = departmentRepository.existsById(id);

            return Response.status(Response.Status.OK).entity(new HttpResponseDTO(
                "Department existence check completed",
                null,
                uriInfo.getPath(),
                Response.Status.OK.getStatusCode(),
                exists
            )).build();
        } catch (Exception e) {
            // Log the error
            eventProducer.sendSecurityEvent(SecurityEventType.INTERNAL_SERVER_ERROR.toString(), 
                jwt.getName(),
                e.getMessage());

            // Return an internal server error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new HttpResponseDTO(
                "Error checking department existence",
                e.getMessage(),
                uriInfo.getPath(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                null
            )).build();
        }
    }

    @GET
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER" })
    @Path("/exists-by-name/")
    public Response existsByDeptName(@QueryParam("deptName") String deptName) {
        try {
            // Check the existence of the department by name
            boolean exists = departmentRepository.existsByDeptName(deptName);

            return Response.status(Response.Status.OK).entity(new HttpResponseDTO(
                "Department existence check completed",
                null,
                uriInfo.getPath(),
                Response.Status.OK.getStatusCode(),
                exists
            )).build();
        } catch (Exception e) {
            // Log the error
            eventProducer.sendSecurityEvent(SecurityEventType.INTERNAL_SERVER_ERROR.toString(), 
                jwt.getName(),
                e.getMessage());

            // Return an internal server error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new HttpResponseDTO(
                "Error checking department existence",
                e.getMessage(),
                uriInfo.getPath(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                null
            )).build();
        }
    }

    @POST
    @RolesAllowed({ "ROLE_ADMIN" })
    public Response createDepartment(DepartmentDTO departmentDTO) {
        try {
            // Validate the input departmentDTO
            if (departmentDTO == null) {
                // Log the invalid parameters
                eventProducer.sendSecurityEvent(SecurityEventType.BAD_REQUEST.toString(), 
                    jwt.getName(),
                    "Department cannot be null");

                return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                    "Department cannot be null",
                    null,
                    uriInfo.getPath(),
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    null
                )).build();
            }

            // Persist the department using the service
            Department createdDepartment = departmentService.createDepartment(departmentMapper.toEntity(departmentDTO));

            // Return the created department as a response
            return Response.status(Response.Status.CREATED).entity(new HttpResponseDTO(
                "Department created successfully",
                null,
                uriInfo.getPath(),
                Response.Status.CREATED.getStatusCode(),
                departmentMapper.toDTO(createdDepartment)
            )).build();
        } catch (DepartmentAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity(new HttpResponseDTO(
                e.getMessage(),
                null,
                uriInfo.getPath(),
                Response.Status.CONFLICT.getStatusCode(),
                null
            )).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                e.getMessage(),
                null,
                uriInfo.getPath(),
                Response.Status.BAD_REQUEST.getStatusCode(),
                null
            )).build();
        } catch (Exception e) {
            // Log the error
            eventProducer.sendSecurityEvent(SecurityEventType.INTERNAL_SERVER_ERROR.toString(), 
                jwt.getName(),
                e.getMessage());

            // Return an internal server error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new HttpResponseDTO(
                "Error creating department",
                e.getMessage(),
                uriInfo.getPath(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                null
            )).build();
        }
    }

    @PUT
    @RolesAllowed({ "ROLE_ADMIN" })
    @Path("/{id}")
    public Response updateDepartment(@PathParam("id") String id, DepartmentDTO departmentDTO) {
        try {
            // Validate the input parameters
            if (id == null || id.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                    "Department ID cannot be null or empty",
                    null,
                    uriInfo.getPath(),
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    null
                )).build();
            }

            if (departmentDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                    "Department cannot be null",
                    null,
                    uriInfo.getPath(),
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    null
                )).build();
            }

            // Update the department using the service
            Department updatedDepartment = departmentService.updateDepartment(id, departmentMapper.toEntity(departmentDTO));

            // Return the updated department as a response
            return Response.status(Response.Status.OK).entity(new HttpResponseDTO(
                "Department updated successfully",
                null,
                uriInfo.getPath(),
                Response.Status.OK.getStatusCode(),
                departmentMapper.toDTO(updatedDepartment)
            )).build();
        } catch (DepartmentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new HttpResponseDTO(
                e.getMessage(),
                null,
                uriInfo.getPath(),
                Response.Status.NOT_FOUND.getStatusCode(),
                null
            )).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                e.getMessage(),
                null,
                uriInfo.getPath(),
                Response.Status.BAD_REQUEST.getStatusCode(),
                null
            )).build();
        } catch (Exception e) {
            // Log the error
            eventProducer.sendSecurityEvent(SecurityEventType.INTERNAL_SERVER_ERROR.toString(), 
                jwt.getName(),
                e.getMessage());

            // Return an internal server error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new HttpResponseDTO(
                "Error updating department",
                e.getMessage(),
                uriInfo.getPath(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                null
            )).build();
        } 
    }

    @PATCH
    @RolesAllowed({ "ROLE_ADMIN" })
    @Path("/inactivate/{id}")
    public Response inactivateDepartment(@PathParam("id") String id) {
        try {
            // Validate the input parameter
            if (id == null || id.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                    "Department ID cannot be null or empty",
                    null,
                    uriInfo.getPath(),
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    null
                )).build();
            }

            // Inactivate the department using the service
            boolean inactivated = departmentService.inactivateDepartment(id);

            // Return the result as a response
            return Response.status(Response.Status.OK).entity(new HttpResponseDTO(
                "Department inactivated successfully",
                null,
                uriInfo.getPath(),
                Response.Status.OK.getStatusCode(),
                inactivated
            )).build();
        } catch (DepartmentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new HttpResponseDTO(
                e.getMessage(),
                null,
                uriInfo.getPath(),
                Response.Status.NOT_FOUND.getStatusCode(),
                null
            )).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                e.getMessage(),
                null,
                uriInfo.getPath(),
                Response.Status.BAD_REQUEST.getStatusCode(),
                null
            )).build();
        } catch (Exception e) {
            // Log the error
            eventProducer.sendSecurityEvent(SecurityEventType.INTERNAL_SERVER_ERROR.toString(), 
                jwt.getName(),
                e.getMessage());

            // Return an internal server error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new HttpResponseDTO(
                "Error inactivating department",
                e.getMessage(),
                uriInfo.getPath(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                null
            )).build();
        }
    }

    @PATCH
    @RolesAllowed({ "ROLE_ADMIN" })
    @Path("/activate/{id}")
    public Response activateDepartment(@PathParam("id") String id) {
        try {
            // Validate the input parameter
            if (id == null || id.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                    "Department ID cannot be null or empty",
                    null,
                    uriInfo.getPath(),
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    null
                )).build();
            }

            // Activate the department using the service
            boolean activated = departmentService.activateDepartment(id);

            // Return the result as a response
            return Response.status(Response.Status.OK).entity(new HttpResponseDTO(
                "Department activated successfully",
                null,
                uriInfo.getPath(),
                Response.Status.OK.getStatusCode(),
                activated
            )).build();
        } catch (DepartmentNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new HttpResponseDTO(
                e.getMessage(),
                null,
                uriInfo.getPath(),
                Response.Status.NOT_FOUND.getStatusCode(),
                null
            )).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new HttpResponseDTO(
                e.getMessage(),
                null,
                uriInfo.getPath(),
                Response.Status.BAD_REQUEST.getStatusCode(),
                null
            )).build();
        } catch (Exception e) {
            // Log the error
            eventProducer.sendSecurityEvent(SecurityEventType.INTERNAL_SERVER_ERROR.toString(), 
                jwt.getName(),
                e.getMessage());

            // Return an internal server error response
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new HttpResponseDTO(
                "Error activating department",
                e.getMessage(),
                uriInfo.getPath(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                null
            )).build();
        }
    }
}
