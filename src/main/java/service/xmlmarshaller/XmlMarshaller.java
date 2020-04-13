package service.xmlmarshaller;

import exception.response.ResponseMarshallingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface XmlMarshaller <T> {
    void sendMarshalledResponse(HttpServletResponse resp, T entity) throws JAXBException, IOException, ResponseMarshallingException;

    void sendMarshalledResponse(HttpServletResponse resp, List<T> list, Class clazz) throws JAXBException, IOException, ResponseMarshallingException;

    T getMarshalledRequest(HttpServletRequest request, T entity) throws JAXBException, IOException;
}
