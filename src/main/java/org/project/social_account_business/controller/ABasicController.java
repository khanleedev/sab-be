package org.project.social_account_business.controller;

import org.project.social_account_business.dto.account.AccountDto;
import org.project.social_account_business.exception.UnauthorizationException;
import org.project.social_account_business.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class ABasicController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AccountService accountService;

    /**
     * Get the current logged-in user's AccountDto based on JWT token.
     *
     * @return AccountDto of the currently authenticated user
     */
    protected AccountDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizationException("[ABasicController] ❌ No authentication information available");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            log.debug("Authenticated user: {}", email);
            return accountService.getAccountDto(email);
        } else {
            throw new UnauthorizationException("[ABasicController] ❌ Invalid user principal");
        }
    }
}
