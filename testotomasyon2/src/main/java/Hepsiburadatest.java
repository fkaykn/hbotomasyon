import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class Hepsiburadatest {
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
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("product-name")));

            // Check visibility of "Diğer Satıcılar" tab
            List<WebElement> otherSellersTabs = driver.findElements(By.cssSelector("a[title='Diğer Satıcılar']"));
            boolean otherSellersAvailable = !otherSellersTabs.isEmpty();

            if (otherSellersAvailable) {
                WebElement otherSellersTab = otherSellersTabs.get(0);
                otherSellersTab.click();

                // Wait for the "Diğer Satıcılar" section to load
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.seller-container")));

                // Get the prices from the "Diğer Satıcılar" section
                List<WebElement> sellerPrices = driver.findElements(By.cssSelector("div.seller-container span.price"));

                double lowestPrice = Double.MAX_VALUE;
                WebElement lowestPriceButton = null;

                for (WebElement priceElement : sellerPrices) {
                    String priceText = priceElement.getText().replace("TL", "").replace(",", "").trim();
                    double price = Double.parseDouble(priceText);
                    WebElement addToCartButton = priceElement.findElement(By.xpath("../following-sibling::div/button"));

                    if (price < lowestPrice) {
                        lowestPrice = price;
                        lowestPriceButton = addToCartButton;
                    }
                }

                if (lowestPriceButton != null) {
                    lowestPriceButton.click();
                    System.out.println("Lowest price product added to cart: " + lowestPrice + " TL");
                } else {
                    System.out.println("No valid price found in 'Diğer Satıcılar'. Adding the default product to cart.");
                }

            } else {
                System.out.println("'Diğer Satıcılar' tab is not available. Adding the current product to cart.");
                WebElement addToCartButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.add-to-cart")));
                addToCartButton.click();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
