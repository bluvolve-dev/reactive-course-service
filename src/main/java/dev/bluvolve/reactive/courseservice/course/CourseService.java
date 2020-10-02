package dev.bluvolve.reactive.courseservice.course;


import dev.bluvolve.reactive.courseservice.course.commands.CreateCourse;
import dev.bluvolve.reactive.courseservice.course.events.CourseCreated;
import dev.bluvolve.reactive.courseservice.course.mappers.CourseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseService {

    private final ICourseRepository courseRepository;

    private final ApplicationEventPublisher publisher;

    private final CourseMapper mapper;

    @Autowired
    public CourseService(ICourseRepository courseRepository, ApplicationEventPublisher publisher, CourseMapper mapper) {
        this.courseRepository = courseRepository;
        this.publisher = publisher;
        this.mapper = mapper;
    }

    /**
     * Creates a new course from the given 'CreateCourse' command.
     * @param command the command.
     * @return an instance of the saved course.
     */
    public Course createCourse(CreateCourse command)  {
        Assert.notNull(command, "The given command must not be null!");

        log.debug("Try to create new course {} requested by {}.", command.getTitle(), command.getCreatedByUserId());

        Course course = this.mapper.commandToEntity(command);

        Course savedCourse = this.courseRepository.save(course);
        log.debug("Course {} saved to database. Created timestamp {}", savedCourse.getId(), savedCourse.getDateCreated());

        this.publisher.publishEvent(new CourseCreated(savedCourse));

        return savedCourse;
    }

    /**
     * Fetches all courses.
     * @return a list of courses.
     */
    public List<CourseDto> getCourses() {
        return this.courseRepository.findAll()
                .stream().map(c -> this.mapper.entityToDto(c))
                .collect(Collectors.toList());
    }
}
