package autotest.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.appears;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TicketInfoPage {

    private SelenideElement
        ticketRulesBtn = $(By.xpath(".//*[text()='Правила билета']/../parent::button")),
        ticketForwardDetails = $(By.xpath(".//*[@id='details'] //div[@data-segments='vm.ticket.forward.segments']")),
        ticketBackwarddDetails = $(By.xpath(".//*[@id='details'] //div[@data-segments='vm.ticket.backward.segments']")),
        ticketDifficultDetails = $(By.xpath(".//*[@id='details'] //div[@data-segments='difficultFlight.segments']"));


    @Step("Дождемся появления кнопки 'Правила билета'")
    public void waitForTicketRulesBtn(){
        ticketRulesBtn.waitUntil(appears, 45 * 1000).shouldBe(enabled);
    }

    @Step("Проверим наличие данных билета 'туда'")
    public void checkTicketForwardDetails(){
        ticketForwardDetails.shouldBe(visible);
    }

    @Step("Проверим наличие данных билета 'обратно'")
    public void checkTicketBackwardDetails(){
        ticketBackwarddDetails.shouldBe(visible);
    }


    public void checkTicketDifficultDetails(){
        ticketDifficultDetails.shouldBe(visible);
    }
}
