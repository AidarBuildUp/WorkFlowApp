package dao.organization;

import domain.Organization;

import javax.ejb.Local;
import java.util.UUID;

public interface OrganizationDao {
    Organization get();
    UUID put(Organization organization);
    boolean update(Organization organization);
    boolean delete(UUID id);
}
