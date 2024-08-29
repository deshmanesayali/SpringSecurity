package com.sayali.SpringSecurity.controller;

import com.sayali.SpringSecurity.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    private List<Student> students = new ArrayList<>(List.of(
        new Student(1, "Sayali", 90),
        new Student(2, "Sagar", 80),
        new Student(3, "Suresh", 70)
    ));

    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    } //List<Student>

    @PostMapping("/add-students")
    public Student addStudent(@RequestBody Student newStudent) {
        students.add(newStudent);
        return newStudent;
    }

   //This code snippet is a Java method that is annotated with @GetMapping("/csrf-token").
   // It is a handler for an HTTP GET request to the /csrf-token endpoint. The method takes an HttpServletRequest object as a parameter.
   // It retrieves the CSRF token from the request's attributes using the key "_csrf" and returns it.
   // The return type of the method is CsrfToken.
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");

    }
}
