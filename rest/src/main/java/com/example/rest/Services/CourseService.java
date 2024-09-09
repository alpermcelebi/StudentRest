package com.example.rest.Services;

import java.util.List;

import com.example.rest.Models.Course;
import com.example.rest.Models.Student;
import com.example.rest.Repo.CourseRepo;
import com.example.rest.Repo.StudentRepo;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepo courseRepo;

    private final StudentRepo studentRepo;

    public CourseService(CourseRepo courseRepo, StudentRepo studentRepo) {
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
    }


    @CachePut(value = "courses", key = "#course.id")
    public Course addCourse(Course course) {
        return courseRepo.save(course);
    }

    @Cacheable(value = "courses", key = "#id")
    public Course getCourseById(Long id) {
        return courseRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Course with " + id + " not found"));
    }

    @Cacheable(value = "studentsByCourse", key = "#courseId")
    public List<Student> getStudentsByCourseId(Long courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        return course.getStudents();
    }
    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }
    public void deleteCache(){}

    @CachePut(value = "courses", key = "#id")
    public Course updateCourse(Long id, Course updatedCourse) {
        Course course = getCourseById(id);
        course.setName(updatedCourse.getName());
        course.setWeeklyHours(updatedCourse.getWeeklyHours());
        return courseRepo.save(course);
    }

    @CacheEvict(value = "courses", key = "#courseId")
    public void deleteCourse(Long courseId) {
        Course course = getCourseById(courseId);
        List<Student> students = studentRepo.findByCourses_Id(courseId);

        for (Student student : students) {
            student.getCourses().remove(course);
            studentRepo.save(student);  // Save changes to the student
        }

        courseRepo.delete(course);
    }
}
