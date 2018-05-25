package autotest.dto.promin.request;

import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Setter
@XmlRootElement
public class User {

    @XmlAttribute
    private String login;
    @XmlAttribute
    private String password;
    @XmlAttribute
    private String auth;

}
