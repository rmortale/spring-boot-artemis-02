package com.example.springbootartemis.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;

@Configuration
public class JmsConfig {

    @Bean
    public XMLInputFactory xmlInputFactory() {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        // https://rules.sonarsource.com/java/RSPEC-2755
        // prevent xxe
        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return xmlInputFactory;
    }

}
