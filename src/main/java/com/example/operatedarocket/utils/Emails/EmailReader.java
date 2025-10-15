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
    public static List<Email> getEmails() {
        ArrayList<Email> emails = new ArrayList<>();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader reader = factory.createXMLEventReader(EmailReader.class.getResourceAsStream("/Inbox.xml"));

            String emailId = null;
            String title = null;
            String inboxID = null;
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
                        bodyContent.setLength(0); // reset body
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
                        e.body = bodyContent.toString().trim();
                        emails.add(e);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return emails;
    }

    public static Email getEmail(String id) {
        for (Email e : getEmails()) {
            if (id.equals(e.id)) {
                return e;
            }
        }
        return null;
    }
}
