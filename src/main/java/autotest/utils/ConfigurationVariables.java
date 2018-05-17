package autotest.utils;

import org.apache.log4j.Logger;

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
    public String userCashierLogin = System.getProperty("userCashierLogin");
    public String userCashierPassword = System.getProperty("userCashierPassword");
    public String totpUrl;
    public String branch;
    public String branchCass;
    public String branchCashier;
    public String dbClass;
    public String URLBaseConnection;
    public String loginDataBase;
    public String passwordDataBase;

    static {

        fillMyProperties(configurationData, configFilePath);
        fillMyProperties(testData, testDataFilePath);
        instance = new ConfigurationVariables();

    }


     ConfigurationVariables() {
        if (currentBrowser == null || currentBrowser.equalsIgnoreCase(""))
            currentBrowser = getProperty(configurationData, "currentBrowser");

        if (userLogin == null|| userLogin.equalsIgnoreCase(""))
            userLogin = getProperty(configurationData, "userLogin");

        if (userPassword == null || userPassword.equalsIgnoreCase(""))
            userPassword = getProperty(configurationData, "userPassword");

         if (userCashierLogin == null|| userCashierLogin.equalsIgnoreCase(""))
             userCashierLogin = getProperty(configurationData, "userCashierLogin");

         if (userCashierPassword == null || userCashierPassword.equalsIgnoreCase(""))
             userCashierPassword = getProperty(configurationData, "userCashierPassword");

         totpUrl = getProperty(configurationData, "totpUrl");
         branch = getProperty(configurationData, "branch");
         branchCass = getProperty(configurationData, "branchCass");
         branchCashier = getProperty(configurationData, "branchCashier");

         dbClass = getProperty(configurationData, "dbClass");
         URLBaseConnection = getProperty(configurationData, "URLBaseConnection");
         loginDataBase = getProperty(configurationData, "loginDataBase");
         passwordDataBase = getProperty(configurationData, "passwordDataBase");

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
