package autotest.pages;


import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class PassengersDataPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    private SelenideElement
            passengersDataText = $(By.xpath(".//h2[text()='Данные о пассажирах']")),
            preloader = $(By.xpath(".//*[class='circle-spinner']")),
            preloaderBaggage = $(By.xpath(".//*[contains(text(),'Получение дополнительного багажа')]"));

    private SelenideElement
            passengerTitle = $(By.xpath(".//*[@data-ng-bind='passenger.title']")),
            surnameField = $(By.xpath(".//*[@name='lastname']")),
            nameField = $(By.xpath(".//*[@name='firstname']")),
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

    private SelenideElement
            sexMaleChkbox = $(By.xpath(".//label[@title='Мужской']")),
            sexFemaleChkbox = $(By.xpath(".//label[@title='Женский']"));

    private SelenideElement
            backBtn = $(By.xpath(".//*[@id='details'] //*[text()='Назад']/parent::a")),
            bookingBtn = $(By.xpath(".//button[contains(@class,'button-booking')]")),
            buyBtn = $(By.xpath(".//button[contains(@class,'button-buy')]"));



    @Step("Проверим наличие и доступность основных полей ввода данных")
    public void checkAvaliabilityOfCustomersDataFields() {
        passengersDataText.shouldBe(visible);
        preloader.waitUntil(disappear, 60*1000);
        preloaderBaggage.shouldBe(visible);

        preloaderBaggage.waitUntil(disappear, 180 * 1000);


        surnameField.shouldBe(visible, enabled);
        nameField.shouldBe(visible, enabled);
        sexMaleChkbox.shouldBe(visible, enabled);
        sexFemaleChkbox.shouldBe(visible, enabled);
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
//        $(By.xpath(".//label[text()='Докупить багаж']")).shouldBe(visible);
        $(By.xpath(".//*[text()='Фамилия/имя должны совпадать с данными паспорта']")).shouldBe(visible);
        $(By.xpath(".//*[text()='Отключите поле, если нет при себе паспорта']")).shouldBe(visible);
        $(By.xpath(".//*[text()='Для документов без срока действия отключите поле']")).shouldBe(visible);
        emailText1.shouldBe(visible);
        emailText2.shouldBe(visible);
    }

    @Step("Проверим наличие кнопок и текста со сроком бронирования")
    public void checkPresenceAndAvaliabilityOfButtons(){
        backBtn.shouldBe(visible, enabled);
        bookingBtn.shouldBe(visible, enabled);
        buyBtn.shouldBe(visible, enabled);
        Assert.assertTrue(
                $(By.xpath(".//*[@data-ng-bind='vm.bookingData.expireTimeBefore']")).shouldBe(visible)
                        .getText().contains("Действует до"));
        $(By.xpath(".//*[text()='Общая стоимость:']")).shouldBe(visible);
        $(By.xpath(".//*[@data-ng-bind='vm.product.formatTotal']")).shouldBe(visible).shouldNotHave(exactText(""));
    }


    @Step("Заполним клиентские данные: ФИО, пол, дата рождения и гражданство")
    public void fillCustomersData(String surname, String name, String bDate){
        surnameField.setValue(surname);
        nameField.setValue(name);
        bdateField.setValue(bDate);
    }

    @Step("Укажем гражданство")
    public void fillCitizenship(String citizenship){
        citizenshipField.setValue(citizenship);
        $(By.xpath(String.format(".//strong[text()='%s']/ancestor::li", citizenship))).shouldBe(visible).click();
        $(By.xpath(".//*[contains(@ng-messages,'citizenship')] //*[contains(text(),'Заполните поле')]")).shouldNotBe(visible);
    }

    @Step("Установим пол")
    public void setSex(String sex){
        if("F".equalsIgnoreCase(sex)) sexFemaleChkbox.click();
        else sexMaleChkbox.click();
    }

    @Step("Заполним данные документов")
    public void fillDocData(String serNum, String expDate){
        if(serNum != null) {
            if( !$(By.id("docs-0")).isSelected() ) docSNEnabledChkbox.shouldBe(visible).click();
            docSerNumField.shouldBe(enabled).setValue(serNum);
        }

        if(expDate != null) {
            if( !$(By.id("withExpireDateLabel-0")).isSelected() ) docExpDateChkbox.shouldBe(visible).click();
            docSerNumField.shouldBe(enabled).setValue(expDate);
        }
    }

    @Step("Укажем мильную карту")
    public void fillMileCard(String mileCardNumber){
        if (mileCardNumber != null) {
            if(!$(By.id("isBonusCard-0")).isSelected()) mileCardChkbox.click();
            mileCardField.shouldBe(enabled).setValue(mileCardNumber);
        }
    }

    @Step("Заполним поле e-mail")
    public void fillEmail(String email){
        eMailField.setValue(email);
    }

    @Step("Нажмем кнопку 'Забронировать (бесплатно)'")
    public void bookTicket(){
        bookingBtn.click();
    }

}