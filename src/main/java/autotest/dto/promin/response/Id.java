package autotest.dto.promin.response;


import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Setter
@XmlRootElement
public class Id {

    @XmlAttribute(name = "value")
    public String getValue() {
        return value;
    }

    private String value;
}
