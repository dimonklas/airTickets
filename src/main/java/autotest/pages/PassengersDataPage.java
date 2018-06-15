package autotest.pages;


import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PassengersDataPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    private SelenideElement
            passengersDataText = $(By.xpath(".//h2[text()='Данные о пассажирах']")),
            preloader = $(By.xpath(".//*[class='circle-spinner']")),
            preloaderBooking = $(By.xpath(".//*[contains(@data-ng-show,'vm.loader||vm.bookingData.loader')] //*[contains(@class,'circle-spinner__circle')]")),
            preloaderBaggage = $(By.xpath(".//*[contains(text(),'Получение дополнительного багажа')]")),
            bookingMessageText = $(By.xpath(".//*[text()='Вы забронировали авиабилет стоимостью']")),
            errorText = $(By.xpath(".//*[@class='text-error'][text()='Проверьте заполнение пассажирских данных']"));

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
            eMailField = $(By.xpath(".//*[@name='email']"));


    private SelenideElement
            sexMaleChkbox = $(By.xpath(".//label[@title='Мужской']")),
            sexFemaleChkbox = $(By.xpath(".//label[@title='Женский']"));

    private SelenideElement
            backBtn = $(By.xpath(".//*[@id='details'] //*[text()='Назад']/parent::a")),
            bookingBtn = $(By.xpath(".//button[contains(@class,'button-booking')]")),
            buyBtn = $(By.xpath(".//button[contains(@class,'button-buy')]"));



    @Step("Проверим наличие и доступность основных полей ввода данных")
    public void checkAvaliabilityOfCustomersDataFields() {
        passengersDataText.shouldBe(visible).scrollIntoView(true);
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
    public void checkPresenceOfTextElements(int passNo, String passType){
        passengerTitle.shouldBe(visible).should(matchesText(passType));

        $(By.xpath(String.format("(.//label[text()='Фамилия'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Имя'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Пол'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Дата рождения'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Гражданство'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Серия, № документа'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Срок действия'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Мильная карта'])[%s]", passNo))).shouldBe(visible);
//        $(By.xpath(String.format("(.//label[text()='Докупить багаж'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//*[text()='Фамилия/имя должны совпадать с данными паспорта'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//*[text()='Отключите поле, если нет при себе паспорта'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//*[text()='Для документов без срока действия отключите поле'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//*[text()='Укажите e-mail'])[%s]", passNo))).shouldBe(visible);
        $(By.xpath(String.format("(.//*[text()='На него будет выслан электронный билет'])[%s]", passNo))).shouldBe(visible);

    }

    @Step("Проверим наличие кнопок 'Назад', 'Забронировать (бесплатно)', 'Купить' и текста со сроком бронирования")
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
        surnameField.shouldBe(visible).setValue(surname);
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
            docExpDateField.shouldBe(enabled).setValue(expDate);
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
        bookingBtn.shouldBe(visible).scrollIntoView(true).click();
        errorText.shouldNot(appear);
        bookingMessageText.waitUntil(appear, 120*1000).shouldBe(visible).scrollTo();
    }


    @Step("Проверим данные после бронирования")
    public void checkBookedTicketMessage(String price){
        String text = "Вы забронировали авиабилет стоимостью " + price + ",00" + " грн";
        $$(By.xpath(".//*[contains(@class,'booking-message__text-header')]/span")).forEach(e -> {
            Assert.assertTrue(text.contains(e.getText()), "В сообщении о бронировании билета некорректно отобразился текст");
        });
    }

    @Step("Проверим отображение кода бронирования билета")
    public String getBookingCode(){
        String code = $(By.xpath(".//*[text()='Код бронирования']/following-sibling::p[@data-ng-bind='locator']")).shouldBe(visible).getText();
        Assert.assertFalse(code.isEmpty(), "В сообщении о бронировании билета не отобразился код бронирования");
        return code;
    }

    @Step("Перейдем в архив билетов")
    public void openArchive(){
        $(By.linkText("Архив билетов")).shouldBe(visible, enabled).click();
    }

}