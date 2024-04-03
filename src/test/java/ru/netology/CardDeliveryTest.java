package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class CardDeliveryTest {

    @Test
    void shouldSendSuccessfulRequest() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id=phone] input").setValue("+79876543211");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Успешно! Встреча успешно забронирована на " + dueDate));
    }

    @Test
    void shouldSendSuccessfulRequestWithDayMore3() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Петров Кирилл");
        $("[data-test-id='phone'] input").setValue("+79876543211");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Успешно! Встреча успешно забронирована на " + dueDate));
    }

    @Test
    void shouldSendSuccessfulRequestWithValidName() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Пупкин-Петров Иван-Егор");
        $("[data-test-id='phone'] input").setValue("+79876543211");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Успешно! Встреча успешно забронирована на " + dueDate));
    }

    @Test
    void shouldNotSendEmptyRequest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id=date] input").setValue(Keys.CONTROL + "a" + Keys.DELETE);
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("");
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='city'] .input__inner  .input__sub")
                .shouldBe(visible)
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendRequestEmptyWithCheckBox() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id=date] input").setValue(Keys.CONTROL + "a" + Keys.DELETE);
        $("[data-test-id='date'] input").setValue("");
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='city'] .input__inner  .input__sub")
                .shouldBe(visible)
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendRequestEmptyCity() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79876543211");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='city'] .input__inner  .input__sub")
                .shouldBe(visible)
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendRequestWithCityNotRussia() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Амстердам");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79876543211");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='city'] .input__sub")
                .shouldBe(visible)
                .shouldHave(Condition.text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotSendRequestWithInvalidCityName() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Moscow");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79876543211");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='city'] .input__sub")
                .shouldBe(visible)
                .shouldHave(Condition.text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotSendRequestWithLess3Days() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79876543211");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='date'] .input__sub")
                .shouldBe(visible)
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotSendRequestEmptyDate() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79876543211");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='date'] .input__sub")
                .shouldBe(visible)
                .shouldHave(text("Неверно введена дата"));
    }

    @Test
    void shouldNotSendRequestEmptyName() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id=phone] input").setValue("+79876543211");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='name'] .input__inner .input__sub")
                .shouldBe(visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendRequestWithInvalidName() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Egor-Ivan Petrov ");
        $("[data-test-id=phone] input").setValue("+79876543211");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='name'] .input__inner .input__sub")
                .shouldBe(visible)
                .shouldHave(text
                        ("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotSendRequestWithEmptyPhone() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='phone'] .input__inner .input__sub")
                .shouldBe(visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendRequestWithInvalidPhone() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+7fa-sfge-av");
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='phone'] .input__inner .input__sub")
                .shouldBe(visible)
                .shouldHave(text
                        ("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotSendRequestEmptyCheckBox() {
        open("http://localhost:9999");
        String dueDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='city'] input").setValue("Луховицы");
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dueDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79876543211");
        $x("//span[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='agreement'] .checkbox__text")
                .shouldBe(Condition.visible)
                .shouldHave(text
                        ("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
