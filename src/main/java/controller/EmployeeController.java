package controller;


import dao.employee.EmployeeDao;
import dao.organization.OrganizationDao;
import domain.Employee;
import domain.Organization;
import exception.validator.EmptyFieldException;
import org.apache.log4j.Logger;
import service.xmlmarshaller.XmlMarshaller;

import javax.ejb.EJB;
import javax.ejb.NoSuchEntityException;
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

@WebServlet(urlPatterns = "/employee?id")
public class EmployeeController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EmployeeController.class);

    @EJB
    EmployeeDao employeeDao;

    @EJB
    XmlMarshaller xmlMarshaller;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, UnsupportedEncodingException {
        logger.info("get request caught");
        req.setCharacterEncoding("UTF-8");

        try {
            UUID id = UUID.fromString( req.getParameter("id") );
            if (id.equals(null)) throw new EmptyFieldException("id parameter missed");
            Employee employee = employeeDao.getById(id);

            xmlMarshaller.sendMarshalledResponse(resp, employee);

        } catch (JAXBException | IOException e) {
            logger.error("error during marshalling", e);
        } catch (EmptyFieldException e) {
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("post request caught");
        req.setCharacterEncoding("UTF-8");

        try{
            Employee employeeFromRequest = new Employee();
            employeeFromRequest = (Employee) xmlMarshaller.doUnmarshall(req, employeeFromRequest);
            checkInputParams(employeeFromRequest);
            Employee employeeFromDB = employeeDao.getById(employeeFromRequest.getId());

            if (employeeFromDB == null) throw new NoSuchEntityException();

            employeeDao.update(employeeFromRequest);
            xmlMarshaller.sendMarshalledResponse(resp, employeeDao.getById(employeeFromRequest.getId()));

        } catch (EmptyFieldException | NoSuchEntityException e){
            logger.error(e);
            errorMessage(resp, e);

        } catch (JAXBException e) {
            logger.error("Exception during unmarshalling request ", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            if (req.getParameter("id") != null) {
                if (!employeeDao.delete(UUID.fromString(req.getParameter("id"))))
                    throw new NoSuchEntityException();
            } else
                throw new EmptyFieldException("Empty id field");

        } catch (EmptyFieldException | NoSuchEntityException e) {
            logger.error(e);
        }
    }

    private void checkInputParams (Employee employee) throws EmptyFieldException {
        if ( (employee != null) && (employee.getFirstName().isEmpty()) ||
        (employee.getSecondName().isEmpty()) || (employee.getPatronymicName().isEmpty()) ||
                (employee.getOrganization().getId().equals(null))) {
            throw new EmptyFieldException("Empty fields in required fields");
        }
    }

    private void errorMessage(HttpServletResponse resp, Exception e) {
        try {
            PrintWriter writer = resp.getWriter();
            writer.println(e.getMessage());

        } catch (IOException ex) {
            logger.error(e);
        }
    }
}
