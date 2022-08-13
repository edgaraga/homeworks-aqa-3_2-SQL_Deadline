package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.sql.DbInteraction;
import ru.netology.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;

public class AuthorizationTest {
    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @AfterAll
    public static void cleanDb() {
        DbInteraction.deleteTables();
    }

    @Test
    public void shouldAuthorization() {
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validData(authInfo);
        verificationPage.validVerify(DbInteraction.getCode());
    }

    @Test
    public void shouldGetErrorIfEnteredInvalidPassword() {
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getInvalidPasswordAuthInfo();
        loginPage.invalidData(authInfo);
        loginPage.getErrorIfInvalidData();
    }

    @Test
    public void shouldGetErrorIfEnteredInvalidLogin() {
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getInvalidLoginAuthInfo();
        loginPage.invalidData(authInfo);
        loginPage.getErrorIfInvalidData();
    }

    @Test
    public void shouldGetErrorIfEnteredWrongCode() {
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validData(authInfo);
        val code = DataHelper.getWrongVerificationCode();
        verificationPage.invalidVerify(code);
        verificationPage.getErrorIfInvalidSmsCode();
    }

    @Test
    public void shouldGetErrorIfEnteredWrongPasswordThreeTimes() {
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getInvalidPasswordAuthInfo();
        loginPage.invalidData(authInfo);
        loginPage.getErrorIfInvalidData();
        loginPage.cleaning();
        loginPage.invalidData(authInfo);
        loginPage.getErrorIfInvalidData();
        loginPage.cleaning();
        loginPage.invalidData(authInfo);
        loginPage.getBlockedMessage();
    }
}
