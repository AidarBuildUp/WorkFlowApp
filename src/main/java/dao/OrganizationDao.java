package dao;

import domain.Organization;

import java.sql.SQLException;

public interface OrganizationDao {
    Organization getOrganization();
    Integer putOrganization();
    boolean changeOrganization();
    boolean deleteOrganization();
}
