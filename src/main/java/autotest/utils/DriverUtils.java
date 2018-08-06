package autotest.utils;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


public final class DriverUtils {

    @Attachment(type = "image/png")
    public static byte[] screenshot() throws IOException {
        if(getWebDriver() != null) {
            return Files.toByteArray(Screenshots.takeScreenShotAsFile());
        } else return null;
    }

    @Step("Закрытие вкладок/окон")
    public void closeTab(){
        switchTo().window(0);
        String originalHandle = getWebDriver().getWindowHandle();
        getWebDriver().getWindowHandles().stream().filter(handle -> !handle.equals(originalHandle)).forEach(handle -> {
            getWebDriver().switchTo().window(handle);
            getWebDriver().close();
        });
        switchTo().window(0);
    }
}