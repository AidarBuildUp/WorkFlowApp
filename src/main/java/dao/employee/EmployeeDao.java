package dao.employee;

import domain.Employee;

import java.util.Set;
import java.util.UUID;

public interface EmployeeDao {
    Set <Employee> getAll();
    Employee getById(UUID id);
    UUID put(Employee employee);
    boolean update(Employee employee);
    boolean delete(UUID id);
}
