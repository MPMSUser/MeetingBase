/*
 *  Copyright 2025 Better Orange IR & HV AG
 *
 *  Licensed under the Meetingbase License (the "License");
 *  Vou may not use this file except in compliance with the License.
 *  You may obtain a copy of the License in the root directory (MEETINGBASE_LICENSE).
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.meetingapps.meetingportal.meetComAllg;

import java.io.StringReader;
import java.io.StringWriter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

/**
 * The {@code CaXML} class provides utility methods for marshalling
 * (converting Java objects to XML) and unmarshalling (converting XML to Java objects)
 * using Jakarta XML Binding (JAXB).
 */
public class CaXML {

    /**
     * Converts an XML string to a Java object of the specified type.
     * 
     * @param <T> the type of the desired object
     * @param xml the XML string to be converted
     * @param clazz the class of the desired object
     * @return an object of type {@code T} represented by the given XML string
     * @throws JAXBException if an error occurs during the unmarshalling process
     */
    public static <T> T unmarshal(String xml, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return clazz.cast(unmarshaller.unmarshal(new StringReader(xml)));
    }

    /**
     * Converts a Java object to its XML string representation.
     * 
     * @param object the object to be converted to XML
     * @return the XML string representing the given object
     * @throws JAXBException if an error occurs during the marshalling process
     */
    public static String marshal(Object object) {
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);

            return stringWriter.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String marshalAndPrepare(Object object) {

        String xml = null;

        try {

            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);

            xml = stringWriter.toString();
            xml = xml.replaceAll("(?<=>)(\\s+)(?=<)", "");
            xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "").trim();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return xml;
    }

    public static String removeBlanks(String xml) {
        return xml.replaceAll("(?<=>)(\\s+)(?=<)", "");
    }
}