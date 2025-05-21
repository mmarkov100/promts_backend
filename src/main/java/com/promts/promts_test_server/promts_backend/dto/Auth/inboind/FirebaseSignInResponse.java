package com.promts.promts_test_server.promts_backend.dto.Auth.inboind;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/** Часть ответа identitytoolkit: берём только idToken */
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirebaseSignInResponse {

    @JsonProperty("idToken")     // точное имя поля в JSON
    private String idToken;
}
