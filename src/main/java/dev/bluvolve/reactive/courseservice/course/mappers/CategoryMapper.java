package dev.bluvolve.reactive.courseservice.course.mappers;

import dev.bluvolve.reactive.courseservice.course.*;
import dev.bluvolve.reactive.courseservice.course.commands.CreateCourse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CategoryMapper {
    private final ModelMapper modelMapper;;

    public CategoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CategoryDto entityToDto(Category category){
        log.debug("Convert 'Category' entity to DTO. ['id': {}, 'title', {}]",
                category.getId(), category.getTitle());

        CategoryDto dto = modelMapper.map(category, CategoryDto.class);

        log.debug("DTO '{}' initialized with id {}", dto.getTitle(), dto.getId());
        return dto;
    }
}
