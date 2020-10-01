package dev.bluvolve.reactive.courseservice.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ICourseRepository extends JpaRepository<Course, UUID> {
    // add custom queries here
    List<Course> findCoursesByCategory(Category category);
}
