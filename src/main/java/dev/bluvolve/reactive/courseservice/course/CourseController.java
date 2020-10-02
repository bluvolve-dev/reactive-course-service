package dev.bluvolve.reactive.courseservice.course;

import dev.bluvolve.reactive.courseservice.course.commands.CreateCourse;
import dev.bluvolve.reactive.courseservice.course.events.CourseCreated;
import dev.bluvolve.reactive.courseservice.course.listeners.ICourseCreatedEventListener;
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
    private final CourseCreatedEventProcessor processor;

    public CourseController(CourseService courseService,
                            CategoryService categoryService,
                            CourseCreatedEventProcessor processor) {

        this.courseService = courseService;
        this.categoryService = categoryService;
        this.processor = processor;
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
        return this.createStream();
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

    private Flux<CourseDto> createStream() {
        Flux<CourseDto> flux = Flux.create(sink -> {
            processor.register(new ICourseCreatedEventListener() {
                @Override
                public void processComplete() {

                    sink.complete();
                }

                @Override
                public void onData(CourseCreated data, CourseMapper mapper) {
                    log.debug("New course received. Start handling...");
                    Course course = (Course) data.getSource();
                    CourseDto dto = mapper.entityToDto(course);
                    log.info("Course model '{}' generated from incoming data.", dto.toString());
                    sink.next(dto);
                }
            });
        });

        return flux;
    }
}
