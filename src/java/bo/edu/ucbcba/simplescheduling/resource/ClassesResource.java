/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucbcba.simplescheduling.resource;

import bo.edu.ucbcba.simplescheduling.model.MyClass;
import bo.edu.ucbcba.simplescheduling.model.Student;
import bo.edu.ucbcba.simplescheduling.response.ErrorResponse;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jorge
 */
@Path("v1/classes")
public class ClassesResource {

    @Context
    private UriInfo context;
    private final Gson gson = new Gson();

    public ClassesResource() {
    }
    
    @GET
    @Path("{classCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyClass(@PathParam("classCode") String classCode) {
        // search class
        MyClass myclass = GenericResource.getMyClass(classCode);
        if (myclass != null) {
            return Response.ok(gson.toJson(myclass), MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyClassList() {
        return Response.ok(gson.toJson(GenericResource.getMyClassList()), 
                            MediaType.APPLICATION_JSON).build();
    }
    
    /**
     * Creates a new student.
     * @param jsonString String
     * @return String
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response myClasses(String jsonString) {
        System.out.println(jsonString);
        MyClass myclass = gson.fromJson(jsonString, MyClass.class);
        
        if (GenericResource.getMyClass(myclass.getCode()) == null) {
            String classCode = myclass.getCode();
            String title = myclass.getTitle();
            String description = myclass.getDescription();
            List<Integer> studentIds = myclass.getStudentIds();

            System.out.println("classCode=" + classCode + "title=" + title +
                    "description=" + description + "studentIds=" + studentIds);
            
            myclass.setStudentIds(studentIds);
            
            // create class
            GenericResource.putMyClass(myclass);
            
            List<Integer> invalidIds = GenericResource.getMyClassMap().get(classCode).setStudentIds(studentIds);
            
            if (!invalidIds.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(UUID.randomUUID(),
                              Response.Status.BAD_REQUEST, "ERR_002", "Creation with errors",
                              "Invalid ids: " + invalidIds.toString(), Arrays.asList("Algunos estudiantes no existen"));
                return Response.ok(gson.toJson(errorResponse), MediaType.APPLICATION_JSON)
                               .status(Response.Status.OK).build();
            }
        }
        
        ErrorResponse errorResponse = new ErrorResponse(UUID.randomUUID(),
            Response.Status.BAD_REQUEST, "ERR_001", "Creation failed",
            "Class was not created", Arrays.asList("La clase ya existe"));
        return Response.ok(gson.toJson(errorResponse), MediaType.APPLICATION_JSON)
                .status(Response.Status.BAD_REQUEST).build();
        // status code 400:  Bad Request
    }
    
    @DELETE
    @Path("{classCode}")
    public Response deleteMyClass(@PathParam("classCode") String classCode) {
        // search student
        MyClass myclass = GenericResource.getMyClass(classCode);
        if (myclass != null) {
            GenericResource.removeMyClass(classCode);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @PUT
    @Path("{classCode}")
    public Response editMyClass(String jsonString) {
        System.out.println(jsonString);
        MyClass myclass = gson.fromJson(jsonString, MyClass.class);
        MyClass original = GenericResource.getMyClass(myclass.getCode());
        
        if (original != null) {
            if (!myclass.getCode().isEmpty()) {
                original.setCode(myclass.getCode());
            }
            if (!myclass.getTitle().isEmpty()) {
                original.setTitle(myclass.getTitle());
            }
            if (!myclass.getDescription().isEmpty()) {
                original.setDescription(myclass.getDescription());
            }
            List<Integer> invalidIds = original.setStudentIds(myclass.getStudentIds());

            System.out.println("classCode=" + original.getCode() + 
                    "title=" + original.getTitle() +
                    "Description=" + original.getDescription() + 
                    "studentIds=" + original.getStudentIds());
            
            // create class
            GenericResource.putMyClass(original);
            
            if (!invalidIds.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(UUID.randomUUID(),
                              Response.Status.BAD_REQUEST, "ERR_002", "Creation with errors",
                              "Invalid ids: " + invalidIds.toString(), Arrays.asList("Algunos estudiantes no existen"));
                return Response.ok(gson.toJson(errorResponse), MediaType.APPLICATION_JSON)
                               .status(Response.Status.OK).build();
            }
        }
        
        return Response.status(Response.Status.BAD_REQUEST).build();
        // status code 400:  Bad Request
    }
    
}
