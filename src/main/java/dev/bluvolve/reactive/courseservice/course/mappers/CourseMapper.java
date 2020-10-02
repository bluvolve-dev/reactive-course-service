package dev.bluvolve.reactive.courseservice.course.mappers;

import dev.bluvolve.reactive.courseservice.course.Category;
import dev.bluvolve.reactive.courseservice.course.CategoryService;
import dev.bluvolve.reactive.courseservice.course.Course;
import dev.bluvolve.reactive.courseservice.course.CourseDto;
import dev.bluvolve.reactive.courseservice.course.commands.CreateCourse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class CourseMapper {
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;

    public CourseMapper(ModelMapper modelMapper, CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
    }

    public Course commandToEntity(CreateCourse command){
        log.debug("Convert 'CreateCourse' command to new course instance. ['userId': {}, 'title', {}]",
                command.getCreatedByUserId(), command.getTitle());

        Category category = this.categoryService.getById(command.getCategoryId());

        Course course = modelMapper.map(command, Course.class);
        course.setId(UUID.randomUUID());
        course.setCategory(category);

        log.debug("Course entity {} with id {} initialized.", course.getTitle(), course.getId());
        return course;
    }

    public CourseDto entityToDto(Course course){
        log.debug("Convert 'Course' entity to DTO. ['id': {}, 'title', {}]",
                course.getId(), course.getTitle());

        CourseDto dto = modelMapper.map(course, CourseDto.class);

        log.debug("DTO '{}' initialized with id {}", course.getTitle(), course.getId());
        return dto;
    }
}
