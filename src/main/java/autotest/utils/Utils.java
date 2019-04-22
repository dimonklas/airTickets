package autotest.utils;

import autotest.dto.custData.ClientDataItem;
import autotest.entity.AuthData;
import autotest.entity.TicketData;
import autotest.pages.ArchivePage;
import autotest.utils.http.RestTemplateSetRequest;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import static com.codeborne.selenide.Condition.visible;
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
    public static String getDateForFlightSearchResults(int daysFromToday){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, daysFromToday);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM E");
        return sdf.format(calendar.getTime()).toLowerCase();
    }

    public static String dateFormatted(String formatDate, int daysFromToday){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, daysFromToday);
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

    public static String getArchiveUrl(String phoneNum) {
        if (phoneNum.startsWith("+")) phoneNum = phoneNum.substring(1);
        RestTemplateSetRequest restTemplateSetRequest = new RestTemplateSetRequest();
        String url = "https://" + CV.urlBase + "/archive/frame/exsite/";
        String postBody ="data=%7B%22phone%22%3A%22%2B" + phoneNum + "%22%2C%22locale%22%3A%22ru%22%7D&frame=true&stage=0";

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
        String url = String.format("https://%s/archive/order/create/%s/storno?csid=%s", CV.urlBase, ticketId, AuthData.getAuth_key());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", CV.urlBase);
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

    @Step("Дождемся смены статуса в архиве и проверим его значение")
    public static void waitAndCheckForBookingStatusChanged(String bookingCode, String expStatus){
        String xPathStatus = String.format(".//*[text()='%s']/../*[@data-ng-bind='ticket.status_text']", bookingCode);
        String actStatus = $(By.xpath(xPathStatus)).shouldBe(visible).getText().trim();
        int counter = 0;
        while (!expStatus.equalsIgnoreCase(actStatus) && counter < 5) {
            sleep(10 * 1000);
            refresh();
            ArchivePage.waitForArchivePageLoad();
            actStatus = $(By.xpath(xPathStatus)).shouldBe(visible).getText().trim();
            counter++;
        }

        Assert.assertEquals(actStatus, expStatus, "Не дождались смены статуса в архиве");
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

    public static void waitUntilFileDownload(String fileName){
        File file = new File(CV.downloadsDir + fileName);
        int counter = 0;
        while (!file.exists() && counter <= 10) {
            sleep(2 * 1000);
            counter++;
        }
        sleep(3*1000);
    }

    public static String pdfToString(String fileName) {
        try {
            return PdfTextExtractor.getTextFromPage(new PdfReader(CV.downloadsDir + fileName), 1);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла " + fileName + "\n" + e.getMessage());
        }
    }

    public static String docToString(String fileName){
        try {
            FileInputStream fis = new FileInputStream(CV.downloadsDir + fileName);
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            return extractor.getText();
        } catch(Exception e) {
            throw new RuntimeException("Ошибка чтения файла " + fileName + "\n" + e.getMessage());
        }
    }

    public static TicketData getTicketDataByLastName(String lastName, String bookingCode){
        ClientDataItem clientDataItem = CV.clientData.get(0);
        boolean find = false;
        int counter = 1;
        while (!find && counter < CV.clientData.size()-2) {
            if (clientDataItem.getLastName().equalsIgnoreCase(lastName)) {
                find = true;
            } else clientDataItem = CV.clientData.get(counter);
            counter++;
        }

        ClientDataItem cdi = clientDataItem;
        return new TicketData(t -> {
            t.setOwnerFIO(cdi.getLastName().toUpperCase() + " " + cdi.getFirstName().toUpperCase());
            t.setClientDataItem(cdi);
            t.setBookingId(bookingCode);
        });
    }

    public static void closeTabAfterOpenArchivePage() {
        if (WebDriverRunner.getWebDriver().getWindowHandles().size() > 1) {
            switchTo().window(1).close();
        }
    }
}
