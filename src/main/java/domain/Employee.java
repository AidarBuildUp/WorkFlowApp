package domain;

import javax.xml.bind.annotation.*;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee extends BaseEntity {

    @XmlElement
    private UUID id;

    @XmlElement
    private String firstName;

    @XmlElement
    private String secondName;

    @XmlElement
    private String patronymicName;

    @XmlElement
    private String function;

    public Employee() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", patronymicName='" + patronymicName + '\'' +
                ", function='" + function + '\'' +
                '}';
    }
}
