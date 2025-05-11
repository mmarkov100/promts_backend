package com.promts.promts_test_server.promts_backend.service.Auth;

import com.promts.promts_test_server.promts_backend.config.MockBackendConfig;
import com.promts.promts_test_server.promts_backend.dto.Auth.outbound.SuccessLoginDTO;
import com.promts.promts_test_server.promts_backend.dto.Shared.outbound.SuccessMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.User.inbound.UserModelDTO;
import com.promts.promts_test_server.promts_backend.repository.Auth.AuthRepository;
import com.promts.promts_test_server.promts_db.dto.user.outbound.AppUserDto;
import com.promts.promts_test_server.promts_db.service.user.AppUserService;
import com.promts.promts_test_server.shared.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Profile({"dbmock"})
public class DBMockAuthService implements AuthService {

    private static final Logger logger = Logger.getLogger(DBMockAuthService.class.getName());
    private final AuthRepository authRepository;
    private final AppUserService appUserService;
    // private final MockBackendConfig mockBackendConfig; // Keep if needed for other configs

    @Autowired
    public DBMockAuthService(AuthRepository authRepository, AppUserService appUserService, MockBackendConfig mockBackendConfig) {
        this.authRepository = authRepository;
        this.appUserService = appUserService;
        // this.mockBackendConfig = mockBackendConfig;
    }

    /**
     * Validates the authorization token (which is the uidFirebase in this mock)
     * and returns the uidFirebase if valid.
     * The authorization string IS the uidFirebase.
     */
    @Override
    public String getUidFirebaseAndValidate(String authorization) {
        authorization = authorization.split(" ")[1];
        logger.log(Level.INFO, "Attempting to validate token (uidFirebase): " + authorization);
        if (authorization == null || authorization.isEmpty()) {
            logger.log(Level.WARNING, "Authorization token is empty or null.");
            throw new GlobalException("TOKEN_VALIDATION_ERROR", "Токен авторизации отсутствует.");
        }

        try {
            // The token itself is the uidFirebase for lookup
            AppUserDto user = appUserService.findByUidFirebase(authorization);
            // Check if a user was found and the UID matches the token
            if (user != null && Objects.equals(user.getUidFirebase(), authorization)) {
                logger.log(Level.INFO, "Token validated successfully for uidFirebase: +", user.getUidFirebase());
                return user.getUidFirebase(); // Return the validated uidFirebase
            } else {
                logger.log(Level.WARNING, "Token validation failed. Token: {0}. User found: {1}", new Object[]{authorization, user != null});
                throw new GlobalException("TOKEN_VALIDATION_ERROR", "Токен авторизации недействителен или пользователь не найден.");
            }
        } catch (Exception e) { // Catch potential exceptions if user not found by appUserService
            logger.log(Level.WARNING, "Exception during token validation for token: " + authorization, e);
            throw new GlobalException("TOKEN_VALIDATION_ERROR", "Ошибка проверки токена: " + e.getMessage());
        }
    }

    @Override
    public SuccessMessageDTO registerNewUser(String email, String password) {
        logger.log(Level.INFO, "Attempting to register new user with email: {0}", email);
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            logger.log(Level.WARNING, "Registration attempt with empty email or password.");
            throw new GlobalException("REGISTRATION_ERROR", "Отсутствует логин или пароль.");
        }

        // 1. Check if user already exists (using email as uidFirebase for lookup)
        try {
            appUserService.findByUidFirebase(email);
            // If findByUidFirebase does not throw an exception, user exists.
            logger.log(Level.WARNING, "Registration attempt for existing email: {0}", email);
            throw new GlobalException("REGISTRATION_ERROR", "Пользователь с таким email уже существует.");
        } catch (Exception e) {
            // This is expected if the user does not exist. Proceed with registration.
            logger.log(Level.INFO, "User with email {0} not found, proceeding with registration.", email);
        }

        // 2. Delegate to AuthRepository to perform the registration via HTTP call
        // DBMockAuthRepository will set uidFirebase = email
        UserModelDTO registeredUserDTO = authRepository.registerUser(email);

        if (registeredUserDTO != null && Objects.equals(registeredUserDTO.getEmail(), email) && Objects.equals(registeredUserDTO.getUidFirebase(), email)) {
            logger.log(Level.INFO, "User registered successfully via repository (mock): {0}", email);
            return new SuccessMessageDTO(true, "Успешно создан аккаунт (мок)");
        } else {
            logger.log(Level.SEVERE, "Failed to register user via repository or DTO mismatch for email: {0}. Returned DTO: {1}", new Object[]{email, registeredUserDTO});
            throw new GlobalException("FAILED_REGISTRATION", "Не удалось зарегистрировать пользователя (мок).");
        }
    }

    /**
     * Checks if the provided token (uidFirebase) is active/valid.
     * The authorization string IS the uidFirebase.
     */
    @Override
    public SuccessMessageDTO actualiseToken(String authorization) {
        authorization = authorization.split(" ")[1];
        logger.log(Level.INFO, "Attempting to actualise token (uidFirebase): {0}", authorization);
        if (authorization == null || authorization.isEmpty()) {
            logger.log(Level.WARNING, "Token actualization attempt with empty or null token.");
            throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Токен авторизации отсутствует.");
        }
        try {
            AppUserDto user = appUserService.findByUidFirebase(authorization); // The token is the uidFirebase
            if (user != null && Objects.equals(user.getUidFirebase(), authorization)) {
                logger.log(Level.INFO, "Token actualized successfully for UID: {0}", authorization);
                return new SuccessMessageDTO(true, "Токен действителен (мок)");
            } else {
                logger.log(Level.WARNING, "Token actualization failed. Token: {0}. User found: {1}", new Object[]{authorization, user != null});
                throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Токен авторизации недействителен или пользователь не найден.");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception during token actualization for token: " + authorization, e);
            throw new GlobalException("TOKEN_ACTUALISE_ERROR", "Ошибка проверки токена: " + e.getMessage());
        }
    }

    @Override
    public SuccessLoginDTO loginUserGetJWTToken(String email, String password) {
        logger.log(Level.INFO, "Attempting to login user with email: {0}", email);
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            logger.log(Level.WARNING, "Login attempt with empty email or password.");
            throw new GlobalException("LOGIN_ERROR", "Отсутствует логин или пароль.");
        }

        // In this mock, the password is not actually checked.
        // We find the user by email, which is also their uidFirebase.
        try {
            AppUserDto user = appUserService.findByUidFirebase(email); // email is the uidFirebase
            if (user != null && Objects.equals(user.getEmail(), email)) {
                // Login successful, return uidFirebase as the "token"
                logger.log(Level.INFO, "User logged in successfully (mock): {0}. Returning uidFirebase as token: {1}", new Object[]{email, user.getUidFirebase()});
                return new SuccessLoginDTO(true, "Успешный вход в аккаунт (мок)", user.getUidFirebase());
            } else {
                logger.log(Level.WARNING, "Login failed for email (user not found or email mismatch): {0}", email);
                throw new GlobalException("LOGIN_ERROR", "Неверный логин или пароль (мок).");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception during login for email: " + email, e);
            throw new GlobalException("LOGIN_ERROR", "Ошибка входа: Нет такого пользователя");
        }
    }
}