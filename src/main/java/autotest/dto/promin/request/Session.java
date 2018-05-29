package autotest.dto.promin.request;

import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Setter
@XmlRootElement
public class Session {
    private User user;

    public User getUser() {
        return user;
    }


    @XmlAttribute
    private String adminSid;
    @XmlAttribute
    private String state;
}