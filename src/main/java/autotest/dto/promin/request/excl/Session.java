package autotest.dto.promin.request.excl;

import autotest.dto.promin.request.User;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@Setter
@Getter
@XmlRootElement
public class Session {
    private User user;
}
