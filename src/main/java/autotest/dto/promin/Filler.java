package autotest.dto.promin;

import autotest.dto.promin.request.Session;
import autotest.dto.promin.request.User;

public class Filler {

    public Session fillSessionRequest(String login, String adminSid) {
        Session session = new Session();
        User user = new User();

        user.setAuth("LDAP");
        user.setLogin(login);

        session.setUser(user);
        session.setAdminSid(adminSid);
        session.setState("ACTIVE");

        return session;
    }


    public autotest.dto.promin.request.excl.Session fillAdminSessionRequest(String techLogin, String techPassword){
        autotest.dto.promin.request.excl.Session session = new autotest.dto.promin.request.excl.Session();
        User user = new User();

        user.setAuth("EXCL");
        user.setLogin(techLogin);
        user.setPassword(techPassword);
        session.setUser(user);

        return session;
    }

}
