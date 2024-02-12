package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

import static ru.javaops.masterjava.persist.CityTestData.getNew;

public class CityDaoTest extends AbstractDaoTest<CityDao> {


    public CityDaoTest() {
        super(CityDao.class);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        CityTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        CityTestData.setUp();
    }

    @Test
    public void testInsert() {
        City expected = getNew();
        City actual = dao.insert(expected);
        Assert.assertNotNull(actual);
        expected.setId(actual.getId());
        Assert.assertSame(actual, expected);
    }

    @Test
    public void testGetWithLimit() {
        List<City> actual = dao.getWithLimit(4);
        Assert.assertEquals(CityTestData.fourCityList, actual);
    }
}