package controller;


import dao.OrganizationDao;
import domain.Organization;
import exception.validator.EmptyFieldException;
import org.apache.log4j.Logger;
import service.xmlmarshaller.XmlMarshaller;

import javax.ejb.EJB;
import javax.ejb.NoSuchEntityException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@WebServlet(urlPatterns = "/organization", loadOnStartup = 1)
public class OrganizationController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(OrganizationController.class);

    @EJB
    OrganizationDao organizationDao;

    @EJB
    XmlMarshaller xmlMarshaller;

//    @Override
//    public void init() throws ServletException {
//
//        logger.info("init method in OrganizationController started");
//        RepoInitializer repoInitializer = new RepoInitializerIml();
//        if (!repoInitializer.initialize()) {
//            throw new ServletException("Error during repository initialize. Servlet loading breaked");
//        }
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, UnsupportedEncodingException {
        logger.info("get request caught");
        req.setCharacterEncoding("UTF-8");

        Organization organization = organizationDao.get();

        try {
            xmlMarshaller.sendMarshalledResponse(resp, organization);

        } catch (JAXBException | IOException e) {
            logger.error("error during marshalling", e);
        }
        return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("post request caught");
        req.setCharacterEncoding("UTF-8");

        try{
            Organization organizationFromDB = organizationDao.get();
            Organization organizationFromRequest = new Organization();
            organizationFromRequest = (Organization) xmlMarshaller.doUnmarshall(req, organizationFromRequest);

            checkInputParams(organizationFromRequest);

            if (!organizationFromDB.getId().equals(organizationFromRequest.getId())) {
                throw new NoSuchEntityException("No such organization in database");
            }

            organizationDao.update(organizationFromRequest);
            xmlMarshaller.sendMarshalledResponse(resp, organizationDao.get());

        } catch (EmptyFieldException | NoSuchEntityException e){
            logger.error(e);
            errorMessage(resp, e);

        } catch (JAXBException e) {
            logger.error("Exception during unmarshalling request ", e);
        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        logger.info("put request caught");
        req.setCharacterEncoding("UTF-8");

        try{
            Organization organizationFromRequest = new Organization();
            organizationFromRequest = (Organization) xmlMarshaller.doUnmarshall(req, organizationFromRequest);

            checkInputParams(organizationFromRequest);

            organizationDao.put(organizationFromRequest);

            xmlMarshaller.sendMarshalledResponse(resp, organizationDao.get());

        } catch (EmptyFieldException e){
            logger.error(e);
            errorMessage(resp, e);

        } catch (JAXBException e) {
            logger.error("Exception during unmarshalling request ", e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            if (req.getParameter("id") != null) {
                organizationDao.delete(UUID.fromString(req.getParameter("id")));

            } else
                throw new EmptyFieldException("Empty id field");

        } catch (EmptyFieldException e) {
            logger.error(e);
        }
    }

    private void checkInputParams (Organization organization) throws EmptyFieldException {
        if ( (organization != null) && (organization.getName().isEmpty()) ||
        (organization.getPhysicalAddress().isEmpty()) || (organization.getLegalAddress().isEmpty()) ) {
            throw new EmptyFieldException("Empty fields in required fields");
        }
    }

    private void errorMessage(HttpServletResponse resp, Exception e) {
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
            writer.println(e.getMessage());

        } catch (IOException ex) {
            logger.error(e);
        }
    }
}
