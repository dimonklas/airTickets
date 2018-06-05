package autotest.pages;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CustomerContactDataPage {

    private SelenideElement
        contDataText = $(By.xpath(".//h2[text()='Контактные данные покупателя']")),
        selectUserText = $(By.xpath(".//*[text()='Выберите пользователя']")),
        phoneFieldText1 = $(By.xpath(".//*[text()='Укажите Ваш номер телефона']")),
        phoneFieldText2 = $(By.xpath(".//*[text()='на него будет выслан SMS с кодом']")),
        inputPhoneField = $(By.xpath(".//input[@name='phone']")),
        backBtn = $(By.xpath(".//button[text()='Назад']")),
        nextBtn = $(By.xpath(".//button[text()='Далее']"));


    private ElementsCollection
        usersButtons = $$(By.xpath(".//*[contains(@class,'authentication-board__item')]"));


    @Step("Проверим наличие блока 'Контактные данные покупателя' и его основных элементов")
    public void checkPresenceOfContactDataBlock(){
        contDataText.shouldBe(visible);

        if(selectUserText.isDisplayed()) {
            usersButtons.shouldBe(CollectionCondition.sizeGreaterThanOrEqual(2));
            usersButtons.forEach(s -> s.shouldBe(visible));
        } else {
            inputPhoneField.shouldBe(visible);
            phoneFieldText1.shouldBe(visible);
            phoneFieldText2.shouldBe(visible);
            backBtn.shouldBe(visible);
            nextBtn.shouldBe(visible);
        }
    }


    @Step("Заполним клиентские данные")
    public void enterUserData(){

    }


}
