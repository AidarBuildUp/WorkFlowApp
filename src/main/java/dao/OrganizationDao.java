package dao;

import domain.Organization;

import java.util.UUID;

public interface OrganizationDao {
    Organization get();
    UUID put(Organization organization);
    boolean update(Organization organization);
    boolean delete(UUID id);
}
