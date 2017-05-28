/*
 * Student.java
 */

package bo.edu.ucbcba.simplescheduling.model;

import bo.edu.ucbcba.simplescheduling.resource.GenericResource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego
 */
public class Student {

    private int studentId;
    private String lastName;
    private String firstName;
    private List<String> classCodes;

    public Student() {
        lastName = firstName = "";
        classCodes = new ArrayList<>();
    }

    public Student(int studentId, String lastName, String firstName) {
        this.studentId = studentId;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * @return the studentId
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the classCodes
     */
    public List<String> getClassCodes() {
        return classCodes;
    }

    /**
     * @param classCodes the classCodes to set
     * @return 
     */
    public List<String> setClassCodes(List<String> classCodes) {
        List<String> invalidCodes = new ArrayList<>();
        List<String> validCodes = new ArrayList<>();
        
        classCodes.forEach((code) -> {
            MyClass myclass = GenericResource.getMyClassMap().get(code);
            if (myclass != null) {
                validCodes.add(code);
                if (!myclass.getStudentIds().contains(this.studentId)) {
                    myclass.getStudentIds().add(this.studentId);
                }
            } else {
                invalidCodes.add(code);
            }
        });
        
        this.classCodes = validCodes;
        
        return invalidCodes;
    }
    
}
