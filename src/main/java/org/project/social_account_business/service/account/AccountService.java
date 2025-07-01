package org.project.social_account_business.service.account;

import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.account.AccountDto;
import org.project.social_account_business.form.account.CreateAccountForm;
import org.project.social_account_business.form.account.UpdateAccountForm;
import org.project.social_account_business.model.Account;
import org.project.social_account_business.model.criteria.AccountCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    /**
     * Find an account by email.
     *
     * @param email the email of the account
     * @return the account with the specified email
     */
    Account findAccountByEmail(String email);

    /**
     * Get an account by its ID.
     *
     * @param id the ID of the account
     * @return the account with the specified ID
     */
    AccountDto getAccountById(Long id);

    /**
     * Find an account by its ID.
     *
     * @param id the ID of the account
     * @return the account with the specified ID
     */
    Account findById(Long id);

    /**
     * Find an account by username.
     *
     * @param username the username of the account
     * @return the account with the specified username
     */
    Account findAccountByUsername(String username);

    /**
     * Check if an account exists by email.
     *
     * @param email the email to check
     * @return true if an account with the specified email exists, false otherwise
     */
    boolean existsAccountByEmail(String email);

    /**
     * Create a new admin account.
     *
     * @param createAccountForm the form containing the details for the new admin account
     */
    void createAdminAccount(CreateAccountForm createAccountForm);

    /**
     * Create a new user account.
     *
     * @param createAccountForm the form containing the details for the new user account
     */
    void createUserAccount(CreateAccountForm createAccountForm);

    /**
     * Update an existing account.
     *
     * @param updateAccountForm the form containing the updated details for the account
     */
    void updateAccount(UpdateAccountForm updateAccountForm);

    /**
     * Delete an account by its ID.
     *
     * @param id the ID of the account to delete
     */
    void deleteAccount(Long id);

    /**
     * Get account details as a DTO by email.
     *
     * @param email the email of the account
     * @return the account details as a DTO
     */
    AccountDto getAccountDto(String email);

    /**
     * Retrieve a paginated list of accounts based on criteria.
     *
     * @param accountCriteria the criteria for filtering accounts
     * @param pageable        the pagination information
     * @return a response containing the list of account DTOs and pagination details
     */
    ResponseListDto<List<AccountDto>> accountList(AccountCriteria accountCriteria, Pageable pageable);

    /**
     * Save an account entity to the database.
     *
     * @param account the account entity to save
     */
    void save(Account account);

    /**
     * Disable an account by its ID.
     * @param accountId
     */
    void deactivateAccount(long accountId);

    /**
     * Enable an account by its ID.
     * @param accountId
     */
    void activateAccount(long accountId);

    /**
     * Update the account balance for a given account ID.
     *
     * @param accountId the ID of the account to update
     * @param amount    the amount to add or subtract from the account balance
     */
    void updateAccountBalance(long accountId, double amount, String transactionCode);
}
