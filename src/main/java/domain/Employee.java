package domain;

import domain.helperClasses.EmployeeFunctions;


public class Employee {

    private Long id;

    private String firstName;

    private String secondName;

    private String patronymicName;

    private EmployeeFunctions employeeFunctions;

    private Department department;

    private Organization organization;

    public Employee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPatronymicName() {
        return patronymicName;
    }

    public void setPatronymicName(String patronymicName) {
        this.patronymicName = patronymicName;
    }

    public EmployeeFunctions getEmployeeFunctions() {
        return employeeFunctions;
    }

    public void setEmployeeFunctions(EmployeeFunctions employeeFunctions) {
        this.employeeFunctions = employeeFunctions;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
