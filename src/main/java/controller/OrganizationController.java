package controller;


import dao.Dao;
import dao.OrganizationDaoBean;
import domain.BaseEntity;
import domain.Organization;
import exception.database.NoSuchEntityException;
import exception.response.ResponseMarshallingException;
import exception.validator.EmptyFieldException;
import org.apache.log4j.Logger;
import service.xmlmarshaller.XmlMarshaller;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/organization", loadOnStartup = 1)
public class OrganizationController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(OrganizationController.class);

    @EJB (beanName = "OrganizationDaoBean")
    Dao organizationDao;

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

    void getById (HttpServletRequest req, HttpServletResponse resp, UUID id) throws JAXBException, IOException {

        try {

            Object employee = organizationDao.getById(new Organization().getClass(), id);
            xmlMarshaller.sendMarshalledResponse(resp, employee);

        } catch (ResponseMarshallingException | exception.database.NoSuchEntityException e) {
            logger.error(e);
        }
    }

    void getAll (HttpServletRequest req, HttpServletResponse resp) throws JAXBException, IOException {

        try {
            List<BaseEntity> organizations = organizationDao.getAll(new Organization().getClass());

            if (organizations.isEmpty()) return;

            xmlMarshaller.sendMarshalledResponse(resp, organizations, new Organization().getClass());

        } catch (ResponseMarshallingException e) {
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("post request caught");
        req.setCharacterEncoding("UTF-8");

        try{
            Organization organizationFromRequest = new Organization();
            organizationFromRequest = (Organization) xmlMarshaller.doUnmarshall(req, organizationFromRequest);

            organizationDao.checkInputParams(organizationFromRequest);

            UUID id = organizationDao.put(new Organization().getClass(), organizationFromRequest);

            xmlMarshaller.sendMarshalledResponse(resp, organizationDao.getById(new Organization().getClass(), id));

        } catch (EmptyFieldException | exception.database.NoSuchEntityException e){
            logger.error(e);

        } catch (JAXBException | ResponseMarshallingException e) {
            logger.error("Exception during unmarshalling request ", e);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("put request caught");
        req.setCharacterEncoding("UTF-8");

        try{
            Organization organizationFromRequest = new Organization();
            organizationFromRequest = (Organization) xmlMarshaller.doUnmarshall(req, organizationFromRequest);

            organizationDao.checkInputParams(organizationFromRequest);

            BaseEntity employeeFromDB = organizationDao.getById(new Organization().getClass(), organizationFromRequest.getId());

            if (employeeFromDB == null) throw new NoSuchEntityException("No entity by such id");

            organizationDao.update(new Organization().getClass(), organizationFromRequest);
            xmlMarshaller.sendMarshalledResponse(resp, organizationDao.getById(new Organization().getClass(), organizationFromRequest.getId()));

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
                organizationDao.delete(new Organization().getClass(), UUID.fromString(req.getParameter("id")));

            } else
                throw new EmptyFieldException("Empty id field");

        } catch (EmptyFieldException e) {
            logger.error(e);
        }
    }
}
