package com.example.operatedarocket.utils.Emails;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class EmailReader {
    public List<Email> emails = new ArrayList<>();
    public List<Email> initEmails() {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader reader = factory.createXMLEventReader(EmailReader.class.getResourceAsStream("/Inbox.xml"));

            String emailId = null;
            String title = null;
            String uuid = null;
            StringBuilder bodyContent = new StringBuilder();
            boolean inBody = false;

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    String tagName = startElement.getName().getLocalPart();

                    if ("email".equals(tagName)) {
                        emailId = startElement.getAttributeByName(new QName("id")).getValue();
                        title = startElement.getAttributeByName(new QName("title")).getValue();
                        uuid = startElement.getAttributeByName(new QName("key")).getValue();
                        bodyContent.setLength(0);
                    } else if ("body".equals(tagName)) {
                        inBody = true;
                    }

                } else if (event.isCharacters() && inBody) {
                    bodyContent.append(event.asCharacters().getData());

                } else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    String tagName = endElement.getName().getLocalPart();

                    if ("body".equals(tagName)) {
                        inBody = false;
                    } else if ("email".equals(tagName)) {
                        Email e = new Email();
                        e.id = emailId;
                        e.title = title;
                        e.uuid = uuid;
                        e.body = bodyContent.toString().trim();
                        e.sent = false;
                        emails.add(e);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return emails;
    }

    public Email getEmail(String id) {
        for (Email e : emails) {
            if (id.equals(e.uuid)) {
                return e;
            }
        }
        return null;
    }
}
