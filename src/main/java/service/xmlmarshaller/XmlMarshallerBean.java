package service.xmlmarshaller;

import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.util.List;

@Stateless
public class XmlMarshallerBean implements XmlMarshaller {

    @EJB (beanName = "Wrapper")
    Wrapper xmlWrapper;

    private static final Logger logger = Logger.getLogger(XmlMarshallerBean.class);

    @Override
    public void sendMarshalledResponse(HttpServletResponse resp, Object entity) throws JAXBException, IOException{
        logger.info("start");
        JAXBContext context = JAXBContext.newInstance(entity.getClass());
        Marshaller mar = context.createMarshaller();

        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        mar.marshal(entity, resp.getOutputStream());

        logger.info("success");
    }

    @Override
    public void sendMarshalledResponse(HttpServletResponse resp, List list, Class clazz) throws JAXBException, IOException {
        logger.info("start");

        JAXBContext jc = JAXBContext.newInstance(Wrapper.class, clazz);

        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        xmlWrapper.setItems(list);

        JAXBElement<Wrapper> wrapperJAXBElement = new JAXBElement<Wrapper>(new QName(clazz.getSimpleName().toLowerCase()+"s"), Wrapper.class, xmlWrapper);

        m.marshal(wrapperJAXBElement, resp.getOutputStream());

        logger.info("success");
    }

    @Override
    public Object getMarshalledRequest(HttpServletRequest request, Object entity) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(entity.getClass());

        return context.createUnmarshaller()
                .unmarshal(request.getInputStream());
    }
}
