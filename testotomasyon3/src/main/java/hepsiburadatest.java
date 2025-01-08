import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class hepsiburadatest {
    public static void main(String[] args) {
        // Setup ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to hepsiburada.com
            driver.get("https://www.hepsiburada.com");

            // Maximize the browser window
            driver.manage().window().maximize();

            // Wait for the page to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Perform a search (e.g., "iphone")
            WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input.desktopOldAutosuggestTheme-input")));
            searchBox.sendKeys("iphone");
            searchBox.submit();

            // Wait for the search results to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li.search-item")));

            // Get the list of products
            List<WebElement> products = driver.findElements(By.cssSelector("li.search-item"));

            if (products.isEmpty()) {
                System.out.println("No products found.");
                return;
            }

            // Select a random product
            Random random = new Random();
            WebElement randomProduct = products.get(random.nextInt(products.size()));
            randomProduct.click();

            // Wait for the product detail page to load
            WebElement priceElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.product-price")));
            String productPriceText = priceElement.getText().replace("TL", "").replace(",", "").trim();
            double productPrice = Double.parseDouble(productPriceText);

            System.out.println("Product price on detail page: " + productPrice + " TL");

            // Add product to the cart
            WebElement addToCartButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.add-to-cart")));
            addToCartButton.click();

            // Go to the cart
            WebElement goToCartButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href='/sepetim']")));
            goToCartButton.click();

            // Wait for the cart page to load
            WebElement cartPriceElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.cart-price")));
            String cartPriceText = cartPriceElement.getText().replace("TL", "").replace(",", "").trim();
            double cartPrice = Double.parseDouble(cartPriceText);

            System.out.println("Product price in cart: " + cartPrice + " TL");

            // Compare the product price and cart price
            if (productPrice == cartPrice) {
                System.out.println("Price verification successful: " + productPrice + " TL");
            } else {
                System.out.println("Price mismatch! Detail page price: " + productPrice + " TL, Cart price: " + cartPrice + " TL");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
