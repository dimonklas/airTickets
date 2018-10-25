package autotest.pages;

import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class TicketFilterPage {


    @Step("Проверим отображение кнопок фильтров")
    public void checkPresenceOfFiltersButtons(){
        $x(".//*[text()='Время вылета/прилета']/..").shouldBe(visible, enabled.because("Dropdown 'Время вылета/прилета'"));
        $x(".//*[text()='Аэропорт']/..").shouldBe(visible, enabled.because("Dropdown 'Аэропорт'"));
        $x(".//*[text()='Авиакомпания']/..").shouldBe(visible, enabled.because("Dropdown 'Авиакомпания'"));
        $x(".//*[text()='Багаж']/..").shouldBe(visible, enabled.because("Dropdown 'Багаж'"));
        $x(".//*[@for='filters-without-transfers']").shouldBe(visible, enabled.because("Чек-бокс 'Прямой'"));
        $x(".//*[@for='filters-one-transfer']").shouldBe(visible, enabled.because("Чек-бокс '1 пересадка'"));
    }

    @Step("Применим фильтр 'Есть багаж'")
    public void filterBaggageByAvailability(){
        $x(".//*[text()='Багаж']/..").shouldBe(visible).click();
        if(!$x(".//*[text()='Есть багаж']").isDisplayed()) $x(".//*[text()='Багаж']/..").click(); //костыль
        $x(".//*[text()='Есть багаж']").shouldBe(visible, enabled).click();
        $$x(".//span[text()='Есть багаж']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(10));
        $$x(".//span[text()='Нет багажа']").shouldHaveSize(0);
    }

    @Step("Применим фильтр 'Нет багажа'")
    public void filterBaggageByAbsence(){
        $x(".//*[text()='Багаж']/..").click();
        $x(".//*[text()='Все']").shouldBe(visible, enabled).click();
        $x(".//*[text()='Нет багажа']").shouldBe(visible, enabled).click();
        $$x(".//span[text()='Нет багажа']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(10));
        $$x(".//span[text()='Есть багаж']").shouldHaveSize(0);

    }

    @Step("Применим фильтр 'Частичный'")
    public void filterBaggageByPartialAvailability(){
        $x(".//*[text()='Багаж']/..").click();
        $x(".//*[text()='Все']").shouldBe(visible, enabled).click();
        $x(".//*[text()='Частичный']").shouldBe(visible, enabled).click();
        $$x(".//span[text()='Частичный' or text()='Есть багаж' or text()='Нет багажа']").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
    }
}
