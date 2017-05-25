/*
 * GenericResource.java
 */

package bo.edu.ucbcba.simplescheduling.resource;

import bo.edu.ucbcba.simplescheduling.model.MyClass;
import bo.edu.ucbcba.simplescheduling.model.Student;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Diego
 */
public class GenericResource {
    
    private static final Map<Integer, Student> studentMap = new HashMap<>();
    private static final Map<String, MyClass> myClassMap = new HashMap<>();
    
    public static void putStudent(Student student) {
        if (student != null) {
            studentMap.put(student.getStudentId(), student);
        }
    }
    
    public static Student getStudent(Integer studentId) {
        return studentMap.get(studentId);
    }
    
    public static boolean removeStudent(Integer studentId) {
        return studentMap.remove(studentId) != null;
    }
    
    public static List<Student> getStudentList() {
        List<Student> resp = new ArrayList<>(studentMap.values());
        return resp;
    }
    
    public static void putMyClass(MyClass myclass) {
        if (myclass != null) {
            myClassMap.put(myclass.getCode(), myclass);
        }
    }
    
    public static MyClass getMyClass(String classCode) {
        return myClassMap.get(classCode);
    }
    
    public static boolean removeMyClass(String classCode) {
        return myClassMap.remove(classCode) != null;
    }
    
    public static List<MyClass> getMyClassList() {
        List<MyClass> resp = new ArrayList<>(myClassMap.values());
        return resp;
    }

    public static Map<Integer, Student> getStudentMap() {
        return studentMap;
    }

    public static Map<String, MyClass> getMyClassMap() {
        return myClassMap;
    }
    
}
