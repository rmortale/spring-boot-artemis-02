package com.example.springbootartemis.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.StringReader;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaxMessageUtil {

    private final XMLInputFactory xmlInputFactory;

    public VaiHeader parseVaiHeader(String message) throws XMLStreamException {
        if (isBlankString(message)) {
            throw new XMLStreamException("Message can not be null or blank!");
        }

        XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new StringReader(message));
        int eventType = reader.getEventType();
        VaiHeader header = null;

        while (reader.hasNext()) {
            eventType = reader.next();
            if (eventType == XMLEvent.START_ELEMENT) {
                switch (reader.getName().getLocalPart()) {
                    case "vai_header":
                        header = new VaiHeader();
                        break;
                    case "source_system":
                        eventType = reader.next();
                        if (eventType == XMLEvent.CHARACTERS) {
                            header.setSourceSystem(reader.getText());
                        }
                        break;
                    case "source_environment":
                        eventType = reader.next();
                        if (eventType == XMLEvent.CHARACTERS) {
                            header.setSourceEnvironment(reader.getText());
                        }
                        break;
                    case "vai_service_id":
                        eventType = reader.next();
                        if (eventType == XMLEvent.CHARACTERS) {
                            header.setVaiServiceId(reader.getText());
                        }
                        break;
                    case "vai_service_version":
                        eventType = reader.next();
                        if (eventType == XMLEvent.CHARACTERS) {
                            header.setVaiServiceVersion(reader.getText());
                        }
                        break;
                }
            }
            if (eventType == XMLEvent.END_ELEMENT) {
                // if </vai_header>
                if (reader.getName().getLocalPart().equals("vai_header")) {
                    break;
                }
            }
        }
        return header;
    }

    private boolean isBlankString(String string) {
        return string == null || string.isBlank();
    }
}
