package service.xmlmarshaller;

import javax.ejb.Stateless;
import javax.xml.bind.annotation.XmlAnyElement;
import java.util.LinkedList;
import java.util.List;

@Stateless (name = "Wrapper")
public class Wrapper <T> {

    private List<T> items = new LinkedList<>();

    @XmlAnyElement(lax=true)
    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {

        this.items = items;

    }
}
