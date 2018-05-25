package autotest.utils.listeners;

import org.apache.log4j.Logger;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.net.UrlChecker;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.List;


public class RunTestAgainIfFailed implements IRetryAnalyzer {

    private final static Logger LOGGER = Logger.getLogger(RunTestAgainIfFailed.class);

    private int repeatCounter = 0;
    private final int MAX_COUNT = 2;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (repeatCounter < MAX_COUNT && isRetrieble(iTestResult.getThrowable())) {
            repeatCounter++;
            LOGGER.info("***  Повторный перезапуск упавшего теста  ***");
            return true; // перезапускаем тест
        }
        if (repeatCounter > 0) LOGGER.error("ТЕСТ ПРОВАЛЕН ДВАЖДЫ!!!");
        repeatCounter = 0;
        return false;
    }

    private boolean isRetrieble(Throwable throwable) {
        List<Class> list = Arrays.asList(
                ElementNotInteractableException.class,
                ElementNotVisibleException.class,
                TimeoutException.class,
                UrlChecker.TimeoutException.class,
                java.util.concurrent.TimeoutException.class,
//                com.codeborne.selenide.ex.ElementNotFound.class,
//                com.codeborne.selenide.ex.ElementShould.class,
//                com.codeborne.selenide.ex.ElementShouldNot.class,
                InvalidElementStateException.class
        );

        return list.stream().anyMatch(clazz -> {
            return clazz.isInstance(throwable);
        });
    }

}