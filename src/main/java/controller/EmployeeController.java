package controller;


import dao.Dao;
import domain.BaseEntity;
import domain.Employee;
import exception.database.NoSuchEntityException;
import exception.response.ResponseMarshallingException;
import exception.validator.EmptyFieldException;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/employee")
public class EmployeeController extends AbstractController{

    @EJB(beanName = "EmployeeDaoBean")
    Dao employeeDao;


    @Override
    void getById (HttpServletRequest req, HttpServletResponse resp, UUID id) throws JAXBException, IOException {

        try {
            BaseEntity employee = employeeDao.getById(new Employee(), id);

            xmlMarshaller.sendMarshalledResponse(resp, employee);

        } catch (ResponseMarshallingException | NoSuchEntityException e) {
            logger.error(e);
        }
    }

    @Override
    void getAll (HttpServletRequest req, HttpServletResponse resp) throws JAXBException, IOException {

        try {
            List<BaseEntity> employees =  employeeDao.getAll(new Employee());

            if (employees.isEmpty()) return;

            xmlMarshaller.sendMarshalledResponse(resp, employees, new Employee().getClass());

        } catch (ResponseMarshallingException e) {
            logger.error(e);
        }
    }

    @Override
    void put(HttpServletRequest req, HttpServletResponse resp) throws JAXBException, IOException, NoSuchEntityException, ResponseMarshallingException, EmptyFieldException {

        Employee employeeFromRequest = new Employee();

        employeeFromRequest = (Employee) xmlMarshaller.getMarshalledRequest(req, employeeFromRequest);

        employeeDao.checkInputParams(employeeFromRequest);

        UUID id = employeeDao.put(employeeFromRequest);

        xmlMarshaller.sendMarshalledResponse(resp, employeeDao.getById(new Employee(), id));
    }

    @Override
    void post(HttpServletRequest req, HttpServletResponse resp) throws JAXBException, IOException, EmptyFieldException, NoSuchEntityException, ResponseMarshallingException {

        Employee employeeFromRequest = new Employee();

        employeeFromRequest = (Employee) xmlMarshaller.getMarshalledRequest(req, employeeFromRequest);

        employeeDao.checkInputParams(employeeFromRequest);

        BaseEntity employeeFromDB = employeeDao.getById(new Employee(), employeeFromRequest.getId());

        if (employeeFromDB == null) throw new NoSuchEntityException("No entity by such id");

        employeeDao.update(employeeFromRequest);

        xmlMarshaller.sendMarshalledResponse(resp, employeeDao.getById(new Employee(), employeeFromRequest.getId()));
    }

    @Override
    boolean delete (HttpServletRequest req){

        return employeeDao.delete(new Employee(), UUID.fromString(req.getParameter("id")));

    }
}
