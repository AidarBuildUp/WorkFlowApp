package controller;


import dao.OrganizationDao;
import domain.Organization;
import exception.response.ResponseMarshallingException;
import exception.validator.NullFieldException;
import org.apache.log4j.Logger;
import service.xmlmarshaller.XmlMarshaller;

import javax.ejb.EJB;
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
            xmlMarshaller.doMarshall(resp, organization);

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
            Organization organization = organizationDao.get();
            String [] paramValues = checkInputParams(req);

            organization.setName(paramValues[0]);
            organization.setPhysicalAddress(paramValues[1]);
            organization.setLegalAddress(paramValues[2]);
            if (req.getParameter("id") != null) {
                organization.setId(UUID.fromString(req.getParameter("id")));
            } else throw new NullFieldException("Empty id field");

            organizationDao.update(organization);

            sendResponse(resp, organization);

        } catch (NullFieldException | ResponseMarshallingException e){
            logger.error(e);
            errorMessage(resp, e);
        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("put request caught");
        req.setCharacterEncoding("UTF-8");

        try{
            String [] paramValues = checkInputParams(req);
            Organization organization = new Organization();
            organization.setName(paramValues[0]);
            organization.setPhysicalAddress(paramValues[1]);
            organization.setLegalAddress(paramValues[2]);
            organization.setId (organizationDao.put(organization));

            sendResponse(resp, organization);

        } catch (NullFieldException | ResponseMarshallingException e){
            logger.error(e);
            errorMessage(resp, e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            if (req.getParameter("id") != null) {
                organizationDao.delete(UUID.fromString(req.getParameter("id")));

            } else
                throw new NullFieldException("Empty id field");

        } catch (NullFieldException e) {
            logger.error(e);
        }
    }

    private final String [] inputParams = {"name", "physicalAddress", "legalAddress"}; //TODO add manager field
    private String[] checkInputParams (HttpServletRequest request) throws NullFieldException {
        String [] notNullParamsValue = new String[inputParams.length];
        for (int i = 0; i < inputParams.length; i++) {
            if (request.getParameter(inputParams[i]) != null){
                notNullParamsValue[i] = request.getParameter(inputParams[i]);
            } else {
                throw new NullFieldException("Null in not null requested field " + inputParams[i]);
            }
        }
        return notNullParamsValue;
    }

    private void sendResponse(HttpServletResponse resp, Organization organization) throws ResponseMarshallingException {
        try {
            xmlMarshaller.doMarshall(resp, organization);

        } catch (JAXBException | IOException e) {
            logger.error("error during marshalling", e);
            throw new ResponseMarshallingException();
        }
    }

    private void errorMessage(HttpServletResponse resp, Exception e) {
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
            writer.println(e.getMessage());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
