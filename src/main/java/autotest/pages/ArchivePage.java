package autotest.pages;


import autotest.utils.ConfigurationVariables;
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
        if(!$(By.xpath(".//*[text()='Авторизация']")).isDisplayed()) sleep(3000);
        if( $(By.xpath(".//*[text()='Авторизация']")).isDisplayed()) {
            phoneInputField.shouldBe(visible, enabled).setValue(CV.phone);
            submitPhoneBtn.click();
            otpcodeField.shouldBe(visible, enabled).setValue(CV.otp);
            submitOtpBtn.click();
            $(By.xpath(".//div[@class='tickets-filters']")).waitUntil(appear, 30 * 1000);
            searchBtn.should(appear);
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
        $$x(".//*[contains(@id,'ticket-')]").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        ElementsCollection bookedTicketsId = $$x(".//*[text()='Забронирован, не оплачен']/ancestor::section");
        List<String> idList = new ArrayList<>();
        if(bookedTicketsId.size() > 0) {
            bookedTicketsId.forEach(element -> {
                String attrValue =  element.getAttribute("id");
                String id = attrValue.substring(attrValue.lastIndexOf("-")+1);
                idList.add(id);
            });
        }
        return idList;
    }

}
