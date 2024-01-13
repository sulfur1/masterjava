package ru.javaops.masterjava.xml.export;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.xml.model.User;
import ru.javaops.masterjava.xml.util.ThymeleafUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ExportXml", urlPatterns = "/upload")
public class ExportXmlServlet extends HttpServlet {
    private final UserExport userExport = new UserExport();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        final TemplateEngine templateEngine = ThymeleafUtil.getTemplateEngine(req.getServletContext());
        templateEngine.process("export", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final ServletFileUpload servletFileUpload = new ServletFileUpload();
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        final TemplateEngine templateEngine = ThymeleafUtil.getTemplateEngine(req.getServletContext());

        try {
            final FileItemIterator itemIterator = servletFileUpload.getItemIterator(req);
            while (itemIterator.hasNext()) {
                FileItemStream fileItemStream = itemIterator.next();
                if (!fileItemStream.isFormField()) {
                    try (InputStream is = fileItemStream.openStream()) {
                        List<User> users = userExport.process(is);
                        webContext.setVariable("users", users);
                        templateEngine.process("result", webContext, resp.getWriter());
                    }
                }
            }

        } catch (Exception e) {
            webContext.setVariable("exception", e);
            templateEngine.process("exception", webContext, resp.getWriter());
        }
    }
}
