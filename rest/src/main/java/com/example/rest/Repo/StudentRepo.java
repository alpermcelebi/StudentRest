package com.example.rest.Repo;

import com.example.rest.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// By extending JpaRepository,
// StudentRepo inherits a wide range of CRUD
// operations for the Student entity.
@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
    List<Student> findByCourses_Id(Long courseId);
}
