package tutorialSolution.elementInteractionSolution;

import browser.BrowserGetter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import tutorialSolution.pages.WebElementInteractionPage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class ElementInteractionTest {
    private final BrowserGetter browserGetter = new BrowserGetter();
    private WebElementInteractionPage page;
    private WebDriver driver;

    @BeforeAll
    public void beforeAll() {
        //initialize the Chrome browser here
        driver = browserGetter.getChromeDriver();
        //initialize page object class
        page = PageFactory.initElements(driver, WebElementInteractionPage.class);
        driver.get(new File("src/main/resources/interactions.html").getAbsolutePath());
    }

    @AfterAll
    public void afterAll() {
        driver.quit();
    }

    @Test
    public void click() {

    }

    @Test
    public void sendKeys() {

    }

    @Test
    public void clear() {

    }

    @Test
    public void getText() {

    }

    @Test
    public void getAttribute() {
        //an attribute that is assigned to the WebElement
        System.out.println("Existing attribute: " + page.elementWithId.getAttribute("height"));
        assertEquals("100", page.elementWithId.getAttribute("height"));
        //an attribute that could be assigned to the WebElement (is it allowed on the WebElement as per the HTML
        // definition) but is not currently assigned
        System.out.println("Non-assigned attribute: " + page.elementWithId.getAttribute("class"));
        assertEquals("", page.elementWithId.getAttribute("class"));
        //an attribute that could never be assigned to the WebElement as it is not allowed by the HTML definition
        System.out.println("Non-existent attribute: " + page.elementWithId.getAttribute("nonExistentAttribute"));
        assertNull(page.elementWithId.getAttribute("nonExistentAttribute"));
        //attributes that appear to have multiple values
        //value of 'class' attribute is composed of multiple 'words'
        System.out.println("The value of the class attribute: " + page.disabledButton.getAttribute("class"));
        assertEquals("w3-btn w3-padding w3-border w3-purple", page.disabledButton.getAttribute("class"));
        //value of 'style' attribute refers to several properties: font size, font family
        System.out.println("The value of the style attribute: " + page.disabledButton.getAttribute("style"));
        assertEquals("font-size: 24px; font-family: verdana;", page.disabledButton.getAttribute("style"));
        assertEquals("font-size: 24px; font-family: verdana;".replaceAll("\\s", ""),
                page.disabledButton.getAttribute("style").replaceAll("\\s", ""));
        //'boolean' attributes
        //the 'checked' attribute value of a checked checkbox
        System.out.println("Checked checkbox state: " + page.checkedCheckbox.getAttribute("checked"));
        assertEquals("true", page.checkedCheckbox.getAttribute("checked"));
        //the 'checked' attribute value of an unchecked checkbox
        System.out.println("Unchecked checkbox state: " + page.uncheckedCheckbox.getAttribute("checked"));
        assertNull(page.uncheckedCheckbox.getAttribute("checked"));
        //the 'checked' attribute value of an unchecked radio button
        System.out.println("Radio button state before checking: " + page.radioButton.getAttribute("checked"));
        assertNull(page.radioButton.getAttribute("checked"));
        page.radioButton.click();
        //the 'checked' attribute value of a checked radio button
        System.out.println("Radio button state after checking: " + page.radioButton.getAttribute("checked"));
        assertEquals("true", page.radioButton.getAttribute("checked"));
        //the value of the 'disabled' attribute of the disabled button
        System.out.println("Disabled attribute of the disabled button: " + page.disabledButton.getAttribute("disabled"
        ));
        assertEquals("true", page.disabledButton.getAttribute("disabled"));
    }

    @Test
    public void select() {
        //declaring the Select objects based on the WebEelements corresponding to the 'select' tags
        Select coffeeSelect = new Select(page.coffeeSelect);
        Select teaSelect = new Select(page.teaSelect);
        Select refreshmentSelect = new Select(page.refreshmentSelect);

        // 1. checking that the available options of the coffee dropdown are the expected ones
        //getOptions will return the list of WebElements corresponding to each option
        //to retrieve the option text (which you can see on the webpage) you need to apply getText() on the option
        List<String> actualCoffeeOptions = new ArrayList<>();
        for (WebElement element : coffeeSelect.getOptions()) {
            actualCoffeeOptions.add(element.getText());
        }
        assertEquals(Arrays.asList("Espresso", "Doppio", "Cappuccino", "Latte Machiato", "Americano"), actualCoffeeOptions);

        // 1.Bonus. same for the teaSelect, which also has an empty text option tag
        List<String> actualTeaOptions = new ArrayList<>();
        for (WebElement element : teaSelect.getOptions()) {
            actualTeaOptions.add(element.getText());
        }
        assertEquals(Arrays.asList("", "Assam", "Ceylon", "Earl Grey", "Pu Erh", "Lady Grey"),
                actualTeaOptions);

        // 2. select from the coffee dropdown based on the index and check that the selection worked by using
        // 'getFirstSelectedOption()'
        //index here is the Java index. Index 0 is the first element in the list
        coffeeSelect.selectByIndex(1);
        assertEquals("Doppio", coffeeSelect.getFirstSelectedOption().getText());

        // 3. select from the tea dropdown based on the value attribute and check that the selection worked
        assertEquals("", teaSelect.getFirstSelectedOption().getText());
        teaSelect.selectByValue("Ceylon");
        assertEquals("Ceylon", teaSelect.getFirstSelectedOption().getText());
        // you can also select the item whose value is an empty String
        teaSelect.selectByValue("");
        // 4. selecting several options from a multiple type dropdown and check that all the selected options were
        // correctly selected
        List<String> actualRefreshmentOptions = new ArrayList<>();
        refreshmentSelect.selectByVisibleText("Still Water");
        refreshmentSelect.selectByVisibleText("Sparkling Water");
        for (WebElement element : refreshmentSelect.getAllSelectedOptions()) {
            actualRefreshmentOptions.add(element.getText());
        }
        assertEquals(Arrays.asList("Still Water", "Sparkling Water"), actualRefreshmentOptions);

        // 5. deselect all options from the multiple type dropdown and check that there are no selected items, by
        // checking the size of the List returned by 'getAllSelectedOptions'
        refreshmentSelect.deselectAll();
        assertEquals(0, refreshmentSelect.getAllSelectedOptions().size());

        // 6. select some options, then deselect one by index and check that selected options do not include the one
        // that was deselected
        refreshmentSelect.selectByVisibleText("Still Water");
        refreshmentSelect.selectByVisibleText("Sparkling Water");
        refreshmentSelect.selectByVisibleText("Rose Lemonade");
        refreshmentSelect.deselectByIndex(0);
        actualRefreshmentOptions = new ArrayList<>();

        for (WebElement element : refreshmentSelect.getAllSelectedOptions()) {
            actualRefreshmentOptions.add(element.getText());
        }
        assertEquals(Arrays.asList("Still Water", "Sparkling Water"), actualRefreshmentOptions);

        // 7. deselect an option by value and check that it was deselected successfully
        refreshmentSelect.deselectByValue("5");
        actualRefreshmentOptions = new ArrayList<>();
        for (WebElement element : refreshmentSelect.getAllSelectedOptions()) {
            actualRefreshmentOptions.add(element.getText());
        }
        assertEquals(Arrays.asList("Still Water"), actualRefreshmentOptions);

        // 8. deselect last remaining selected option by visible text and check that there are no more selected options
        refreshmentSelect.deselectByVisibleText("Still Water");
        assertEquals(0, refreshmentSelect.getAllSelectedOptions().size());

    }

    @Test
    public void getCSSValue() {

    }
}
