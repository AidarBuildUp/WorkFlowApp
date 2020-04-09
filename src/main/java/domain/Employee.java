package domain;

import domain.helperClasses.EmployeeFunctions;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.UUID;

@XmlRootElement
public class Employee {

    private UUID id;

    private String firstName;

    private String secondName;

    private String patronymicName;

    private EmployeeFunctions employeeFunctions;

    private Department department;

    private Organization organization;

    public Employee() {
    }

    public UUID getId() {
        return id;
    }
    @XmlElement
    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    @XmlElement
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    @XmlElement
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPatronymicName() {
        return patronymicName;
    }

    @XmlElement
    public void setPatronymicName(String patronymicName) {
        this.patronymicName = patronymicName;
    }

    public EmployeeFunctions getEmployeeFunctions() {
        return employeeFunctions;
    }

    @XmlTransient
    public void setEmployeeFunctions(EmployeeFunctions employeeFunctions) {
        this.employeeFunctions = employeeFunctions;
    }

    public Department getDepartment() {
        return department;
    }

    @XmlTransient
    public void setDepartment(Department department) {
        this.department = department;
    }

    public Organization getOrganization() {
        return organization;
    }

    @XmlElement
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
