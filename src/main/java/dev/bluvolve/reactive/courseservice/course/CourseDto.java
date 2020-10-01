package dev.bluvolve.reactive.courseservice.course;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CourseDto {
    @NonNull
    private UUID id;
    @NonNull
    private String title;
    @NonNull
    private UUID categoryId;
    @NonNull
    private String categoryTitle;

    private UUID createdByUserId;

    private OffsetDateTime dateCreated;
    private OffsetDateTime lastUpdated;

    /* Gives a short description. */
    private String teaser;

    /* A detailed description of course contents. */
    private String description;

    /* The duration of the course in minutes. */
    @NonNull
    private long duration;
}
