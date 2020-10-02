package dev.bluvolve.reactive.courseservice.course.processors;

import dev.bluvolve.reactive.courseservice.course.events.CourseCreated;
import dev.bluvolve.reactive.courseservice.course.listeners.ICourseCreatedEventListener;
import dev.bluvolve.reactive.courseservice.course.mappers.CourseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Processes the 'CourseCreated' event.
 */
@Slf4j
@Component
public class CourseCreatedEventProcessor implements ApplicationListener<CourseCreated> {
    private ICourseCreatedEventListener listener;
    private final CourseMapper mapper;

    public CourseCreatedEventProcessor(CourseMapper mapper) {
        this.mapper = mapper;
    }

    public void register(ICourseCreatedEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void onApplicationEvent(CourseCreated event) {
        this.onEvent(event);
    }

    private void onEvent(CourseCreated event) {
        if (this.listener != null) {
            this.listener.onData(event, mapper);
        }
    }
}
