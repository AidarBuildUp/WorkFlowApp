package dao;

import domain.Organization;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface OrganizationDao {
    Organization get();
    UUID put(Organization organization);
    boolean update(Organization organization);
    boolean delete(Integer id);
}
