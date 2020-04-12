package controller;


import dao.Dao;
import domain.BaseEntity;
import domain.Employee;
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

@WebServlet(urlPatterns = "/employee")
public class EmployeeController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EmployeeController.class);

    @EJB(beanName = "EmployeeDaoBean")
    Dao employeeDao;

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

            Object employee = employeeDao.getById(new Employee().getClass(), id);
            xmlMarshaller.sendMarshalledResponse(resp, employee);

        } catch (ResponseMarshallingException | NoSuchEntityException e) {
            logger.error(e);
        }
    }

    void getAll (HttpServletRequest req, HttpServletResponse resp) throws JAXBException, IOException {

        try {
            List<BaseEntity> employees = employeeDao.getAll(new Employee().getClass());

            if (employees.isEmpty()) return;

            xmlMarshaller.sendMarshalledResponse(resp, employees, new Employee().getClass());

        } catch (ResponseMarshallingException e) {
            logger.error(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        logger.info("put request caught");
        req.setCharacterEncoding("UTF-8");

        try{
            Employee employeeFromRequest = new Employee();
            employeeFromRequest = (Employee) xmlMarshaller.doUnmarshall(req, employeeFromRequest);

            employeeDao.checkInputParams(employeeFromRequest);

            UUID id = employeeDao.put(new Employee().getClass(), employeeFromRequest);

            xmlMarshaller.sendMarshalledResponse(resp, employeeDao.getById(new Employee().getClass(), id));

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
            Employee employeeFromRequest = new Employee();
            employeeFromRequest = (Employee) xmlMarshaller.doUnmarshall(req, employeeFromRequest);

            employeeDao.checkInputParams(employeeFromRequest);

            BaseEntity employeeFromDB = employeeDao.getById(new Employee().getClass(), employeeFromRequest.getId());

            if (employeeFromDB == null) throw new NoSuchEntityException("No entity by such id");

            employeeDao.update(new Employee().getClass(), employeeFromRequest);
            xmlMarshaller.sendMarshalledResponse(resp, employeeDao.getById(new Employee().getClass(), employeeFromRequest.getId()));

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
                if (!employeeDao.delete(new Employee().getClass(), UUID.fromString(req.getParameter("id"))))
                    throw new NoSuchEntityException("No entity by such id");
            } else
                throw new EmptyFieldException("Empty id field");

        } catch (EmptyFieldException | NoSuchEntityException e) {
            logger.error(e);
        }
    }
}
