package controller;


import dao.employee.EmployeeDao;
import domain.Employee;
import exception.database.NoEntitiesInDatabase;
import exception.validator.EmptyFieldException;
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
import java.util.Set;
import java.util.UUID;

@WebServlet(urlPatterns = "/employees")
public class EmployeesController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EmployeesController.class);

    @EJB
    EmployeeDao employeeDao;

    @EJB
    XmlMarshaller xmlMarshaller;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, UnsupportedEncodingException {
        logger.info("get request caught");
        req.setCharacterEncoding("UTF-8");

        Set<Employee> employees = employeeDao.getAll();

        try {

            if (employees.isEmpty()) throw new NoEntitiesInDatabase();

            xmlMarshaller.sendMarshalledResponse(resp, employees);

        } catch (JAXBException | IOException e) {
            logger.error("error during marshalling", e);
        } catch (NoEntitiesInDatabase e) {
            logger.error("looks like no employes in the organization ", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("put request caught");
        req.setCharacterEncoding("UTF-8");

        try{
            Employee employeeFromRequest = new Employee();
            employeeFromRequest = (Employee) xmlMarshaller.doUnmarshall(req, employeeFromRequest);

            checkInputParams(employeeFromRequest);

            UUID id = employeeDao.put(employeeFromRequest);

            xmlMarshaller.sendMarshalledResponse(resp, employeeDao.getById(id));

        } catch (EmptyFieldException e){
            logger.error(e);
            errorMessage(resp, e);

        } catch (JAXBException e) {
            logger.error("Exception during unmarshalling request ", e);
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
