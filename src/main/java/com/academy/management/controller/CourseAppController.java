package com.academy.management.controller;

import com.academy.management.model.CourseApp;
import com.academy.management.service.CourseAppService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseAppController {

    private final CourseAppService courseService;

    public CourseAppController(CourseAppService courseService) {
        this.courseService = courseService;
    }

    // -------------------------
    // CREATE / POST
    // -------------------------
    @PostMapping
    public ResponseEntity<CourseApp> createCourse(@RequestBody CourseApp course) {
        CourseApp savedCourse = courseService.createCourse(course);
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    // -------------------------
    // GET all courses
    // -------------------------
    @GetMapping
    public ResponseEntity<List<CourseApp>> getAllCourses() {
        List<CourseApp> courses = courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    // -------------------------
    // GET course by ID
    // -------------------------
    @GetMapping("/{id}")
    public ResponseEntity<CourseApp> getCourse(@PathVariable Long id) {
        CourseApp course = courseService.getCourseById(id);
        if (course == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    // -------------------------
    // PUT / full update
    // -------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable Long id, @RequestBody CourseApp course) {
        course.setId(id);
        courseService.updateCourse(course);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // -------------------------
    // PATCH / partial update
    // -------------------------
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchCourse(@PathVariable Long id, @RequestBody CourseApp course) {
        course.setId(id);
        courseService.patchCourse(course);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // -------------------------
    // DELETE
    // -------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        boolean deleted = courseService.deleteCourse(id);
        if (deleted) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
