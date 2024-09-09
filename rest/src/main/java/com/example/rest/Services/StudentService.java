package com.example.rest.Services;


import java.util.List;

import com.example.rest.Models.Course;
import com.example.rest.Models.Student;
import com.example.rest.Repo.CourseRepo;
import com.example.rest.Repo.StudentRepo;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;



@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private CourseRepo courseRepository;

    private final NotificationService notificationService;

    public StudentService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @CachePut(value = "students", key = "#student.id")
    public Student addStudent(Student student) {
        try {
            System.out.println("Saving student: " + student.toString());
            return studentRepo.save(student);
        } catch (Exception e) {
            System.out.println("Error saving student: " + e.getMessage());
            throw e;
        }
    }

    @Cacheable(value = "students", key = "#id")
    public Student getStudentById(Long id) {
        return studentRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Student with " + id + " not found"));
    }

    public boolean studentExists(Long id){
        return studentRepo.existsById(id);
    }

    @Cacheable(value = "coursesByStudent", key = "#studentId")
    public List<Course> getCoursesByStudentId(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return student.getCourses();
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    @CachePut(value = "students", key = "#id")
    public Student updateStudent(Long id, Student updatedStudent) {
        Student student = getStudentById(id);

        student.setFirstName(updatedStudent.getFirstName());
        student.setLastName(updatedStudent.getLastName());
        student.setYear(updatedStudent.getYear());

        return studentRepo.save(student);
    }

    @CacheEvict(value = "students", key = "#id")
    public boolean deleteStudent(Long id) {

        if(studentRepo.existsById(id)){
            studentRepo.deleteById(id);
            return true;
        }
        return false;

    }
    public void registerStudentToCourse(Long studentId, Long courseId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        // Check if the student is already enrolled in the course
        if (student.getCourses().contains(course)) {
            throw new IllegalArgumentException("Student is already registered for this course");
        }

        // Add the course to the student's list
        student.getCourses().add(course);

        // Save the updated student
        studentRepo.save(student);

        // Send a notification after successful registration
        String message = "Student registered";
        notificationService.sendNotification(message);

    }

    public void deregisterStudentFromCourse(Long studentId, Long courseId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        if (!student.getCourses().contains(course)) {
            throw new IllegalArgumentException("Student is not registered for this course");
        }

        student.getCourses().remove(course);
        studentRepo.save(student);

        // Send a notification after deregister
        String message = "Student deregistered";
        notificationService.sendNotification(message);

    }

    //@RabbitListener(queues = "firstStepQueue")
    public void registerMessage(String message){
        System.out.println(message + "!");
    }



}
