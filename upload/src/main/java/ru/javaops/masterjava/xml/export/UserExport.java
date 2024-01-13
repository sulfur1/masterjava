package ru.javaops.masterjava.xml.export;

import ru.javaops.masterjava.xml.model.User;
import ru.javaops.masterjava.xml.model.UserFlag;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserExport {
    public List<User> process(final InputStream in) throws XMLStreamException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(in);
        List<User> users = new ArrayList<>();

        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            final String email = processor.getAttribute("email");
            final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
            final String fullName = processor.getReader().getElementText();
            User user = new User(fullName, email, flag);
            users.add(user);
        }
        return users;
    }
}
