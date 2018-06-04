package autotest;

import autotest.utils.ConfigurationVariables;
import autotest.utils.listeners.AllureOnFailListener;
import autotest.utils.listeners.RunTestAgainIfFailed;
import io.qameta.allure.Epic;
import lombok.extern.log4j.Log4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Log4j
@Epic("Сайт покупки авиабилетов (регрессионное тестирование крит. функционала)")
//@Feature("Бронирование билетов")
@Listeners({AllureOnFailListener.class})
public class TestRunner extends SetUpAndTearDown {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();
    private TestSuite testSuite = new TestSuite();


    @Test(  enabled = true,
            retryAnalyzer = RunTestAgainIfFailed.class,
            description = "front-14514: Бронировка авиабилета для одного взрослого (Внешний Сайт)",
            groups = {"Покупка билетов"},
            priority = 10)
    public void a1_front_14514(){

        testSuite.front_14514();
        log.info("Sid = " + CV.prominSession);
    }


}

