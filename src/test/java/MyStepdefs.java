import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.assertEquals;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class MyStepdefs {

    private WebDriver driver;
    private WebElement joinBtn;
    //Explicit wait: Väntar innan element är klickbart
    private static void click(WebDriver driver, WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.
                elementToBeClickable(element));
        element.click();

    }

    //Går in på webbsidan
    @Given("I am on the registration page")
    public void iAmOnTheRegistrationPage() {
        String random = RandomStringUtils.randomNumeric(1);

        int randomNumber = Integer.parseInt(random);

        // Väljer ut Chrome eller Edge, beroende på vad randomNumber är.
        if (randomNumber >=8 ) {

            driver = new ChromeDriver();
        } else {

            driver = new EdgeDriver();
        }
        driver.get("https://membership.basketballengland.co.uk/NewSupporterAccount");
    }

    //Fyller i födelsedatum samt email
    @When("I fill the date of birth field and email field")
    public void iFillTheDateOfBirthFieldAndEmailField() {
        WebElement date = driver.findElement(By.cssSelector("input[name=DateOfBirth]"));
        //Använder min explicit wait metod på date
        click(driver, date);
        date.sendKeys("01/03/2000");

        //Genererar 6 bokstäver på måfå och lägger till det i email-fältet.
        String randomString = RandomStringUtils.randomAlphabetic(6);
        String email = randomString + "@mymail.com";
        driver.findElement(By.id("member_emailaddress")).sendKeys(email);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(email);
    }


    //Fyller i min form
    @And("I fill the registration form with {string}, {string}, {string} and {string}")
    public void iFillTheRegistrationFormWithAnd(String firstname, String lastname, String password, String confirmpassword) {
        joinBtn = driver.findElement(By.name("join"));

        driver.findElement(By.id("member_firstname")).sendKeys(firstname);
        driver.findElement(By.id("member_lastname")).sendKeys(lastname);
        //Om efternamn är blankt, kollar så att varningstexten överensstämer.
        if(lastname==""){
            joinBtn.click();
            assertEquals("Last Name is required", driver.findElement(By.cssSelector(".warning > span")).getText());

        }
        driver.findElement(By.id("signupunlicenced_password")).sendKeys(password);
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys(confirmpassword);

        //Om lösenord inte matchar, kollar så att varningstexten överensstämer.
        if (!password.equals(confirmpassword)) {
            joinBtn.click();
            assertEquals("Password did not match", driver.findElement(By.cssSelector(".warning > span")).getText());
        }
    }
    //Klickar in samtyckesrutan
    @And("I {string} the consent boxes")
    public void iTheConsentBoxes(String check) {
        //Konverterar Boolean till String
        boolean isChecked = Boolean.parseBoolean(check);
        List<WebElement> checkboxes = driver.findElements(By.cssSelector(".box"));
        //Ändrar så att klickandet börjar nerifrån.
        Collections.reverse(checkboxes);
        //Klickar in nödvändiga rutor
        for (int i = 0; i < 4; i++) {
            checkboxes.get(i).click();
        }
        //Kollar om samtyckesrutan är ifylld, om inte kollar så att varningstexten överensstämer.
        if (isChecked) checkboxes.get(4).click();
        else {
            joinBtn.click();
            assertEquals("You must confirm that you have read and accepted our Terms and Conditions", driver.findElement(By.cssSelector(".warning > span")).getText());
        }

    }
        //Registrerar mig på sidan och bekräftar det genom vald text.
        @Then("I should have successfully registered")
        public void iShouldHaveSuccessfullyRegistered() {
            click(driver, joinBtn);
            assertEquals("THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND",
                    driver.findElement(By.cssSelector(".bold:nth-child(1)")).getText());
        }



}
