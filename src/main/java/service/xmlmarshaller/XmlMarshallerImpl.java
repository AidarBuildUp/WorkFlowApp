package service.xmlmarshaller;

import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;

@Stateless
public class XmlMarshallerImpl implements XmlMarshaller {

    private static final Logger logger = Logger.getLogger(XmlMarshallerImpl.class);

    public void doMarshall(HttpServletResponse resp, Object entity) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(entity.getClass());
        Marshaller mar= context.createMarshaller();

        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(entity,resp.getOutputStream());
    }
}
