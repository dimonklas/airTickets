package autotest.pages;


import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class PassengersDataPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    private SelenideElement
            passengersDataText = $(By.xpath(".//h2[text()='Данные о пассажирах']")),
            preloader = $(By.xpath(".//*[class='circle-spinner']")),
            preloaderBaggage = $(By.xpath(".//*[text()='Получение дополнительного багажа...']/.."));

    private SelenideElement
            passengerTitle = $(By.xpath(".//*[@data-ng-bind='passenger.title']")),
            surnameField = $(By.xpath(".//*[@name='lastname']")),
            nameField = $(By.xpath(".//*[@name='firstname']")),
            sexMaleRadioBtn = $(By.xpath(".//label[@title='Мужской']")),
            sexFemaleRadioBtn = $(By.xpath(".//label[@title='Женский']")),
            bdateField = $(By.xpath(".//*[@name='birthday']")),
            citizenshipField = $(By.xpath(".//*[@name='citizenship']")),
            docSNEnabledChkbox = $(By.xpath(".//label[@for='docs-0']")),
            docExpDateChkbox = $(By.xpath(".//label[@for='withExpireDateLabel-0']")),
            mileCardChkbox = $(By.xpath(".//label[@for='isBonusCard-0']")),
            buyBaggageChkbox = $(By.xpath(".//label[@for='with-baggage-0']")),
            docSerNumField = $(By.xpath(".//*[@name='docnum']")),
            docExpDateField = $(By.xpath(".//*[@name='doc_expire_date']")),
            mileCardField = $(By.xpath(".//*[@name='bonus_card']")),
            eMailField = $(By.xpath(".//*[@name='email']")),
            emailText1 = $(By.xpath(".//h2[text()='Укажите e-mail']")),
            emailText2 = $(By.xpath(".//*[text()='На него будет выслан электронный билет']"));



    @Step("Проверим наличие и доступность основных полей ввода данных")
    public void checkAvaliabilityOfCustomersDataFields() {
        passengersDataText.shouldBe(visible);
        preloader.waitUntil(disappear, 60*1000);
        preloaderBaggage.waitUntil(not(visible), 180*1000);
        surnameField.shouldBe(visible, enabled);
        nameField.shouldBe(visible, enabled);
        sexMaleRadioBtn.shouldBe(visible, enabled);
        sexFemaleRadioBtn.shouldBe(visible, enabled);
        bdateField.shouldBe(visible, enabled);
        citizenshipField.shouldBe(visible, enabled);
        docSNEnabledChkbox.shouldBe(visible, enabled);
        docExpDateChkbox.shouldBe(visible, enabled);
        mileCardChkbox.shouldBe(visible, enabled);
//        buyBaggageChkbox.shouldBe(visible, enabled);
        docSerNumField.shouldBe(visible, enabled);
        docExpDateField.shouldBe(visible, enabled);
        mileCardField.shouldBe(visible);
        eMailField.shouldBe(visible, enabled);
    }

    @Step("Проверим основные надписи для полей ввода данных")
    public void checkPresenceOfTextElements(){
        passengerTitle.shouldBe(visible);
        $(By.xpath(".//label[text()='Фамилия']")).shouldBe(visible);
        $(By.xpath(".//label[text()='Имя']")).shouldBe(visible);
        $(By.xpath(".//label[text()='Пол']")).shouldBe(visible);
        $(By.xpath(".//label[text()='Дата рождения']")).shouldBe(visible);
        $(By.xpath(".//label[text()='Гражданство']")).shouldBe(visible);
        $(By.xpath(".//label[text()='Серия, № документа']")).shouldBe(visible);
        $(By.xpath(".//label[text()='Срок действия']")).shouldBe(visible);
        $(By.xpath(".//label[text()='Мильная карта']")).shouldBe(visible);
        $(By.xpath(".//label[text()='Докупить багаж']")).shouldBe(visible);
        $(By.xpath(".//*[text()='Фамилия/имя должны совпадать с данными паспорта']")).shouldBe(visible);
        $(By.xpath(".//*[text()='Отключите поле, если нет при себе паспорта']")).shouldBe(visible);
        $(By.xpath(".//*[text()='Для документов без срока действия отключите поле']")).shouldBe(visible);
        emailText1.shouldBe(visible);
        emailText2.shouldBe(visible);
    }

}