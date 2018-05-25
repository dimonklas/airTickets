package autotest.utils;

import com.google.gson.Gson;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class JaxbGsonUtils {

	public String objectToXml(Object obj, Class... classes) throws JAXBException {
		JAXBContext jaxbContextSes = JAXBContext.newInstance(classes);
		Marshaller marshaller = jaxbContextSes.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(obj, sw);
		return sw.toString();
	}

	public Object xmlToObject(String xml, Class... classes) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(classes);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(xml);
		return unmarshaller.unmarshal(reader);
	}

	public String objectToJson(Object obj) throws JAXBException {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	public Object jsonToObject(String json,Class clas) throws JAXBException {
		Gson gson = new Gson();
		return gson.fromJson(json, clas);
	}
}
