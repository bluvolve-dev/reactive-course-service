package dev.bluvolve.reactive.courseservice.course;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CategoryDto {
    @NonNull
    private UUID id;
    @NonNull
    private String title;
}
