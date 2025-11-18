package com.academy.management.service;

import com.academy.management.dao.CourseAppDao;
import com.academy.management.model.CourseApp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseAppService {

    private final CourseAppDao courseAppDao;

    public CourseAppService(CourseAppDao courseAppDao) {
        this.courseAppDao = courseAppDao;
    }

    // GET all courses
    public List<CourseApp> getAllCourses() {
        return courseAppDao.getAllCourses();
    }

    // GET course by ID
    public CourseApp getCourseById(Long id) {
        return courseAppDao.getCourseById(id);
    }

    // CREATE / POST course
    public CourseApp createCourse(CourseApp course) {
        return courseAppDao.save(course);
    }

    // PUT / full update
    public void updateCourse(CourseApp course) {
        courseAppDao.update(course);
    }

    // PATCH / partial update
    public void patchCourse(CourseApp course) {
        courseAppDao.updatePartial(course);
    }

    // DELETE course
    public boolean deleteCourse(Long id) {
        return courseAppDao.delete(id);
    }
}
