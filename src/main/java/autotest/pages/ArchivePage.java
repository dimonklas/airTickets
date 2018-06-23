package autotest.pages;


import autotest.utils.ConfigurationVariables;
import autotest.utils.Utils;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ArchivePage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    public SelenideElement
            searchBtn = $x(".//button[text()='Поиск']");

    private SelenideElement
            phoneInputField = $(By.name("phone")),
            submitPhoneBtn = $x(".//button[text()='Далее']"),
            otpcodeField = $(By.name("otpcode")),
            submitOtpBtn = $x(".//button[text()='Отправить']");

    @Step("Авторизация на форме архива")
    public void auth(){
        try {
            $(By.xpath(".//*[text()='Авторизация']")).shouldBe(visible);
        } catch (Error e) {
            e.getMessage();
        }
        if( $(By.xpath(".//*[text()='Авторизация']")).isDisplayed() && phoneInputField.isEnabled() ) {
            phoneInputField.setValue(CV.phone);
            submitPhoneBtn.click();
            otpcodeField.shouldBe(visible, enabled).setValue(CV.otp);
            submitOtpBtn.click();
            $(By.xpath(".//div[@class='tickets-filters']")).waitUntil(appear, 30 * 1000);
            Utils.setCookieData();
        }
    }


    @Step("Проверим состояние забронированного билета")
    public void checkTicketStatus(String bookingCode, String expectedStatus){
        switchTo().window(1);
        switchTo().defaultContent();

        $$(By.xpath(".//section[contains(@id,'ticket-')]")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        String xPathStatus = String.format(".//*[text()='%s']/../*[@data-ng-bind='ticket.status_text']", bookingCode);
        String actualStatus = $(By.xpath(xPathStatus)).shouldBe(visible).getText().trim();
        Assert.assertEquals(actualStatus, expectedStatus);
        switchTo().window(1).close();
    }

    public List<String> getTickets_id(){
        ElementsCollection elements = $$x(".//*[text()='Забронирован, не оплачен']/ancestor::section").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        List<String> idList = new ArrayList<>();
        elements.forEach(element -> {
            String attrValue =  element.getAttribute("id");
            String id = attrValue.substring(attrValue.lastIndexOf("-")+1);
            idList.add(id);
        });
        return idList;
    }

}
