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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Diego
 */
@Path("v1/students")
public class StudentsResource {

    @Context
    private UriInfo context;
    private final Gson gson = new Gson();

    /**
     * Creates a new instance of StudentsResource
     */
    public StudentsResource() {
    }
    
    /**
     * Returns a student based on it´s studentId.
     * @param studentId
     * @return 
     */
    
    // FORMA ORIGINAL DE ESTE METODO:
    // public String students(@QueryParam("studentId") int studentId)
    
    @GET
    @Path("{studentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("studentId") Integer studentId) {
        // search student
        Student student = GenericResource.getStudent(studentId);
        if (student != null) {
            return Response.ok(gson.toJson(student), MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentList() {
        return Response.ok(gson.toJson(GenericResource.getStudentList()), 
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
    public Response students(String jsonString) {
        System.out.println(jsonString);
        Student student = gson.fromJson(jsonString, Student.class);
        
        if (GenericResource.getStudent(student.getStudentId()) == null) {
            int studentId = student.getStudentId();
            String lastName = student.getLastName();
            String firstName = student.getFirstName();
            List<String> classCodes = student.getClassCodes();

            System.out.println("studentId=" + studentId + "lastName=" + lastName +
                    "firstName=" + firstName + "classCodes=" + classCodes);
            
            List<String> invalidCodes = student.setClassCodes(classCodes);
            
            // create student
            GenericResource.putStudent(student);
            
            if (!invalidCodes.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(UUID.randomUUID(),
                              Response.Status.BAD_REQUEST, "ERR_002", "Creation with errors",
                              "Invalid codes: " + invalidCodes.toString(), Arrays.asList("Algunas clases no existen"));
                return Response.ok(gson.toJson(errorResponse), MediaType.APPLICATION_JSON)
                               .status(Response.Status.OK).build();
            }
            
            return Response.ok(gson.toJson(student), MediaType.APPLICATION_JSON).
                            status(Response.Status.CREATED).
                            build();
        }
        
        ErrorResponse errorResponse = new ErrorResponse(UUID.randomUUID(),
            Response.Status.BAD_REQUEST, "ERR_001", "Creation failed",
            "Student was not created", Arrays.asList("El usuario ya existe"));
        return Response.ok(gson.toJson(errorResponse), MediaType.APPLICATION_JSON)
                .status(Response.Status.BAD_REQUEST).build();
        // status code 400:  Bad Request
    }
    
    @DELETE
    @Path("{studentId}")
    public Response deleteStudent(@PathParam("studentId") Integer studentId) {
        // search student
        Student student = GenericResource.getStudent(studentId);
        if (student != null) {
            for (String code : student.getClassCodes()) {
                MyClass myclass = GenericResource.getMyClass(code);
                
                if (myclass != null) {
                    myclass.getStudentIds().remove(studentId);
                }
            }
            GenericResource.removeStudent(studentId);
        }
        return Response.status(Response.Status.NO_CONTENT).build();
        // Response es una respuesta estandar para solicitudes http.
        // status es un metodo que retorna el codigo de error o cualquier otro codigo.
        // NO_CONTENT siginifica que no se encontro el recurso.
        // build() se coloca al final cuando ya se tiene el response que se va a retornar.
    }
    
    @PUT
    @Path("{studentId}")
    public Response editStudent(String jsonString) {
        System.out.println(jsonString);
        Student student = gson.fromJson(jsonString, Student.class);
        Student original = GenericResource.getStudent(student.getStudentId());
        
        if (original != null) {
            if (student.getStudentId() != 0) {
                original.setStudentId(student.getStudentId());
            }
            if (!student.getLastName().isEmpty()) {
                original.setLastName(student.getLastName());
            }
            if (!student.getFirstName().isEmpty()) {
                original.setFirstName(student.getFirstName());
            }
            List<String> invalidCodes = original.setClassCodes(student.getClassCodes());

            System.out.println("studentId=" + original.getStudentId() + 
                    "lastName=" + original.getLastName() +
                    "firstName=" + original.getFirstName() + 
                    "classCodes=" + original.getClassCodes());
            // update student
            GenericResource.putStudent(original);

            if (!invalidCodes.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse(UUID.randomUUID(),
                              Response.Status.BAD_REQUEST, "ERR_002", "Creation with errors",
                              "Invalid codes: " + invalidCodes.toString(), Arrays.asList("Algunas clases no existen"));
                return Response.ok(gson.toJson(errorResponse), MediaType.APPLICATION_JSON)
                               .status(Response.Status.OK).build();
            }
            
            return Response.ok(gson.toJson(original), MediaType.APPLICATION_JSON).
                            status(Response.Status.CREATED).
                            build();
        }
        
        return Response.status(Response.Status.BAD_REQUEST).build();
        // status code 400:  Bad Request
    }
    
}
