package autotest.dto.promin;

import autotest.dto.promin.request.Session;
import autotest.dto.promin.request.User;

public class Filler {

    public Session fillSessionRequest(String login, String password) {
        Session session = new Session();
        User user = new User();

        user.setAuth("LDAP");
        user.setLogin(login);
        user.setPassword(password);

        session.setUser(user);

        return session;
    }

}
