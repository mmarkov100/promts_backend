package com.promts.promts_test_server.promts_backend.service.Auth;

import com.promts.promts_test_server.promts_backend.dto.Auth.outbound.SuccessLoginDTO;
import com.promts.promts_test_server.promts_backend.dto.Shared.outbound.SuccessMessageDTO;
import com.promts.promts_test_server.promts_backend.repository.Auth.FirebaseAuthRepository;
import com.promts.promts_test_server.promts_db.dto.user.outbound.AppUserDto;
import com.promts.promts_test_server.promts_db.service.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("firebase")
@RequiredArgsConstructor
public class FirebaseAuthService implements AuthService {

    private final FirebaseAuthRepository repo;
    private final AppUserService appUserService;

    /** Проверка токена + получение uid */
    @Override
    public String getUidFirebaseAndValidate(String authorization) {
        return repo.verify(authorization);
    }

    /** Регистрация: Firebase → PostgreSQL */
    @Override
    public SuccessMessageDTO registerNewUser(String email, String password) {

        String uid = repo.registerUser(email, password).getUidFirebase();

        AppUserDto created = appUserService.createUser(
                AppUserDto.builder().email(email).uidFirebase(uid).build()
        );

        return new SuccessMessageDTO(true,
                "Пользователь создан, id=" + created.getId());
    }

    /** Жив ли токен? */
    @Override
    public SuccessMessageDTO actualiseToken(String authorization) {
        repo.verify(authorization);
        return new SuccessMessageDTO(true, "ok");
    }

    /** Логин: отдаём клиенту idToken Firebase */
    @Override
    public SuccessLoginDTO loginUserGetJWTToken(String email, String password) {
        String idToken = repo.signInAndGetToken(email, password);
        return new SuccessLoginDTO(true, "Вход выполнен", "Bearer " + idToken);
    }
}
