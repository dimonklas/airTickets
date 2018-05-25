package autotest.bl;


import autotest.dto.promin.Filler;
import autotest.dto.promin.request.Session;
import autotest.dto.promin.response.Id;
import autotest.utils.ConfigurationVariables;
import autotest.utils.JaxbGsonUtils;
import autotest.utils.exception.AuthentificationException;
import autotest.utils.http.RestTemplateSetRequest;
import org.springframework.http.HttpHeaders;
import org.testng.Assert;

import javax.xml.bind.JAXBException;

public class ProminSessionBL {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    private RestTemplateSetRequest restTemplateSetRequest = new RestTemplateSetRequest();
    private Filler filler = new Filler();
    private JaxbGsonUtils jaxbGsonUtils = new JaxbGsonUtils();


    public String getProminSession() throws JAXBException {
        String url = CV.urlPromin;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/xml;charset=UTF-8");
        headers.add("Accept", "application/xml");
        headers.add("host", "promin.stage.it.loc");

        Session session = filler.fillSessionRequest(CV.userLogin, CV.userPassword);
        String body = jaxbGsonUtils.objectToXml(session, Session.class);
        Id result = null;
        try{
            result =  (Id) restTemplateSetRequest.requestMethodPost(url, body, headers, Id.class);
        } catch (Exception e) {
            throw new AuthentificationException("Ошибка получения сессии проминя\n" + e.getMessage());
        }

        Assert.assertNotNull(result, "Ошибка получения сессии проминя");
        return result.getValue();
    }

}
