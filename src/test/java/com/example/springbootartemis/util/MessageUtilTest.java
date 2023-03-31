package com.example.springbootartemis.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MessageUtilTest {

    private static final String VAI_OBJECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><vai_object><vai_header><source_system>AVT</source_system><source_environment>AAVT1</source_environment><source_object_id>M/1562815970/XXX</source_object_id><vai_service_id>FTP_EAM_1875FINAXB2B</vai_service_id><vai_service_version>1.0</vai_service_version></vai_header><vai_body><source_transfertype>file</source_transfertype><source_directory>/avai1icn/global/vai/work</source_directory><source_filename>FTP_SWIFT_CLIENT_1875FINAXB2B_006086_20201022052918654</source_filename><source_filesize>236</source_filesize><target_filename>FTP_SWIFT_CLIENT_1875FINAXB2B_006086_20201022052918654</target_filename></vai_body></vai_object>";
    private static final String STAFF_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<Company>\n" +
            "    <staff id=\"1001\">\n" +
            "        <name>mkyong</name>\n" +
            "        <role>support</role>\n" +
            "        <salary currency=\"USD\">5000</salary>\n" +
            "        <!-- for special characters like < &, need CDATA -->\n" +
            "        <bio><![CDATA[HTML tag <code>testing</code>]]></bio>\n" +
            "    </staff>\n" +
            "    <staff id=\"1002\">\n" +
            "        <name>yflow</name>\n" +
            "        <role>admin</role>\n" +
            "        <salary currency=\"EUR\">8000</salary>\n" +
            "        <bio><![CDATA[a & b]]></bio>\n" +
            "    </staff>\n" +
            "</Company>";
    StaxMessageUtil sut = new StaxMessageUtil(XMLInputFactory.newInstance());

    @Test
    public void testEmptyObject() {
        XMLStreamException thrown = Assertions.assertThrows(XMLStreamException.class, () -> {
            VaiHeader vaiHeader = sut.parseVaiHeader("");
        });
        Assertions.assertEquals("Message can not be null or blank!", thrown.getMessage());

        XMLStreamException npe = Assertions.assertThrows(XMLStreamException.class, () -> {
            VaiHeader vaiHeader = sut.parseVaiHeader(null);
        });
        Assertions.assertEquals("Message can not be null or blank!", npe.getMessage());
    }

    @Test
    public void testNoVaiObjectParsedStaffXml() throws XMLStreamException {
        VaiHeader vaiHeader = sut.parseVaiHeader(STAFF_XML);
        assertNull(vaiHeader);
    }

    @Test
    public void testSuccessfullyParsedVaiObject() throws XMLStreamException {
        VaiHeader vaiHeader = sut.parseVaiHeader(VAI_OBJECT_XML);
        assertEquals("AVT", vaiHeader.getSourceSystem());
        assertEquals("AAVT1", vaiHeader.getSourceEnvironment());
        assertEquals("FTP_EAM_1875FINAXB2B", vaiHeader.getVaiServiceId());
        assertEquals("1.0", vaiHeader.getVaiServiceVersion());
    }
}