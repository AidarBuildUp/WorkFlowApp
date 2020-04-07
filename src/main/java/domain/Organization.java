package domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.UUID;

@XmlRootElement()
public class Organization {

    private UUID id;

    private String name;

    private String physicalAddress;

    private String legalAddress;

    private Employee manager;

    public Organization() {
    }

    public UUID getId() {
        return id;
    }
    @XmlElement
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    @XmlElement
    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    @XmlElement
    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public Employee getManager() {
        return manager;
    }

    @XmlElement
    public void setManager(Employee manager) {
        this.manager = manager;
    }
}
