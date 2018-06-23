package autotest.entity;

import lombok.Data;
import org.openqa.selenium.Cookie;

import java.util.Set;

@Data
public class AuthData {

    private static String dep_sid;
    private static String auth_key;
    private static Set<Cookie> cookies;
    private static Cookie dep_sid_cookie;
    private static Cookie auth_key_cookie;

    public static String getDep_sid() {
        return dep_sid;
    }

    public static void setDep_sid(String dep_sid) {
        AuthData.dep_sid = dep_sid;
    }

    public static String getAuth_key() {
        return auth_key;
    }

    public static void setAuth_key(String auth_key) {
        AuthData.auth_key = auth_key;
    }

    public static void setCookies(Set<Cookie> cooks){
        AuthData.cookies = cooks;
    }

    public static Set<Cookie> getCookies(){
        return cookies;
    }
}

