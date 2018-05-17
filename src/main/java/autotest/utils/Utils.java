package autotest.utils;

import org.xml.sax.InputSource;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

}
