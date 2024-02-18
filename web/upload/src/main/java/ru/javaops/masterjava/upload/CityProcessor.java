package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.schema.CityType;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class CityProcessor {
    private static final int NUMBER_THREAD = 4;
    private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREAD);
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);
    private static final CityDao cityDao = DBIProvider.getDao(CityDao.class);

    public void process(final InputStream is) throws XMLStreamException, JAXBException, IOException {
        log.info("Start processing parse cities");
        StaxStreamProcessor staxStreamProcessor = new StaxStreamProcessor(is);
        JaxbUnmarshaller unmarshaller = JAXB_PARSER.createUnmarshaller();
        List<Future<City>> futuresCity = new ArrayList<>();
        while (staxStreamProcessor.doUntil(XMLEvent.START_ELEMENT, "City")) {
            CityType cityType = unmarshaller.unmarshal(staxStreamProcessor.getReader(), CityType.class);
            City city = new City(cityType.getValue());
            addCity(city, futuresCity);
        }
        List<City> cities = new ArrayList<>();
        try {
            for (Future<City> f : futuresCity) {
                cities.add(f.get());
            }
        } catch (InterruptedException | ExecutionException e) {

        }
    }

 private void addCity(City city, List<Future<City>> futuresCity) {
        Future<City> futureCity = executorService.submit(() -> cityDao.insert(city));
        futuresCity.add(futureCity);
        log.info("city: {} insert", city.getCity());
    }

}
