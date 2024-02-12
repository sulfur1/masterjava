package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Group extends BaseEntity {
    private @NonNull String name;
    @Column("type_group")
    private @NonNull TypeGroup typeGroup;

    public Group(Integer id, @NonNull String name, @NonNull TypeGroup typeGroup) {
        this(name, typeGroup);
        this.id = id;
    }
}
