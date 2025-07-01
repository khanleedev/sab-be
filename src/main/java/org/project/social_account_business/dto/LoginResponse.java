package org.project.social_account_business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

@Data
@AllArgsConstructor
@Getter
public class LoginResponse {
    private String accessToken;
    private HttpHeaders headers;
    private Integer kind;
}
