package autotest;


import autotest.database.DataBaseBL;
import autotest.pages.*;
import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j;
import org.testng.SkipException;

import static com.codeborne.selenide.Condition.*;

@Log4j
public class TestSuite {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();




}