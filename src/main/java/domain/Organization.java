package domain;

import javax.xml.bind.annotation.*;
import java.util.UUID;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class Organization extends BaseEntity {

    @XmlElement
    private UUID id;

    @XmlElement
    private String name;

    @XmlElement
    private String physicalAddress;

    @XmlElement
    private String legalAddress;

    @XmlElement
    private Employee manager;

    public Organization() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
}
