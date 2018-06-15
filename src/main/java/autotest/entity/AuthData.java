package autotest.entity;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.Cookie;

import java.util.Set;

@Getter
@Setter
public class AuthData {

    private String dep_sid;
    private String auth_key;
    Set<Cookie> cookies;
    private Cookie dep_sid_cookie;
    private Cookie auth_key_cookie;


}
