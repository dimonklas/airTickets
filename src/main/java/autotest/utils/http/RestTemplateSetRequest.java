package autotest.utils.http;

import org.apache.http.NameValuePair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;


@Component
public class RestTemplateSetRequest {


    private RestTemplateSettings restTemplateTrustCert = new RestTemplateSettings();

    /**
     * Метод отправки http запроса
     * @param url         - ссылка
     * @param httpMethod  - вид запроса (POST, GET, DELETE, и т. д.)
     * @param body        - тело запроса
     * @param headers     - заголовки
     * @param className   - возвращаемый обьект
     * @return
     */
    public ResponseEntity<?> requestMethod(String url, HttpMethod httpMethod, String body, HttpHeaders headers, Class className)  {
        try {
            return restTemplateTrustCert.restTemplateTrustCertificate().exchange(url, httpMethod, new HttpEntity<>(body, headers), className);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Метод отправки http POST запроса
     * @param url       - ссылка
     * @param body      - тело запроса
     * @param headers   - заголовки
     * @param className - возвращаемый обьект
     * @return
     */
    public Object requestMethodPost(String url, String body, HttpHeaders headers, Class className) {
        return restTemplateTrustCert.restTemplateTrustCertificate().postForObject(url, new HttpEntity<>(body, headers), className);
//        try {
//            return restTemplateTrustCert.restTemplateTrustCertificate().postForObject(url, new HttpEntity<>(body, headers), className);
//        }catch (Exception e){
//            throw new RuntimeException(e.getMessage());
//        }

    }

    /**
     * Метод отправки http GET запроса
     * @param url       - ссылка
     * @param className - возвращаемый обьект
     * @return
     */
    public Object requestMethodGet(String url, Class className) {
        try {
            return restTemplateTrustCert.restTemplateTrustCertificate().getForObject(url, className);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     *
     * @param url       - ссылка
     * @param headers   - заголовки
     * @param className - возвращаемый обьект
     * @param nvps      - Name value pair
     * @return
     */
    public ResponseEntity<?> requestMethodForEntity(String url, HttpHeaders headers, Class className, List<NameValuePair> nvps)  {
        try {
            return restTemplateTrustCert.restTemplateTrustCertificate().getForEntity(url, className, new HttpEntity<>(nvps, headers));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }



    public Object requestMethodPostFile(String url, String filePath, HttpHeaders headers, Class className) {
        File file;
        byte[] bytesArray = null;

        try(FileInputStream fileInputStream = new FileInputStream(file = new File(filePath))) {
            bytesArray = new byte[(int) file.length()];
            fileInputStream.read(bytesArray);
        } catch(Exception ex){
            ex.printStackTrace();
        }

        try {
            return restTemplateTrustCert.restTemplateTrustCertificate().postForObject(url, new HttpEntity<>(bytesArray, headers), className);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}