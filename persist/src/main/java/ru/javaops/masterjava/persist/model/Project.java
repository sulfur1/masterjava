package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseEntity {
    private @NonNull String name;
    private @NonNull String description;

    public Project(Integer id, @NonNull String name, @NonNull String description) {
        this(name, description);
        this.id = id;
    }
}
