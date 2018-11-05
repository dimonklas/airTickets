package autotest.pages;


import autotest.utils.ConfigurationVariables;
import autotest.utils.exception.NotClickedException;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class PassengersDataPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    private SelenideElement
            passengersDataText = $(By.xpath(".//h2[text()='Данные о пассажирах']")),
            preloader = $(By.xpath(".//*[class='circle-spinner']")),
            preloaderBaggage = $(By.xpath(".//*[contains(text(),'Получение дополнительного багажа')]")),
            bookingMessageText = $(By.xpath(".//*[text()='Вы забронировали авиабилет стоимостью']")),
            errorText = $(By.xpath(".//*[@class='text-error'][text()='Проверьте заполнение пассажирских данных']")),
            errorText2 = $x(".//*[contains(@class,'alert-danger')]");


    private SelenideElement
            backBtn = $(By.xpath(".//*[@id='details'] //*[text()='Назад']/parent::a")),
            bookingBtn = $(By.xpath(".//button[contains(@class,'button-booking')]")),
            buyBtn = $(By.xpath(".//button[contains(@class,'button-buy')]"));



    @Step("Проверим наличие и доступность основных полей ввода данных")
    public void checkAvaliabilityOfCustomersDataFields() {
        passengersDataText.shouldBe(visible).scrollIntoView(true);
        preloader.waitUntil(disappear, 60*1000);

        try {
            preloaderBaggage.shouldBe(visible).waitUntil(disappear, 180 * 1000);
        } catch (com.codeborne.selenide.ex.ElementShould ex) {
            preloaderBaggage.should(disappears);
        }

        $x(".//*[@name='lastname']").shouldBe(visible, enabled);
        $x(".//*[@name='firstname']").shouldBe(visible, enabled);
        $x(".//label[@title='Мужской']").shouldBe(visible, enabled);
        $x(".//label[@title='Женский']").shouldBe(visible, enabled);
        $x(".//*[@name='birthday']").shouldBe(visible, enabled);
        $x(".//*[@name='citizenship']").shouldBe(visible, enabled);
        $x(".//label[@for='docs-0']").shouldBe(visible, enabled);
        $x(".//label[@for='withExpireDateLabel-0']").shouldBe(visible, enabled);
        $x(".//*[@name='docnum']").shouldBe(visible);
        $x(".//*[@name='doc_expire_date']").shouldBe(visible);
        $x(".//*[@name='email']").shouldBe(visible, enabled);
    }



    @Step("Проверим основные надписи для полей ввода данных")
    public void checkPresenceOfTextElements(int index, String passType){
        passengersDataText.scrollIntoView(true);
        $(By.xpath(String.format("(.//*[@data-ng-bind='passenger.title'])[%s]", index))).shouldBe(visible).should(matchesText(passType));

        $(By.xpath(String.format("(.//label[text()='Фамилия'])[%s]", index))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Имя'])[%s]", index))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Пол'])[%s]", index))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Дата рождения'])[%s]", index))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Гражданство'])[%s]", index))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Серия, № документа'])[%s]", index))).shouldBe(visible);
        $(By.xpath(String.format("(.//label[text()='Срок действия'])[%s]", index))).shouldBe(visible);
        $(By.xpath(String.format("(.//*[text()='Фамилия/имя должны совпадать с данными паспорта'])[%s]", index))).shouldBe(visible);

        $(By.xpath(String.format(".//*[text()='%s']/../..//*[text()='Отключите поле, если нет при себе паспорта' or text()='Данные паспорта не обязательны, но вы можете их заполнить включив поле']", passType))).shouldBe(visible);

        if ($x(".//*[@name='doc_expire_date']").isEnabled()) {
            $x(String.format("(.//*[text()='Для документов без срока действия отключите поле'])[%s]", index)).shouldBe(visible);
        }

        $(By.xpath(".//*[text()='Укажите e-mail']")).shouldBe(visible);
        $(By.xpath(".//*[text()='На него будет выслан электронный билет']")).shouldBe(visible);

    }

    @Step("Проверим наличие кнопок 'Назад', 'Забронировать (бесплатно)', 'Купить' и текста со сроком бронирования")
    public void checkPresenceAndAvaliabilityOfButtons(){
        backBtn.shouldBe(visible, enabled).scrollIntoView(true);
        bookingBtn.shouldBe(visible).scrollIntoView(true);
        buyBtn.shouldBe(visible, enabled);
        if (bookingBtn.isEnabled()) {
            Assert.assertTrue(
                    $(By.xpath(".//*[@data-ng-bind='vm.bookingData.expireTimeBefore']")).shouldBe(visible)
                            .getText().contains("Действует до"), "Текст с временем бронирования содержит 'Действует до'");
        }

        $(By.xpath(".//*[text()='Общая стоимость:']")).shouldBe(visible);
        $(By.xpath(".//*[@data-ng-bind='vm.product.formatTotal']")).shouldBe(visible).shouldNotHave(exactText(""));
    }


    @Step("Заполним клиентские данные: ФИО, пол, дата рождения и гражданство")
    public void fillCustomersData(int index, String surname, String name, String bDate){
        $(By.xpath(String.format("(.//*[@name='lastname'])[%s]", index))).shouldBe(visible).setValue(surname);
        $(By.xpath(String.format("(.//*[@name='firstname'])[%s]", index))).setValue(name);
        $(By.xpath(String.format("(.//*[@name='birthday'])[%s]", index))).setValue(bDate);
    }

    @Step("Укажем гражданство")
    public void fillCitizenship(int index, String citizenship){
        $(By.xpath(String.format("(.//*[@name='citizenship'])[%s]", index))).click();
        $(By.xpath(String.format("(.//*[@name='citizenship'])[%s]", index))).setValue(citizenship);
        $(By.xpath(String.format(".//strong[text()='%s']/ancestor::li", citizenship))).shouldBe(visible).click();
        $(By.xpath(".//*[contains(@ng-messages,'citizenship')] //*[contains(text(),'Заполните поле')]")).shouldNotBe(visible);
    }

    @Step("Установим пол")
    public void setSex(int index, String sex){
        if("F".equalsIgnoreCase(sex)) $(By.xpath(String.format("(.//label[@title='Женский'])[%s]", index))).click();
        else $(By.xpath(String.format("(.//label[@title='Мужской'])[%s]", index))).click();
    }

    @Step("Заполним данные документов")
    public void fillDocData(int index, String serNum, String expDate, boolean isFakeDoc){
        SelenideElement serNumChBox = $x(String.format(".//*[@for='docs-%s']", index-1)),
                        expDateChBox = $x(String.format(".//*[@for='withExpireDateLabel-%s']", index-1)),
                        serNumField = $x(String.format("(.//*[@name='docnum'])[%s]", index)),
                        expDateField = $x(String.format("(.//*[@name='doc_expire_date'])[%s]", index));

        if(isFakeDoc) {
            if (serNumField.isEnabled()) serNumChBox.click();
            serNumField.shouldBe(disabled);
            expDateField.shouldBe(disabled);
        } else {
            if (!serNumField.isEnabled() && serNum != null) serNumChBox.click();
            serNumField.shouldBe(enabled.because("Поле 'Серия, № документа' разлочили радиобаттоном"));
            serNumField.setValue(serNum);

            if(!expDateField.isEnabled() && expDate != null) expDateChBox.click();
            expDateField.shouldBe(enabled.because("Поле 'Срок действия' разлочили радиобаттоном"));
            expDateField.setValue(expDate);
        }
    }

    @Step("Укажем мильную карту")
    public void fillMileCard(int index, String mileCardNumber){
        if (mileCardNumber != null) {
            String id = String.format("isBonusCard-%s", index-1);
            if(!$(By.id(id)).isSelected()) $(By.xpath(String.format(".//label[@for='%s']", id))).click();
            $(By.xpath(String.format("(.//*[@name='bonus_card'])[%s]", index))).shouldBe(enabled).setValue(mileCardNumber);
        }
    }

    @Step("Заполним поле e-mail")
    public void fillEmail(String email){
        $(By.xpath(".//*[@name='email']")).setValue(email);
    }

    @Step("Нажмем кнопку 'Забронировать (бесплатно)'")
    public void bookTicket(){
        bookingBtn.shouldBe(visible).scrollIntoView(true).click();
        sleep(200);
        if(errorText.isDisplayed()) {
            passengersDataText.scrollIntoView(true);
            Assert.fail("Ошибка заполнения клиентских даных. После нажатие на кнопку 'Забронировать' появился текст 'Проверьте заполнение пассажирских данных'");
        }
        if ($x(".//*[contains(text(),'произошла ошибка') and contains(text(),'Обновите страницу и попробуйте заново')]").isDisplayed() ||
                errorText2.isDisplayed()){
            throw new NotClickedException("Ошибка при бронировании билета");
        }
        bookingMessageText.waitUntil(appear, 120*1000).shouldBe(visible).scrollTo();
    }

    @Step("Нажмем кнопку 'Купить'")
    public void buyTicket(){
        buyBtn.shouldBe(visible).scrollIntoView(true).click();
        sleep(100);
        if(errorText.isDisplayed()) {
            passengersDataText.scrollIntoView(true);
            Assert.fail("Ошибка заполнения клиентских даных. После нажатие на кнопку 'Забронировать' появился текст 'Проверьте заполнение пассажирских данных'");
        }
        $x(".//*[text()='Выберите способ оплаты']").shouldBe(visible);
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
        switchTo().window(1);
        switchTo().defaultContent();
        ArchivePage.waitForArchivePageLoad();
    }

    @Step("Введем некорректное значение даты в поле 'Дата рождения' и проверим отображение текста валидации")
    public void checkErrorForBirthdayField(String dateValue, String textExpected){
        $x(".//*[@name='birthday']").shouldBe(visible).setValue(dateValue).pressTab();
        if (textExpected != null) {
            String textActual = $$x(".//*[@ng-messages='linePassengerForm.birthday.$error'] //span")
                    .get(0)
                    .shouldBe(visible.because("Валидация поля - текст ошибки вводимых данных"))
                    .innerText()
                    .trim();
            Assert.assertEquals(textActual, textExpected, "Некорректно отобразился текст валидации поля 'Дата рождения'");
        } else {
            $x(".//*[@ng-messages='linePassengerForm.birthday.$error'] //span").shouldNotBe(visible);
        }
    }
}