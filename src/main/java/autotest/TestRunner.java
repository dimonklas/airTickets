package autotest;

import autotest.utils.ConfigurationVariables;
import autotest.utils.listeners.AllureOnFailListener;
import autotest.utils.listeners.RunTestAgainIfFailed;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Log4j
@Epic("Сайт 'Банковские металлы'")
@Feature("Регресионное тестирование сайта 'https://bmetal.test.it.loc'")
@Listeners({AllureOnFailListener.class})
public class TestRunner extends SetUpAndTearDown {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();
    private TestSuite testSuite = new TestSuite();


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "Бронировка авиабилета для одного взрослого (Внешний сайт)",
            groups = {"Покупка билетов"},
            priority = 10)
    public void a1(){
        log.info("Sid = " + CV.prominSession);
//        testSuite.;
    }


}
