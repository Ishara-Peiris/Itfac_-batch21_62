package com.qatraining.hooks;

import io.cucumber.java.Before;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hook to manage browser sessions for UI tests.
 * Uses AuthenticationManager for centralized session and token caching.
 */
public class SessionHooks {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionHooks.class);

    /**
     * Before each scenario tagged with @admin, login as admin if not already logged in.
     */
    @Before("@admin")
    public void beforeAdmin() {
        LOGGER.info("Setting up admin session");
        WebDriver driver = Serenity.getDriver();
        
        if (!AuthenticationManager.hasActiveSession("admin")) {
            LOGGER.info("Admin session not found, performing login");
            AuthenticationManager.loginAsAdmin(driver);
        } else {
            LOGGER.info("Admin session already cached, injecting cookies");
            AuthenticationManager.injectSessionCookies(driver, "admin");
            driver.navigate().to(AuthenticationManager.getBaseUrl() + "/ui/plants");
        }
    }

    /**
     * Before each scenario tagged with @nonadmin, login as test user if not already logged in.
     */
    @Before("@nonadmin")
    public void beforeNonAdmin() {
        LOGGER.info("Setting up test user session");
        WebDriver driver = Serenity.getDriver();
        
        if (!AuthenticationManager.hasActiveSession("testuser")) {
            LOGGER.info("Test user session not found, performing login");
            AuthenticationManager.loginAsTestUser(driver);
        } else {
            LOGGER.info("Test user session already cached, injecting cookies");
            AuthenticationManager.injectSessionCookies(driver, "testuser");
            driver.navigate().to(AuthenticationManager.getBaseUrl() + "/ui/plants");
        }
    }
}
