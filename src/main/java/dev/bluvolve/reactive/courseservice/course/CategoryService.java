package dev.bluvolve.reactive.courseservice.course;

import dev.bluvolve.reactive.courseservice.course.mappers.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {

    private final ICategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryService(ICategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Returns the instance of a category by given id.
     * @param id the given id.
     * @return category instance.
     */
    public Category getById(UUID id){
        Assert.notNull(id, "The given id must not be null!");

        log.debug("Try to get Category with id {}", id);
        Category category = this.repository.getOne(id);

        if(category == null){
            log.error("Category with id {} does not exist.", id);
            return null;
        }

        log.debug("Category {} with id {} was found.", category.getTitle(), id);
        return category;
    }

    /**
     * Fetches all categories.
     * @return a list of categories.
     */
    public List<CategoryDto> getCategories() {
        return this.repository.findAll()
                .stream().map(c -> this.mapper.entityToDto(c))
                .collect(Collectors.toList());
    }
}
