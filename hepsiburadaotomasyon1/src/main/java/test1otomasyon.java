import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class test1otomasyon {
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

            // Go to the reviews tab
            WebElement reviewsTab = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[title='Değerlendirmeler']")));
            reviewsTab.click();

            // Wait for the reviews section to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.ReviewCard-module-1")));

            // Sort reviews by "En Yeni Değerlendirme"
            WebElement sortDropdown = driver.findElement(By.cssSelector("select[name='sort']"));
            sortDropdown.click();
            WebElement newestOption = driver.findElement(By.xpath("//option[text()='En Yeni Değerlendirme']"));
            newestOption.click();

            // Wait for reviews to be sorted
            Thread.sleep(2000); // Add an explicit wait to ensure sorting is applied

            // Check if reviews are available
            List<WebElement> reviews = driver.findElements(By.cssSelector("div.ReviewCard-module-1"));
            if (reviews.isEmpty()) {
                System.out.println("No reviews available.");
                return;
            }

            // Select thumbsUp or thumbsDown randomly
            WebElement randomReview = reviews.get(random.nextInt(reviews.size()));
            List<WebElement> thumbsButtons = randomReview.findElements(By.cssSelector("button"));

            if (!thumbsButtons.isEmpty()) {
                WebElement randomThumbs = thumbsButtons.get(random.nextInt(thumbsButtons.size()));
                randomThumbs.click();

                // Wait for the "Teşekkür Ederiz" message
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Teşekkür Ederiz')]")));
                System.out.println("Action completed: Teşekkür Ederiz message appeared.");
            } else {
                System.out.println("No thumbsUp or thumbsDown buttons available.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
