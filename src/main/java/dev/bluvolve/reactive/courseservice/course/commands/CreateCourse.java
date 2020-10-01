package dev.bluvolve.reactive.courseservice.course.commands;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CreateCourse {
    @NotNull
    @Size(min = 5, max = 25, message = "The title must be between 5 and 25 characters long.")
    private String title;

    @Size(max = 250, message = "The description must be a maximum of 250 characters long.")
    private String description;

    @NotNull
    private UUID categoryId;

    @NotNull
    private UUID createdByUserId;

    @NotNull
    @Range(min = 15L, max = 480L)
    private long duration = 45L;
}
