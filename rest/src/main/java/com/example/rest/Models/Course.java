package com.example.rest.Models;
import java.io.Serial;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "course")
public class Course implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "weeklyHours")
    private int weeklyHours;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private List<Student> students;

    public Course() {}

    public Course(Long id, String name, int weeklyHours) {
        this.id = id;
        this.name = name;
        this.weeklyHours = weeklyHours;
    }

    // For DTO mapping
    public Course(String name, int weeklyHours) {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(int weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
