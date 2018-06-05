package autotest.utils;

import autotest.bl.ProminSessionBL;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
    public String userLogin = System.getProperty("userLogin");
    public String userPassword = System.getProperty("userPassword");
    public String techLogin = System.getProperty("techLogin");
    public String techPassword = System.getProperty("techPassword");

    public String urlPromin;
    public String urlTest;
    public String urlPrerelease;

    public String prominSession;

    public String dbClass;
    public String URLBaseConnection;
    public String loginDataBase;
    public String passwordDataBase;


    public String phone;
    public String otp;
    public String lastName;
    public String firstName;
    public String sex;
    public String birthDate;
    public String citizenship;
    public String docSN;
    public String docExpDate;


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


    }


    private ConfigurationVariables() {
        if (currentBrowser == null || currentBrowser.equalsIgnoreCase(""))
            currentBrowser = getProperty(configurationData, "currentBrowser");

        if (userLogin == null || userLogin.equalsIgnoreCase(""))
            userLogin = getProperty(configurationData, "userLogin");

        if (userPassword == null || userPassword.equalsIgnoreCase(""))
            userPassword = getProperty(configurationData, "userPassword");

        if (techLogin == null || techLogin.equalsIgnoreCase(""))
            techLogin = getProperty(configurationData, "techLogin");

        if (techPassword == null || techPassword.equalsIgnoreCase(""))
            techPassword = getProperty(configurationData, "techPassword");

        urlPromin = getProperty(configurationData, "urlPromin");
        urlTest = getProperty(configurationData, "urlTest");
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

    //возвращаем инстанс объекта
    public static ConfigurationVariables getInstance() {
        return instance;
    }
}
