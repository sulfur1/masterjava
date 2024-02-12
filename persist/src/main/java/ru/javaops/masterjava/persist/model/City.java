package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
public class City extends BaseEntity {
    private @NonNull String city;

    public City(Integer id, @NonNull String city) {
        this(city);
        this.id = id;
    }
}
