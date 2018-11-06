package autotest.utils;

import autotest.bl.ProminSessionBL;
import autotest.dto.custData.ClientData;
import autotest.dto.custData.ClientDataItem;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

public class ConfigurationVariables {

    //используем Singleton
    private static final ConfigurationVariables instance;

    private static String configFilePath = "src/main/resources/testConfigs/config.properties";
    private static String testDataFilePath = "src/main/resources/testConfigs/testData.properties";

    private static Properties configurationData = new Properties();
    private static Properties testData = new Properties();

    final private static Logger LOGGER = Logger.getLogger(ConfigurationVariables.class);

    public String currentBrowser = System.getProperty("currentBrowser");
    public String locale = System.getProperty("locale");
    public String timeout = System.getProperty("selenide.timeout");

    public String userLogin = System.getProperty("userLogin");
    public String userPassword = System.getProperty("userPassword");
    public String techLogin = System.getProperty("techLogin");
    public String techPassword = System.getProperty("techPassword");

    public String downloadsDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "downloads" + System.getProperty("file.separator");

    public List<ClientDataItem> clientData;

    public String urlBase = System.getProperty("urlBase");

    public String stornBookedTickets;

    public String urlStornBooking;
    public String urlPromin;
    public String urlTest;
    public String urlTestArch;
    public String urlPrerelease;

    public String prominSession;

    public String phone;
    public String phone2;
    public String otp;

    public String defaultDepartureCity;
    public String defaultArrivalCity;

    public String citizenship;
    public String email;


    static {
        fillMyProperties(configurationData, configFilePath);
        fillMyProperties(testData, testDataFilePath);
        instance = new ConfigurationVariables();

        if(instance.prominSession == null) {
            try{
                instance.prominSession = new ProminSessionBL().getProminSession();
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

        instance.clientData = getClientDataFromFile().getClientData();
    }


    private ConfigurationVariables() {
        if (currentBrowser == null || currentBrowser.equalsIgnoreCase(""))
            currentBrowser = getProperty(configurationData, "currentBrowser");

        if (locale == null || locale.equalsIgnoreCase(""))
            locale = getProperty(configurationData, "locale");

        if (userLogin == null || userLogin.equalsIgnoreCase(""))
            userLogin = getProperty(configurationData, "userLogin");

        if (userPassword == null || userPassword.equalsIgnoreCase(""))
            userPassword = getProperty(configurationData, "userPassword");

        if (techLogin == null || techLogin.equalsIgnoreCase(""))
            techLogin = getProperty(configurationData, "techLogin");

        if (techPassword == null || techPassword.equalsIgnoreCase(""))
            techPassword = getProperty(configurationData, "techPassword");

        if (urlBase == null || urlBase.equalsIgnoreCase(""))
            urlBase = getProperty(configurationData, "urlBase");

        urlStornBooking = getProperty(configurationData, "urlStornBooking");
        urlPromin = getProperty(configurationData, "urlPromin");
        urlTest = getProperty(configurationData, "urlTest");
        urlTestArch = getProperty(configurationData, "urlTestArch");
        urlPrerelease = getProperty(configurationData, "urlPrerelease");

        phone = getProperty(testData, "phone");
        phone2 = getProperty(testData, "phone2");
        otp = getProperty(testData, "otp");

        if (System.getProperty("stornBookedTickets") == null || System.getProperty("stornBookedTickets").equalsIgnoreCase(""))
            stornBookedTickets = getProperty(configurationData, "stornBookedTickets");

        if (System.getProperty("defaultDepartureCity") == null || System.getProperty("defaultDepartureCity").equalsIgnoreCase(""))
            defaultDepartureCity = getProperty(testData, "defaultDepartureCity");
        if (System.getProperty("defaultArrivalCity") == null || System.getProperty("defaultArrivalCity").equalsIgnoreCase(""))
            defaultArrivalCity = getProperty(testData, "defaultArrivalCity");

        citizenship = getProperty(testData, "citizenship");
        email = getProperty(testData, "email");

        if (timeout == null || timeout.trim().isEmpty()) timeout = getProperty(configurationData, "timeout");
    }


    private static void fillMyProperties(Properties properties, String filePath) {
        InputStreamReader input;
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(filePath);
            input = new InputStreamReader(fileInputStream, "UTF8");

            // считываем свойства
            properties.load(input);
        } catch (java.io.FileNotFoundException e) {
            LOGGER.info("Ошибка. Файл config.properties не был найден." + filePath, e);
        } catch (java.io.IOException e) {
            LOGGER.info("IO ошибка в пользовательском методе.", e);
        }
    }

    private static String getProperty(Properties properties, String propertyKey) {
        // получаем значение свойства
        return properties.getProperty(propertyKey);
    }

    private static ClientData getClientDataFromFile() {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("src/main/resources/custData.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return gson.fromJson(reader, ClientData.class);
    }

    //возвращаем инстанс объекта
    public static ConfigurationVariables getInstance() {
        return instance;
    }
}
