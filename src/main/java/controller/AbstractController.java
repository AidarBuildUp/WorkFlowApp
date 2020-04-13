package controller;

import exception.database.NoSuchEntityException;
import exception.response.ResponseMarshallingException;
import exception.validator.EmptyFieldException;
import org.apache.log4j.Logger;
import service.xmlmarshaller.XmlMarshaller;

import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public abstract class AbstractController extends HttpServlet {
    protected static final Logger logger = Logger.getLogger(AbstractController.class);

    @EJB
    XmlMarshaller xmlMarshaller;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {

        logger.info("get request caught");
        req.setCharacterEncoding("UTF-8");

        try {
            if (req.getParameter("id") != null) {
                logger.info("Request has an id parameter" + req.getParameter("id"));

                getById(req, resp, UUID.fromString(req.getParameter("id")));
            } else {
                logger.info("Request doesn't have id parameter");

                getAll(req, resp);
            }
        } catch (JAXBException | IOException e) {
            logger.error("error during marshalling", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("put request caught");
        req.setCharacterEncoding("UTF-8");

        try{

            put(req, resp);

        } catch (EmptyFieldException | NoSuchEntityException e){
            logger.error(e);

        } catch (JAXBException | ResponseMarshallingException e) {
            logger.error("Exception during unmarshalling request ", e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("post request caught");
        req.setCharacterEncoding("UTF-8");

        try{

            post (req, resp);

        } catch (EmptyFieldException | NoSuchEntityException e){
            logger.error(e);

        } catch (JAXBException | ResponseMarshallingException e) {
            logger.error("Exception during unmarshalling request ", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        req.setCharacterEncoding("UTF-8");

        try {
            if (req.getParameter("id") != null) {
                boolean deleted = delete(req);
                if (!deleted) throw new NoSuchEntityException("No entity by such id");
            } else
                throw new EmptyFieldException("Empty id field");

        } catch (EmptyFieldException | NoSuchEntityException e) {
            logger.error(e);
        }
    }

    abstract void getById (HttpServletRequest req, HttpServletResponse resp, UUID id) throws JAXBException, IOException;

    abstract void getAll (HttpServletRequest req, HttpServletResponse resp) throws JAXBException, IOException;

    abstract void put(HttpServletRequest req, HttpServletResponse resp) throws IOException, JAXBException, NoSuchEntityException, ResponseMarshallingException, EmptyFieldException;

    abstract void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, JAXBException, EmptyFieldException, NoSuchEntityException, ResponseMarshallingException;

    abstract boolean delete (HttpServletRequest req);
}