package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    public City insert(City city) {
        if (city.isNew()) {
            int id = insertGeneratedId(city);
            city.setId(id);
        } else {
            insertWithId(city);
        }
        return city;
    }
    @SqlQuery("SELECT nextval('cities_seq')")
    public abstract int getNextVal();

    @SqlUpdate("TRUNCATE cities")
    @Override
    public abstract void clean();

    @SqlUpdate("INSERT INTO cities (city) VALUES (:city) ON CONFLICT DO NOTHING")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO cities (id, city) VALUES (:id, :city) ON CONFLICT DO NOTHING")
    @GetGeneratedKeys
    abstract int insertWithId(@BindBean City city);

    @SqlQuery("SELECT * FROM cities LIMIT :it")
    abstract List<City> getWithLimit(@Bind int limit);

}
