package autotest.utils;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
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

    public static String dateForFlightSearchResults(int days){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM E");
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


}
