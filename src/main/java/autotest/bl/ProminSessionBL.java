package autotest.bl;


import autotest.dto.promin.Filler;
import autotest.dto.promin.request.Session;
import autotest.dto.promin.response.Id;
import autotest.utils.ConfigurationVariables;
import autotest.utils.JaxbGsonUtils;
import autotest.utils.exception.AuthenticationException;
import autotest.utils.http.RestTemplateSetRequest;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpHeaders;

import javax.xml.bind.JAXBException;

@Log4j
public class ProminSessionBL {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    private RestTemplateSetRequest restTemplateSetRequest = new RestTemplateSetRequest();
    private Filler filler = new Filler();
    private JaxbGsonUtils jaxbGsonUtils = new JaxbGsonUtils();

    private final String URL_PROMIN = CV.urlPromin;


    /*
    * Получает активированную сессию сотрудника с помощью админской сессии
    * */
    public String getProminSession() throws JAXBException, AuthenticationException {
        Session session = filler.fillSessionRequest(CV.userLogin, getAdminSession());
        String body = jaxbGsonUtils.objectToXml(session, Session.class);
        Id result;
        try{
            result =  (Id) restTemplateSetRequest.requestMethodPost(URL_PROMIN, body, prominHeaders(), Id.class);
        } catch (Exception e) {
            log.error("Ошибка получения LDAP сессии проминя");
            log.error(e.toString());
            log.error("Сервер авторизации: " + URL_PROMIN + "   Логин: " + CV.techLogin);
            log.error("Прогон тестов отстановлен, отчет сформирован не будет");
            throw new AuthenticationException("Ошибка получения LDAP сессии проминя\n" + e.toString() +
                    "\nСервер авторизации: " + URL_PROMIN + "\n Логин: " + CV.userLogin);
        }

        return result.getValue().trim();
    }

    /*
    * Метод для получения админской сессии проминя
    * */
    private String getAdminSession() throws JAXBException, AuthenticationException {
        autotest.dto.promin.request.excl.Session session = filler.fillAdminSessionRequest(CV.techLogin, CV.techPassword);
        String body = jaxbGsonUtils.objectToXml(session, autotest.dto.promin.request.excl.Session.class);
        Id result;
        try{
            result =  (Id) restTemplateSetRequest.requestMethodPost(URL_PROMIN, body, prominHeaders(), Id.class);
        } catch (Exception e) {
            log.error("Ошибка получения EXCL сессии проминя");
            log.error(e.toString());
            log.error("Сервер авторизации: " + URL_PROMIN + "   Логин: " + CV.techLogin);
            log.error("Прогон тестов отстановлен, отчет сформирован не будет");
            throw new AuthenticationException("Ошибка получения EXCL сессии проминя\n" + e.toString() +
                    "\nСервер авторизации: " + URL_PROMIN + "\n Логин: " + CV.techLogin);
        }

        return result.getValue().trim();
    }

    /*
    * Хидеры для запросов на сервер проминя
    * */
    private HttpHeaders prominHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/xml;charset=UTF-8");
        headers.add("Accept", "application/xml");
        headers.add("host", "promin.stage.it.loc");

        return headers;
    }

}
