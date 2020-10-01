package dev.bluvolve.reactive.courseservice.utils;

import dev.bluvolve.reactive.courseservice.course.Category;
import dev.bluvolve.reactive.courseservice.course.ICategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CategoryInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ICategoryRepository repository;

    public CategoryInitializer(ICategoryRepository repository) {
        log.info("Run CategoryInitializer...");
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (this.repository.count() > 0){
            log.info("Category items already created.");
            return;
        }

        List<Category> categories = new ArrayList<>() {
            {
                add(new Category("Bootcamp"));
                add(new Category("Circuit Training"));
                add(new Category("Gymnastics"));
                add(new Category("Outdoor"));
                add(new Category("Weight Training"));
            }
        };

        categories.forEach(category -> {
            this.repository.save(category);
            log.info("Category '{}' saved. ID: {}", category.getTitle(), category.getId());
        });
    }
}