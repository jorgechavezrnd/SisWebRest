/*
 * MyClass.java
 */

package bo.edu.ucbcba.simplescheduling.model;

import bo.edu.ucbcba.simplescheduling.resource.GenericResource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego
 */
public class MyClass {

    private String code;
    private String title;
    private String description;
    private List<Integer> studentIds;

    public MyClass() {
        studentIds = new ArrayList<>();
    }

    public MyClass(String code, String title, String description) {
        this.code = code;
        this.title = title;
        this.description = description;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the studentIds
     */
    public List<Integer> getStudentIds() {
        return studentIds;
    }

    /**
     * @param studentIds the studentIds to set
     * @return 
     */
    public List<Integer> setStudentIds(List<Integer> studentIds) {
        List<Integer> invalidIds = new ArrayList<>();
        List<Integer> validIds = new ArrayList<>();
        
        studentIds.forEach((i) -> {
            Student student = GenericResource.getStudentMap().get(i);
            if (student != null) {
                validIds.add(i);
                if (!student.getClassCodes().contains(this.code)) {
                    student.getClassCodes().add(this.code);
                }
            } else {
                invalidIds.add(i);
            }
        });
        
        this.studentIds = validIds;
        
        return invalidIds;
        
    }
}
