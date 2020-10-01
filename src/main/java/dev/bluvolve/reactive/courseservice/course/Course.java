package dev.bluvolve.reactive.courseservice.course;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Course {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 25)
    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column( nullable = false, updatable = false)
    private UUID createdByUserId;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    /* Gives a short description. */
    @Column()
    private String teaser;

    /* A detailed description of course contents. */
    @Column()
    private String description;

    /* The duration of the course. */
    @Column(nullable = false)
    private long duration = Duration.ofMinutes(45L).toMinutes();

    @PrePersist
    public void prePersist() {
        this.dateCreated = OffsetDateTime.now();
        this.lastUpdated = this.dateCreated;
    }

    @PreUpdate
    public void preUpdate() {

        this.lastUpdated = OffsetDateTime.now();
    }
}
