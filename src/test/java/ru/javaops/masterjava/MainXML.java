package ru.javaops.masterjava;

import com.google.common.io.Resources;
import com.sun.xml.internal.ws.wsdl.writer.document.xsd.Schema;
import j2html.tags.ContainerTag;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class MainXML {
    public static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Format: projectName, xmlName");
            System.exit(1);
        }
        String projectName = args[0];
        URL payloadUrl = Resources.getResource(args[1]);
        MainXML mainXML = new MainXML();

        Set<User> users = mainXML.parseByJaxb(projectName, payloadUrl);
        String out = mainXML.outHtml(users, projectName, Paths.get("out/usersJaxb.html"));
        System.out.println(out);
    }

    private Set<User> parseByJaxb(String projectName, URL payloadUrl) throws Exception {
        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath("payload.xsd"));
        try (InputStream inputStream = payloadUrl.openStream()) {
            Payload payload = (Payload) parser.unmarshal(inputStream);
            Project project = StreamEx.of(payload.getProjects().getProject())
                    .filter(p -> p.getName().equals(projectName))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("ProjectName is not valid"));
            Set<Project.Group> groups = new HashSet<>(project.getGroup());
            return StreamEx.of(payload.getUsers().getUser())
                    .filter(u -> StreamEx.of(u.getGroupRefs())
                            .findAny(groups::contains)
                            .isPresent())
                    .collect(Collectors.toCollection(() -> new TreeSet<>(USER_COMPARATOR)));
        }
    }
    private String outHtml(Set<User> users, String projectName, Path path) throws Exception {
        try (Writer writer = Files.newBufferedWriter(path)) {
            final ContainerTag<?> table = table().with(tr().with(th("FullName"), th("email")));
            users.forEach(u -> table.with(tr().with(td(u.getValue()), td(u.getEmail()))));
            table.attr("border", "1");
            table.attr("cellpadding", "8");
            table.attr("cellspacing", "0");

            String out = html().with(
                    head().with(title(projectName + "users")),
                    body().with(h1(projectName + "users"), table)
            ).render();
            writer.write(out);

            return out;
        }
    }
}
