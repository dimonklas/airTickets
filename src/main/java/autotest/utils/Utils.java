package autotest.utils;

import autotest.entity.AuthData;
import autotest.utils.http.RestTemplateSetRequest;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.springframework.http.HttpHeaders;
import org.testng.Assert;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Selenide.*;

public class Utils {

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


    public static void stornBookedTicket(String ticketId){
        RestTemplateSetRequest restTemplateSetRequest = new RestTemplateSetRequest();
        String url = String.format("https://bilet-dev.isto.it.loc/archive/order/create/%s/storno?csid=%s", ticketId, AuthData.getAuth_key());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "bilet-dev.isto.it.loc");
        headers.add("Accept", "application/json, text/plain, */*");
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.add("Cookie", String.format("dep_sid=%s; auth_key=%s;", AuthData.getDep_sid(), AuthData.getAuth_key()));

        restTemplateSetRequest.requestMethodPost(url, null, headers, String.class);
    }

    public static void setCookieData(){
        sleep(1000);
        AuthData.setCookies(WebDriverRunner.getWebDriver().manage().getCookies());
        AuthData.setAuth_key(WebDriverRunner.getWebDriver().manage().getCookieNamed("auth_key").getValue());
        AuthData.setDep_sid(WebDriverRunner.getWebDriver().manage().getCookieNamed("dep_sid").getValue());
    }
}
