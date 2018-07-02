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
    public String userLogin = System.getProperty("userLogin");
    public String userPassword = System.getProperty("userPassword");
    public String techLogin = System.getProperty("techLogin");
    public String techPassword = System.getProperty("techPassword");

    public List<ClientDataItem> clientData;

    public String urlStornBooking;
    public String urlPromin;
    public String urlTest;
    public String urlTestArch;
    public String urlPrerelease;

    public String prominSession;

    public String dbClass;
    public String URLBaseConnection;
    public String loginDataBase;
    public String passwordDataBase;


    public String phone;
    public String otp;

    public String citizenship;

    public String lastName;
    public String firstName;
    public String sex;
    public String birthDate;

    public String docSN;
    public String docExpDate;

    public String lastNameChd;
    public String firstNameChd;
    public String sexChd;
    public String birthDateChd;
    public String docSNChd;
    public String docExpDateChd;

    public String lastNameInf;
    public String firstNameInf;
    public String sexInf;
    public String birthDateInf;
    public String docSNInf;
    public String docExpDateInf;

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

        urlStornBooking = getProperty(configurationData, "urlStornBooking");
        urlPromin = getProperty(configurationData, "urlPromin");
        urlTest = getProperty(configurationData, "urlTest");
        urlTestArch = getProperty(configurationData, "urlTestArch");
        urlPrerelease = getProperty(configurationData, "urlPrerelease");

        dbClass = getProperty(configurationData, "dbClass");
        URLBaseConnection = getProperty(configurationData, "URLBaseConnection");
        loginDataBase = getProperty(configurationData, "loginDataBase");
        passwordDataBase = getProperty(configurationData, "passwordDataBase");

        phone = getProperty(testData, "phone");
        otp = getProperty(testData, "otp");

        lastName = getProperty(testData, "lastName");
        firstName = getProperty(testData, "firstName");
        sex = getProperty(testData, "sex");
        birthDate = getProperty(testData, "birthDate");
        citizenship = getProperty(testData, "citizenship");
        docSN = getProperty(testData, "docSN");
        docExpDate = getProperty(testData, "docExpDate");

        lastNameChd = getProperty(testData, "lastNameChd");
        firstNameChd = getProperty(testData, "firstNameChd");
        sexChd = getProperty(testData, "sexChd");
        birthDateChd = getProperty(testData, "birthDateChd");
        docSNChd = getProperty(testData, "docSNChd");
        docExpDateChd = getProperty(testData, "docExpDateChd");

        lastNameInf = getProperty(testData, "lastNameInf");
        firstNameInf = getProperty(testData, "firstNameInf");
        sexInf = getProperty(testData, "sexInf");
        birthDateInf = getProperty(testData, "birthDateInf");
        docSNInf = getProperty(testData, "docSNInf");
        docExpDateInf = getProperty(testData, "docExpDateInf");

        email = getProperty(testData, "email");

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
