package com.qatraining.hooks;

import io.restassured.RestAssured;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qatraining.pages.LoginPage;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Centralized authentication manager for UI and API tests.
 * Manages login sessions and tokens to avoid repeated logins across test scenarios.
 */
public class AuthenticationManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationManager.class);
    private static final Properties CONFIG = loadConfig();
    
    // Cache for UI session cookies - one per role
    private static final Map<String, Set<Cookie>> UI_SESSIONS = new ConcurrentHashMap<>();
    
    // Cache for API authentication tokens - one per role
    private static final Map<String, String> API_TOKENS = new ConcurrentHashMap<>();
    
    // Track if login has been performed for a role
    private static final Map<String, Boolean> LOGIN_COMPLETED = new ConcurrentHashMap<>();
    
    private static Properties loadConfig() {
        Properties p = new Properties();
        try (InputStream in = AuthenticationManager.class.getResourceAsStream("/test-config.properties")) {
            if (in != null) p.load(in);
        } catch (IOException ignored) {
            // Ignore if config file not found - default values will be used
        }
        return p;
    }
    
    public static String getBaseUrl() {
        return CONFIG.getProperty("base.url", "http://localhost:8080");
    }
    
    public static String getApiBaseUrl() {
        return CONFIG.getProperty("api.base.url", "http://localhost:8080/api");
    }
    
    public static String getAdminUsername() {
        return CONFIG.getProperty("admin.username", "admin");
    }
    
    public static String getAdminPassword() {
        return CONFIG.getProperty("admin.password", "admin123");
    }
    
    public static String getTestUsername() {
        return CONFIG.getProperty("test.user.username", "testuser");
    }
    
    public static String getTestPassword() {
        return CONFIG.getProperty("test.user.password", "password123");
    }
    
    /**
     * Get or create API token for admin role.
     * Token is cached and reused across all tests.
     */
    public static String getAdminToken() {
        return getToken("admin", getAdminUsername(), getAdminPassword());
    }
    
    /**
     * Get or create API token for test user role.
     * Token is cached and reused across all tests.
     */
    public static String getTestUserToken() {
        return getToken("testuser", getTestUsername(), getTestPassword());
    }
    
    /**
     * Get or create API token for a specific role.
     */
    public static String getToken(String role, String username, String password) {
        // Return cached token if available
        if (API_TOKENS.containsKey(role)) {
            LOGGER.info("Using cached API token for role: {}", role);
            return API_TOKENS.get(role);
        }
        
        LOGGER.info("Authenticating via API for role: {}", role);
        String token = authenticateViaApi(username, password);
        
        if (token != null && !token.isEmpty()) {
            API_TOKENS.put(role, token);
            LOGGER.info("Cached new API token for role: {}", role);
        }
        
        return token;
    }
    
    /**
     * Authenticate via API and get JWT token.
     */
    private static String authenticateViaApi(String username, String password) {
        try {
            String authPayload = String.format(
                "{\"username\":\"%s\",\"password\":\"%s\"}", 
                username, password
            );
            
            String token = RestAssured.given()
                    .contentType("application/json")
                    .body(authPayload)
                    .post(getApiBaseUrl() + "/auth/login")
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("token");
            
            LOGGER.info("Successfully obtained API token for username: {}", username);
            return token;
        } catch (Exception e) {
            LOGGER.error("Failed to authenticate via API for username: {} - Error: {}", username, e.getMessage());
            return null;
        }
    }
    
    /**
     * Perform UI login and cache session cookies for admin.
     * Only performs login if not already cached.
     */
    public static void loginAsAdmin(WebDriver driver) {
        loginAsUser(driver, "admin", getAdminUsername(), getAdminPassword());
    }
    
    /**
     * Perform UI login and cache session cookies for test user.
     * Only performs login if not already cached.
     */
    public static void loginAsTestUser(WebDriver driver) {
        loginAsUser(driver, "testuser", getTestUsername(), getTestPassword());
    }
    
    /**
     * Perform UI login and cache session cookies for a specific role.
     * Only performs login if not already cached.
     */
    public static void loginAsUser(WebDriver driver, String role, String username, String password) {
        // If we already have a cached session and login is complete, reuse it
        if (LOGIN_COMPLETED.getOrDefault(role, false) && UI_SESSIONS.containsKey(role)) {
            LOGGER.info("Using cached UI session for role: {}", role);
            injectSessionCookies(driver, role);
            return;
        }
        
        LOGGER.info("Performing UI login for role: {} with username: {}", role, username);
        
        try {
            LoginPage loginPage = new LoginPage();
            loginPage.open();
            
            loginPage.enterUsername(username);
            loginPage.enterPassword(password);
            loginPage.clickLoginButton();
            
            // Wait for login to complete - SerenityScreenplay handles implicit waits
            
            // Collect and cache cookies
            Set<Cookie> cookies = driver.manage().getCookies();
            UI_SESSIONS.put(role, cookies);
            LOGIN_COMPLETED.put(role, true);
            
            LOGGER.info("Successfully logged in and cached session for role: {}", role);
        } catch (Exception e) {
            LOGGER.error("Failed to perform UI login for role: {} - Error: {}", role, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Inject cached session cookies into the driver.
     */
    public static void injectSessionCookies(WebDriver driver, String role) {
        Set<Cookie> cookies = UI_SESSIONS.get(role);
        if (cookies == null || cookies.isEmpty()) {
            LOGGER.warn("No cached cookies found for role: {}", role);
            return;
        }
        
        try {
            // Navigate to base URL first to set cookie domain
            driver.get(getBaseUrl());
            
            for (Cookie cookie : cookies) {
                try {
                    driver.manage().addCookie(cookie);
                    LOGGER.debug("Injected cookie: {}", cookie.getName());
                } catch (Exception e) {
                    LOGGER.warn("Failed to inject cookie {}: {}", cookie.getName(), e.getMessage());
                }
            }
            
            LOGGER.info("Successfully injected cached session cookies for role: {}", role);
        } catch (Exception e) {
            LOGGER.error("Error injecting session cookies for role: {}: {}", role, e.getMessage(), e);
        }
    }
    
    /**
     * Clear all cached sessions and tokens.
     * Useful for test cleanup.
     */
    public static void clearAllSessions() {
        UI_SESSIONS.clear();
        API_TOKENS.clear();
        LOGIN_COMPLETED.clear();
        LOGGER.info("Cleared all cached sessions and tokens");
    }
    
    /**
     * Clear cached session and token for a specific role.
     */
    public static void clearSession(String role) {
        UI_SESSIONS.remove(role);
        API_TOKENS.remove(role);
        LOGIN_COMPLETED.remove(role);
        LOGGER.info("Cleared cached session for role: {}", role);
    }
    
    /**
     * Check if a role has an active cached session.
     */
    public static boolean hasActiveSession(String role) {
        return LOGIN_COMPLETED.getOrDefault(role, false) && UI_SESSIONS.containsKey(role);
    }
    
    /**
     * Check if a role has a cached token.
     */
    public static boolean hasToken(String role) {
        return API_TOKENS.containsKey(role);
    }
}
