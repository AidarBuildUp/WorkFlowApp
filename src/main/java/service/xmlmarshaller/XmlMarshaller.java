package service.xmlmarshaller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface XmlMarshaller {
    void sendMarshalledResponse(HttpServletResponse resp, Object entity) throws JAXBException, IOException;

    Object doUnmarshall(HttpServletRequest request, Object entity) throws JAXBException, IOException;
}
