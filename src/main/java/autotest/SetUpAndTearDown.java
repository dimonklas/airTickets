package autotest;

import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;


public class SetUpAndTearDown {

    private static final Logger LOGGER = Logger.getLogger(SetUpAndTearDown.class);
    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();
    private final String browser = CV.currentBrowser;
    private final String locale = CV.locale;


    @BeforeSuite(alwaysRun = true)
    public void SetUpBrowser() throws Exception {

        switch (locale) {
            case "UA" : Locale.setDefault(new Locale("uk", "ua"));
                LOGGER.info("Set locale to uk-UA");
                break;
            case "RU" : Locale.setDefault(new Locale("ru", "ua"));
                LOGGER.info("Set locale to ru-UA");
                break;
            case "EN" : Locale.setDefault(new Locale("en", "ua"));
                LOGGER.info("Set locale to en-UA");
                break;
            default: Locale.setDefault(new Locale("uk", "ua"));
                LOGGER.info("Locale is uk-UA");
        }

        switch (browser){
            case "firefox" :
                System.setProperty("selenide.browser", "autotest.webDriverProviders.FirefoxDriverProvider");
                break;
            case "chrome" :
                System.setProperty("selenide.browser", "autotest.webDriverProviders.ChromeDriverProvider");
                break;
            default: System.setProperty("selenide.browser", "autotest.webDriverProviders.FirefoxDriverProvider");
                break;
        }
    }


    @BeforeSuite(alwaysRun = true)
    void deleteOldDirs() throws IOException {
        File buildReportsDir = new File("build/reports");
        if (buildReportsDir.exists())
        FileUtils.deleteDirectory(buildReportsDir);
    }


    @BeforeSuite(alwaysRun = true)
    void emptyDownloadsDir()  {
        if (!new File(CV.downloadsDir).mkdir()) {
            File[] files = new File(CV.downloadsDir).listFiles();
            Arrays.stream(files).forEach(FileUtils::deleteQuietly);
        }
    }


    @AfterSuite(alwaysRun = true)
    void closeBrowser() {
        if(WebDriverRunner.hasWebDriverStarted()) {
            WebDriverRunner.closeWebDriver();
        }
    }

    @Step("Сторнирование бронировок")
    @AfterSuite(alwaysRun = true)
    public void stornBookedTickets() throws Exception {
        if (!"true".equalsIgnoreCase(System.getProperty("isDebug"))) {
            LOGGER.info("Сторнирование бронировок");
            new TestSuite().stornBookings();
        }
    }

    @AfterTest(alwaysRun = true)
    public void closeDriver(){
        WebDriverRunner.closeWebDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void createEnvironmentProps() {
        FileOutputStream fos = null;
        try {
            Properties props = new Properties();
            fos = new FileOutputStream("target/allure-results/environment.properties");

            ofNullable(getProperty("browser")).ifPresent(s -> props.setProperty("browser", s));
            ofNullable(getProperty("driver.version")).ifPresent(s -> props.setProperty("driver.version", s));
            ofNullable(getProperty("os.name")).ifPresent(s -> props.setProperty("os.name", s));
            ofNullable(getProperty("os.version")).ifPresent(s -> props.setProperty("os.version", s));
            ofNullable(getProperty("os.arch")).ifPresent(s -> props.setProperty("os.arch", s));

            props.store(fos, "See https://github.com/allure-framework/allure-app/wiki/Environment");

            fos.close();
        } catch (IOException e) {
            LOGGER.error("IO problem when writing allure properties file", e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }
}
