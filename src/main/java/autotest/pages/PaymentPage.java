package autotest.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;

public class PaymentPage {

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
            backBtn = $(By.xpath(".//button[text()='Назад']"));


    public void doPaymentByCard(String cardNumber, String cardExpDate, String cvv){
        sleep(2 * 1000);
        payByCardBtn.shouldBe(visible,enabled).click();
        linkRules.shouldBe(visible,enabled);
        linkServices.shouldBe(visible,enabled);
        agreeChkBox.shouldBe(visible,enabled).click();

        cardNumField_1.shouldBe(visible,enabled);
        cardNumField_2.shouldBe(visible,enabled);
        cardNumField_3.shouldBe(visible,enabled);
        cardNumField_4.shouldBe(visible,enabled);

        selectYear.shouldBe(visible,enabled);
        selectMonth.shouldBe(visible,enabled);
        cvvField.shouldBe(visible,enabled);

        backBtn.shouldBe(visible,enabled);
        payBtn.shouldBe(visible,enabled).click();
    }

    public void doPaymentByCardFromArchive(String cardNumber, String cardExpDate, String cvv){
        payByCardBtn.shouldBe(visible,enabled).click();
        linkRules.shouldBe(visible,enabled);
        linkServices.shouldBe(visible,enabled);
        agreeChkBox.shouldBe(visible,enabled).click();

        cardNumField_1.shouldBe(visible,enabled);
        cardNumField_2.shouldBe(visible,enabled);
        cardNumField_3.shouldBe(visible,enabled);
        cardNumField_4.shouldBe(visible,enabled);

        selectYear.shouldBe(visible,enabled);
        selectMonth.shouldBe(visible,enabled);
        cvvField.shouldBe(visible,enabled);

        payBtn.shouldBe(visible,enabled).click();
    }


}