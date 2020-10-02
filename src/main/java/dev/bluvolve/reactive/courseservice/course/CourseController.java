package dev.bluvolve.reactive.courseservice.course;

import dev.bluvolve.reactive.courseservice.course.commands.CreateCourse;
import dev.bluvolve.reactive.courseservice.course.events.CourseCreated;
import dev.bluvolve.reactive.courseservice.course.mappers.CourseMapper;
import dev.bluvolve.reactive.courseservice.course.processors.CourseCreatedEventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class CourseController {
    private final CourseService courseService;
    private final CategoryService categoryService;
    private final CourseMapper mapper;
    private final Flux<CourseCreated> events;

    public CourseController(CourseService courseService,
                            CategoryService categoryService,
                            CourseCreatedEventProcessor processor,
                            CourseMapper mapper) {

        this.courseService = courseService;
        this.categoryService = categoryService;
        this.mapper = mapper;
        this.events = Flux.create(processor).share();
    }

    @CrossOrigin
    @PostMapping("/course")
    ResponseEntity<UUID> addCourse(@RequestBody @Valid CreateCourse command){
        log.info("Create new course request received. [title: {}]", command.getTitle());

        try{
            Course course = this.courseService.createCourse(command);
            return ResponseEntity.created(URI.create("/course/" + course.getId().toString())).body(course.getId());
        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin()
    @GetMapping(value = "/course/sse", produces = "text/event-stream;charset=UTF-8")
    public Flux<CourseDto> stream() {
        log.info("Start listening to the course collection.");
        return this.events.map(event -> {
            CourseDto dto = this.mapper.entityToDto((Course) event.getSource());
            return dto;
        });
    }

    @CrossOrigin()
    @GetMapping(value = "/course", produces = "application/json")
    public ResponseEntity<List<CourseDto>> get() {
        log.info("Fetch all courses.");

        try{
            List<CourseDto> courses = this.courseService.getCourses();
            return ResponseEntity.ok().body(courses);
        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin()
    @GetMapping(value = "/course/category", produces = "application/json")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        log.info("Fetch all categories.");

        try{
            List<CategoryDto> categories = this.categoryService.getCategories();
            return ResponseEntity.ok().body(categories);
        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
