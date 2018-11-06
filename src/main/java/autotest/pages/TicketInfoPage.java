package autotest.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.appears;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class TicketInfoPage {

    private SelenideElement
        ticketRulesBtn = $(By.xpath(".//*[text()='Правила билета']/../parent::button")),
        ticketForwardDetails = $(By.xpath(".//*[@id='details'] //div[@data-segments='vm.ticket.forward.segments']")),
        ticketBackwarddDetails = $(By.xpath(".//*[@id='details'] //div[@data-segments='vm.ticket.backward.segments']"));


    @Step("Дождемся появления кнопки 'Правила билета'")
    public void waitForTicketRulesBtn(){
        ticketRulesBtn.waitUntil(appears, 120 * 1000).shouldBe(enabled);
    }

    @Step("Проверим наличие данных билета 'туда'")
    public void checkTicketForwardDetails(){
        ticketForwardDetails.shouldBe(visible);
    }

    @Step("Проверим наличие данных билета 'обратно'")
    public void checkTicketBackwardDetails(){
        ticketBackwarddDetails.shouldBe(visible);
    }


    @Step("Проверим наличие данных билетов для сложного маршрута")
    public void checkTicketDetailsForDifficultRoute(int size){
        $$x(".//*[@id='details'] //div[@data-segments='difficultFlight.segments']")
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(size));
    }
}
