package autotest.entity;

import org.openqa.selenium.Cookie;

import java.util.Set;

public class AuthData {

    private static String dep_sid;
    private static String auth_key;
    private static String pa;
    private static Set<Cookie> cookies;


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

    public static String getPa() {
        return pa;
    }

    public static void setPa(String pa) {
        AuthData.pa = pa;
    }

    public static void setCookies(Set<Cookie> cooks){
        AuthData.cookies = cooks;
    }

    public static Set<Cookie> getCookies(){
        return cookies;
    }
}

