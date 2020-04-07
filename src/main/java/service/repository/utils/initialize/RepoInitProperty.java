package service.repository.utils.initialize;

import service.repository.utils.initialize.property.PropertyEntity;
import exception.database.RepoInitProperyException;

public interface RepoInitProperty {
    PropertyEntity getInitProperty() throws RepoInitProperyException;
}
