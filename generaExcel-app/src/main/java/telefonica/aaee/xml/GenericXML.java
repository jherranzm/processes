package telefonica.aaee.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import telefonica.aaee.utils.ConstantsREG;


public class GenericXML {

	
	private static final boolean DEBUG = false;
	
	private static final Logger LOGGER = Logger.getLogger(GenericXML.class
			.getCanonicalName());
	
	
	
	
	
	public String toXML(){
		String xmlString = "";
		
		try {
			JAXBContext jc = JAXBContext.newInstance( this.getClass() );
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();

			m.marshal( this, sw );
			xmlString = sw.toString();
			if(DEBUG) LOGGER.info("xmlString: " + xmlString);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.severe(e.getErrorCode() + ":" + e.getMessage());
			LOGGER.severe(e.getLocalizedMessage());
			StackTraceElement[] stack = e.getStackTrace();
			for(StackTraceElement el : stack){
				LOGGER.severe(el.getLineNumber() + ":" + el.toString());
			}
				
		}
		return xmlString;
	}
	
	
	public Element toElement(){
		Element el = null;
		//Node node = new Element(this.getClass().getName());
		
		//Node node = new org.w3c.dom.Element(this.getClass().getName());
		
		try {
			JAXBContext jc = JAXBContext.newInstance( this.getClass() );
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");

			StringWriter sw = new StringWriter();

			m.marshal( this, sw );
			
			if(DEBUG) LOGGER.info("Element:\n\n" + sw.toString());
			
			SAXBuilder builder = new SAXBuilder();
			//Document document = builder.build(new ByteArrayInputStream(sw.toString().getBytes()));
			
			//getBytes(AAEEModPlanas.ENC_UTF8)
			Document document = builder.build(new ByteArrayInputStream(sw.toString().getBytes(ConstantsREG.ENC_UTF8)));
			
			el = document.getRootElement();
			
			// Hay que separar el elemento del root para poder incluirlo en el arbol de otro documento
			el.detach();

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return el;
	}

}
