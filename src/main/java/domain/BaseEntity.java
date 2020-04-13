package domain;

import javax.xml.bind.annotation.*;
import java.util.UUID;

public class BaseEntity {

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
