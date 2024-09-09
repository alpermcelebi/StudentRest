package com.example.rest.Mappers;

import com.example.rest.Dtos.CourseDto;
import com.example.rest.Models.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseDto toDto(Course course) {
        return new CourseDto(course.getName(), course.getWeeklyHours());
    }

    public Course toEntity(CourseDto courseDto) {
        return new Course(courseDto.name(), courseDto.weeklyHours());
    }
}
