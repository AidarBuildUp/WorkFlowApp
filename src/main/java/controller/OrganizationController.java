package controller;


import dao.OrganizationDao;
import domain.Organization;
import org.apache.log4j.Logger;
import service.repository.utils.initialize.RepoInitializer;
import service.repository.utils.initialize.RepoInitializerIml;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("get request caught");

        Organization organization = organizationDao.getOrganization();

        try {
            xmlMarshaller.doMarshall(resp, organization);

        } catch (JAXBException e) {
            logger.error("error during marshalling");
            logger.info(e.getStackTrace());
        }
        return;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
