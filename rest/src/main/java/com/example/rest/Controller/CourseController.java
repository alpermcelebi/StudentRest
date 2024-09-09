package com.example.rest.Controller;

import com.example.rest.Dtos.StudentDto;
import com.example.rest.Mappers.CourseMapper;
import com.example.rest.Dtos.CourseDto;
import com.example.rest.Mappers.StudentMapper;
import com.example.rest.Models.Course;
import com.example.rest.Models.ErrorResponse;
import com.example.rest.Models.Student;
import com.example.rest.Services.CourseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseController(CourseService courseService, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto) {
        try {
            Course course = courseMapper.toEntity(courseDto);
            Course createdCourse = courseService.addCourse(course);
            CourseDto createdCourseDto = courseMapper.toDto(createdCourse);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourseDto);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to create course", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            CourseDto courseDto = courseMapper.toDto(course);
            return ResponseEntity.ok(courseDto);
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Course not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<?> getStudentsByCourseId(@PathVariable Long id) {
        try {
            List<Student> students = courseService.getStudentsByCourseId(id);
            List<StudentDto> studentDtos = students.stream()
                    .map(student -> new StudentDto(student.getFirstName(), student.getLastName(), student.getYear()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(studentDtos);
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Course not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            List<CourseDto> courseDtos = courses.stream()
                    .map(courseMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(courseDtos);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to retrieve courses", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseDto updatedCourseDto) {
        try {
            Course updatedCourse = courseMapper.toEntity(updatedCourseDto);
            Course course = courseService.updateCourse(id, updatedCourse);
            CourseDto updatedCourseResponse = courseMapper.toDto(course);
            return ResponseEntity.ok(updatedCourseResponse);
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Course not found", HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to update course", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok("Course deleted successfully");
        } catch (EntityNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("Failed to delete course", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @DeleteMapping("/cache")
    public ResponseEntity<String> deleteCache(){
        courseService.deleteCache();
        return ResponseEntity.ok("Cache Deleted");
    }
}
