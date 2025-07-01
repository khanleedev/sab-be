package org.project.social_account_business.dto;

import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiMessageDto<T> {
    private Boolean result = true;
    private String code = null;
    private T data = null;
    private String message = null;
    private String firebaseUrl;
    private String urlBase;
    public ApiMessageDto(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
