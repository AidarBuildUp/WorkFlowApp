package service.xmlmarshaller;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface XmlMarshaller {
    void doMarshall(HttpServletResponse resp, Object entity) throws JAXBException, IOException;
}
