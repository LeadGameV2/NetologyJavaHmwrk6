import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class CallbackTest {
    private WebDriver driver;
    @BeforeAll
    static void setUpAll() {
// предварительно копируем в папку tmp файл chromedriver.exe из архива
        System.setProperty("webdriver.chrome.driver", "C:\\tmp\\chromedriver.exe");
//Установка драйвера
        WebDriverManager.chromedriver().setup();
    }
    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);

    }
    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }
    @Test
    public void shouldSendFormSuccessfully() {
        driver.get("about:blank");
        driver.get("http://localhost:9999");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Ждем появления формы
        WebElement form = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.tagName("form"))
        );

        // Заплнение
        WebElement nameField = form.findElement(By.cssSelector("[data-test-id='name'] input"));
        nameField.sendKeys("Иван Петров-Сидоров");

        WebElement phoneField = form.findElement(By.cssSelector("[data-test-id='phone'] input"));
        phoneField.sendKeys("+79991234567");

        WebElement agreementCheckbox = form.findElement(By.cssSelector("[data-test-id='agreement'] .checkbox__box"));
        agreementCheckbox.click();

        WebElement submitButton = form.findElement(By.cssSelector("button[type='button']"));
        submitButton.click();

        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test-id='order-success']")
                )
        );

        assertTrue(successMessage.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                successMessage.getText().trim());
    }
}