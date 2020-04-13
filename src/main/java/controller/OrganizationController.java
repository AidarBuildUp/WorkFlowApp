package controller;


import dao.Dao;
import domain.BaseEntity;
import domain.Organization;
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

@WebServlet(urlPatterns = "/organization")
public class OrganizationController extends AbstractController {

    @EJB (beanName = "OrganizationDaoBean")
    Dao organizationDao;

    @Override
    protected void getById (HttpServletRequest req, HttpServletResponse resp, UUID id) throws JAXBException, IOException {

        try {
            BaseEntity organization = organizationDao.getById(new Organization(), id);
            xmlMarshaller.sendMarshalledResponse(resp, organization);

        } catch (ResponseMarshallingException | NoSuchEntityException e) {
            logger.error(e);
        }
    }

    @Override
    protected void getAll (HttpServletRequest req, HttpServletResponse resp) throws JAXBException, IOException {

        try {
            List<BaseEntity> organizations = organizationDao.getAll(new Organization(), viewSettings);

            if (organizations.isEmpty()) return;

            xmlMarshaller.sendMarshalledResponse(resp, organizations, new Organization().getClass());

        } catch (ResponseMarshallingException e) {
            logger.error(e);
        }
    }

    @Override
    protected void put(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, JAXBException, NoSuchEntityException, ResponseMarshallingException, EmptyFieldException{

        Organization organizationFromRequest = new Organization();
        organizationFromRequest = (Organization) xmlMarshaller.getMarshalledRequest(req, organizationFromRequest);

        checkInputParams(organizationFromRequest);

        BaseEntity organizationFromDB = organizationDao.getById(new Organization(), organizationFromRequest.getId());

        if (organizationFromDB == null) throw new NoSuchEntityException("No entity by such id");

        organizationDao.update(organizationFromRequest);

        xmlMarshaller.sendMarshalledResponse(resp, organizationDao.getById(new Organization(), organizationFromRequest.getId()));
    }


    @Override
    protected void post(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, JAXBException, EmptyFieldException, NoSuchEntityException, ResponseMarshallingException{

        Organization organizationFromRequest = new Organization();

        organizationFromRequest = (Organization) xmlMarshaller.getMarshalledRequest(req, organizationFromRequest);

        checkInputParams(organizationFromRequest);

        UUID id = organizationDao.put(organizationFromRequest);

        xmlMarshaller.sendMarshalledResponse(resp, organizationDao.getById(new Organization(), id));
    }


    @Override
    protected boolean delete(HttpServletRequest req){

        return organizationDao.delete(new Organization(), UUID.fromString(req.getParameter("id")));
    }

    @Override
    public void checkInputParams(BaseEntity entity) throws EmptyFieldException {
        Organization organization = (Organization) entity;

        if ( (organization != null) && (organization.getName().isEmpty()) ||
                (organization.getPhysicalAddress().isEmpty()) || (organization.getLegalAddress().isEmpty())
                || (organization.getManager() == null) ) {
            throw new EmptyFieldException("Empty fields in required fields");
        }
    }
}
