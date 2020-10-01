package dev.bluvolve.reactive.courseservice.course.listeners;

import dev.bluvolve.reactive.courseservice.course.events.CourseCreated;
import dev.bluvolve.reactive.courseservice.course.mappers.CourseMapper;

public interface ICourseCreatedEventListener {
    void onData(CourseCreated event, CourseMapper mapper);
    void processComplete();
}
