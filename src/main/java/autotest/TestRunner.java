package autotest;

import autotest.utils.listeners.AllureOnFailListener;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Listeners;

@Epic("Сайт 'Банковские металлы'")
@Feature("Регресионное тестирование сайта 'https://bmetal.test.it.loc'")
@Listeners({AllureOnFailListener.class})
public class TestRunner extends SetUpAndTearDown {

    private TestSuite testSuite = new TestSuite();


}
