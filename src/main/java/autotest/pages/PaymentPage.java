package autotest.pages;

import autotest.utils.ConfigurationVariables;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.switchTo;

public class PaymentPage {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    public SelenideElement
            title = $(By.xpath(".//*[text()='Выберите способ оплаты']"));

    private SelenideElement
            payByCardBtn = $x(".//*[text()='С карты/счета']/parent::div"),
            linkRules = $(By.linkText("правилами билета")),
            linkServices = $(By.linkText("предоставления услуг")),
            agreeChkBox = $x(".//*[@for='insurance-confirm']"),
            cardNumField_1 = $x(".//*[@type='tel' and @index='0']"),
            cardNumField_2 = $x(".//*[@type='tel' and @index='1']"),
            cardNumField_3 = $x(".//*[@type='tel' and @index='2']"),
            cardNumField_4 = $x(".//*[@type='tel' and @index='3']"),
            selectYear = $(By.name("exp_date_year")),
            selectMonth = $(By.name("exp_date_month")),
            cvvField = $(By.name("card_cvv")),
            payBtn = $(By.xpath(".//button[text()='Оплатить']")),
            backBtn = $(By.xpath(".//button[text()='Назад']")),
            confirmPaymentCheckBox = $x("//*[@id='insurance-confirm']//../label"),
            confirmOtpButton = $x("//*[text()='Подтвердить']"),
            otpCode = $(By.name("otpcode"));


    @Step("Проведем оплату картой")
    public void doPaymentByCard(String[] cardNumber, String cardExpDate, String cvv){
        sleep(2 * 1000);
        payByCardBtn.shouldBe(visible,enabled).click();
        linkRules.shouldBe(visible,enabled);
        linkServices.shouldBe(visible,enabled);

        cardNumField_1.shouldBe(visible,enabled).setValue(cardNumber[0]);
        cardNumField_2.shouldBe(visible,enabled).setValue(cardNumber[1]);
        cardNumField_3.shouldBe(visible,enabled).setValue(cardNumber[2]);
        cardNumField_4.shouldBe(visible,enabled).setValue(cardNumber[3]);

        selectYear.shouldBe(visible,enabled).selectOptionByValue(cardExpDate.substring(cardExpDate.indexOf("/")+1));
        selectMonth.shouldBe(visible,enabled).selectOption(cardExpDate.substring(0, cardExpDate.indexOf("/")));

        cvvField.shouldBe(visible,enabled).setValue(cvv);

        backBtn.shouldBe(visible,enabled);
        payBtn.shouldBe(visible,enabled).click();
    }

    @Step("Проведем оплату картой")
    public void doPaymentByCard() {
        sleep(3 * 1000);
        payByCardBtn.shouldBe(visible,enabled).click();
        linkRules.shouldBe(visible,enabled);
        linkServices.shouldBe(visible,enabled);

        $(By.name("cardsSelect")).shouldBe(visible).click();
        $x(".//*[contains(text(),'**51')]").shouldBe(visible).click();
        confirmPaymentCheckBox.shouldBe(visible, enabled).click();

//        backBtn.shouldBe(visible,enabled);
        payBtn.shouldBe(visible,enabled).click();

        otpCode.waitUntil(visible, 30 *1000);
        otpCode.shouldBe(visible).setValue(CV.otp);

        confirmOtpButton.waitUntil(enabled, 5 * 1000);
        confirmOtpButton.shouldBe(visible, enabled).click();

        if (confirmOtpButton.isDisplayed()) confirmOtpButton.click();
        $x("//*[contains(text(),'Создана бронировка')]").waitUntil(visible, 120 * 1000);
    }

    @Step("ID билета, после оплаты")
    public String getIdTicketAfterPayment() {
        return $x("//*[contains(text(),'Создана бронировка')]//../b[1]").getText();
    }

    @Step("Перейдем в архив билетов")
    public void openArchive() {
        $(By.linkText("Архив билетов")).shouldBe(visible, enabled).click();
        if (WebDriverRunner.getWebDriver().getWindowHandles().size() > 1) {
            switchTo().window(1);
            switchTo().defaultContent();
        }
        ArchivePage.waitForArchivePageLoad();
    }

    @Step("Проведем оплату картой в архиве")
    public void doPaymentByCardFromArchive(String cardNumber, String cardExpDate, String cvv){
        payByCardBtn.shouldBe(visible,enabled).click();
        linkRules.shouldBe(visible,enabled);
        linkServices.shouldBe(visible,enabled);

        cardNumField_1.shouldBe(visible,enabled);
        cardNumField_2.shouldBe(visible,enabled);
        cardNumField_3.shouldBe(visible,enabled);
        cardNumField_4.shouldBe(visible,enabled);

        selectYear.shouldBe(visible,enabled);
        selectMonth.shouldBe(visible,enabled);
        cvvField.shouldBe(visible,enabled);

        payBtn.shouldBe(visible,enabled).click();
    }

    @Step("Примем соглашение с правилами билета и предоставления услуг")
    public void acceptOfertaRules(){
        agreeChkBox.shouldBe(visible,enabled).click();
    }

    @Step("Проверим сообщение при незаполненном поле 'Номер карты'")
    public void checkEmptyPanFieldMessage(){
        $x(".//*[@ng-message='required']").shouldBe(visible);
        Assert.assertEquals($x(".//*[@ng-message='required']").innerText().trim(), "Введите номер карты");
    }

    @Step("Проверим сообщение при неполностью заполненном поле 'Номер карты'")
    public void checkPartialFilledPanFieldMessage(){
        $x(".//*[@ng-message='minlength']").shouldBe(visible);
        Assert.assertEquals($x(".//*[@ng-message='minlength']").innerText().trim(), "Минимум 16 символов");
    }

    @Step("Проверим сообщение при некорректно заполненном поле 'Номер карты'")
    public void checkWrongFilledPanFieldMessage(){
        $x(".//*[@ng-message='pattern']").shouldBe(visible);
        Assert.assertEquals($x(".//*[@ng-message='pattern']").innerText().trim(), "Только цифры");
    }


    @Step("Проверим сообщение при незаполненном поле 'CVV2'")
    public void checkEmptyCVV2FieldMessage(){
        $x(".//*[@ng-message='required']").shouldBe(visible);
        Assert.assertEquals($x(".//*[@ng-message='required']").innerText().trim(), "Заполните поле");
    }

    @Step("Проверим сообщение при неполностью заполненном поле 'CVV2'")
    public void checkPartialFilledCVV2FieldMessage(){
        $x(".//*[@ng-message='pattern']").shouldBe(visible);
        Assert.assertEquals($x(".//*[@ng-message='pattern']").innerText().trim(), "Минимум 3 символа. Только цифры");
    }

    @Step("Проверим сообщение при некорректно заполненном поле 'CVV2'")
    public void checkWrongFilledCVV2FieldMessage(){
        $x(".//*[@ng-message='pattern']").shouldBe(visible);
        Assert.assertEquals($x(".//*[@ng-message='pattern']").innerText().trim(), "Минимум 3 символа. Только цифры");
    }

    @Step("Проверим сообщение о непринятых условиях оферты")
    public void chechNotAgreedOfertaMessage(){
        $x(".//*[text()='Для продолжения необходимо принять условия оферты']").shouldBe(visible);
    }

    @Step("Проверим отсутствие сообщений о некорректно заполненных полях")
    public void checkNoErrorMessagesPresent(){
        $x(".//*[@ng-messages='vm.form.card_number.$error']/div").shouldNotBe(visible.because("Сообщение для поля Номер карты"));
        $x(".//*[@ng-messages='vm.form.card_cvv.$error']/div").shouldNotBe(visible.because("Сообщение для поля CVV2"));
        $x(".//*[@data-ng-messages='ChoosePaymentForm.insuranceConfirm.$error']/div").shouldNotBe(visible.because("Сообщение о непринятой оферте"));
    }
}