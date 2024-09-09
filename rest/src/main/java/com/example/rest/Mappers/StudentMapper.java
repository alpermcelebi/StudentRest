package com.example.rest.Mappers;

import com.example.rest.Dtos.StudentDto;
import com.example.rest.Models.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public StudentDto toDto(Student student) {
        return new StudentDto(student.getFirstName(), student.getLastName(), student.getYear());
    }

    public Student toEntity(StudentDto studentDto) {
        return new Student(studentDto.firstName(), studentDto.lastName(), studentDto.year());
    }
}
