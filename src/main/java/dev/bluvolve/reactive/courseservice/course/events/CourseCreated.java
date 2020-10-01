package dev.bluvolve.reactive.courseservice.course.events;

import dev.bluvolve.reactive.courseservice.course.Course;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CourseCreated extends ApplicationEvent {
    public CourseCreated(Course course) {
        super(course);
    }
}
