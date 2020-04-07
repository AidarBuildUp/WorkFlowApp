package service.repository.utils.initialize;

import exception.database.RepoInitProperyException;
import org.apache.log4j.Logger;
import service.repository.utils.initialize.property.PropertyEntity;
import service.repository.utils.initialize.property.RepoInitPropertyEntity;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class RepoInitPropertyImpl implements RepoInitProperty {

    private static final Logger logger = Logger.getLogger(RepoInitPropertyImpl.class);

    @Override
    public PropertyEntity getInitProperty() throws RepoInitProperyException {
        logger.info("start");

        RepoInitPropertyEntity repoInitPropertyEntity = getInitializeProperties();

        logger.info("success");

        if (repoInitPropertyEntity == null)
            throw new RepoInitProperyException("repository property file is not available");

        return repoInitPropertyEntity;
    }


    public RepoInitPropertyEntity getInitializeProperties(){
        try {
            logger.info("start binding");

            JAXBContext context = JAXBContext.newInstance(RepoInitPropertyEntity.class);

            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource("database.xml");
            if (resource == null) {
                throw new FileNotFoundException();
            } else {
                File file = new File (resource.getFile());
                return (RepoInitPropertyEntity) context.createUnmarshaller()
                        .unmarshal(file);
            }

        } catch (JAXBException e) {

            logger.error("JAXB binding exception. Check database.xml property file");
            logger.error(e.getStackTrace());

        } catch (FileNotFoundException e) {

            logger.error("Can not find database.xml file");
            logger.error(e.getStackTrace());
        }
        return null;
    }

}
