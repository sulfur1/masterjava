package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;

public class CityTestData {
    public static City SPB;
    public static City MSK;
    public static City KIV;
    public static City MNSK;
    public static City Vladimir;
    public static List<City> fourCityList;

    public static void init() {
        SPB = new City("Санкт-Петербург");
        MSK = new City("Москва");
        KIV = new City("Киев");
        MNSK = new City("Минск");
        Vladimir = new City("Владимир");
        fourCityList = ImmutableList.of(SPB, MSK, KIV, MNSK);
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            fourCityList.forEach(dao::insert);
            dao.insert(Vladimir);
        });
    }

    public static City getNew() {
        return new City("Екатеринбург");
    }

}
