package org.project.social_account_business.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.project.social_account_business.dto.ApiMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController {
    @GetMapping
    public ResponseEntity<ApiMessageDto<String>> error(HttpServletRequest request) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        return new ResponseEntity<>(apiMessageDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
