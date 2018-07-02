package autotest.utils;

import autotest.entity.AuthData;
import autotest.utils.http.RestTemplateSetRequest;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import static com.codeborne.selenide.Selenide.*;

public class Utils {

    private final static Logger LOG = Logger.getLogger(Utils.class);
    private final static ConfigurationVariables CV = ConfigurationVariables.getInstance();

    //Поиск элемента XML по xPath-выражению
    public static String xPath (String xpathQuery, String xml)  {
        try{
            return XPathFactory
                    .newInstance()
                    .newXPath()
                    .evaluate(xpathQuery, (new InputSource(new StringReader(xml))) );
        } catch (XPathExpressionException e) {
            throw  new RuntimeException("Ошибка парсинга xPath.\n" + e.getMessage());
        }
    }


    public static String date(String formatDate){
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
        return sdf.format(Calendar.getInstance().getTime());
    }

    //Вернет дату в формате "05.05 Пт"
    public static String dateForFlightSearchResults(int days){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM E");
        return sdf.format(calendar.getTime()).toLowerCase();
    }

    public static String dateFormatted(String formatDate, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
        return sdf.format(calendar.getTime()).toLowerCase();
    }


    public static void switchFrame() {
        switchTo().defaultContent();
        $("[name=avia-widget-frame]").shouldBe(Condition.enabled);
        switchTo().frame("avia-widget-frame");
    }


    public static void waitUntilPreloaderRemove(SelenideElement element, int time) {
        int counter = 0;
        int pause = 2;
        int iterations = time / pause;

        while ( element.isDisplayed() ) {
            sleep(pause * 1000);
            counter++;
            if(counter > iterations) Assert.fail("Произошла ошибка (не пропал прелоадер), см. скриншот");
        }
    }

    public static int randomCl(){
        int min = 0, max = 199;
        return new Random().nextInt((max - min) + 1) + min;
    }


    public static String getArchiveUrl() {
        RestTemplateSetRequest restTemplateSetRequest = new RestTemplateSetRequest();
        String url = "https://bilet-dev.isto.it.loc/archive/frame/exsite/";
        String postBody ="data=%7B%22phone%22%3A%22%2B" + CV.phone.substring(1) + "%22%2C%22locale%22%3A%22ru%22%7D&frame=true&stage=0";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        ResponseEntity<String> responseEntity = null;
        try{
            responseEntity = (ResponseEntity<String>) restTemplateSetRequest.requestMethod(url, HttpMethod.POST, postBody, headers, String.class);
            return responseEntity.getHeaders().getFirst("Location");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения URL для перехода в архив билетов" + e.getMessage());
        }
    }

    @Step("Сторнирование билета {ticketId}")
    public static void stornBookedTicket(String ticketId) {
        LOG.info("Сторнируем " + ticketId + "...");
        RestTemplateSetRequest restTemplateSetRequest = new RestTemplateSetRequest();
        String url = String.format("https://bilet-dev.isto.it.loc/archive/order/create/%s/storno?csid=%s", ticketId, AuthData.getAuth_key());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "bilet-dev.isto.it.loc");
        headers.add("Accept", "application/json, text/plain, */*");
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.add("Cookie", "dep_sid=" + AuthData.getDep_sid() + "; auth_key=" + AuthData.getAuth_key() + ";");

        try {
            restTemplateSetRequest.requestMethodPost(url, null, headers, String.class);
        } catch (Exception e) {
            String msg = StringEscapeUtils.unescapeJava(e.getMessage());
            LOG.info("Ошибка выполнения запроса на сторнировку бронирования.\n" + msg);
               if (!msg.contains("была принята ранее"))
                   throw new RuntimeException("Ошибка выполнения запроса на сторнировку бронирования.\n" + msg);
        }
        LOG.info(ticketId + " Ok");
        sleep(5 * 1000);
    }

    public static void setCookieData(){
        Set<Cookie> cookies = WebDriverRunner.getWebDriver().manage().getCookies();
        AuthData.setCookies(cookies);
        cookies.forEach(cookie -> {
            if (cookie.getName().startsWith("auth_key")) AuthData.setAuth_key(cookie.getValue());
            if (cookie.getName().startsWith("dep_sid")) AuthData.setDep_sid(cookie.getValue());
            if (cookie.getName().equalsIgnoreCase("pa")) AuthData.setPa(cookie.getValue());
        });
    }
}
