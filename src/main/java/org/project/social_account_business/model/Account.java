package org.project.social_account_business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.validation.SystemId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = TablePrefix.PREFIX + "accounts", indexes = {
        @Index(name = "idx_account_username", columnList = "username"),
        @Index(name = "idx_account_email", columnList = "email"),
        @Index(name = "index_account_email_phone_no", columnList = "email, phone_no"),
        @Index(name = "idx_account_kind", columnList = "kind"),
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Account extends Auditable<String> implements UserDetails {

    @Id
    @SystemId
    private Long id;

    private Integer kind;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private Double balance;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "reset_pwd_time")
    private Date resetPwdTime;

    @Column(name = "attempt_forget_pwd")
    private Integer attemptCode;

    @Column(name = "attempt_login")
    private Integer attemptLogin;

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TokenInfo> tokens = new ArrayList<>();
    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PaymentTransaction> paymentTransactions = new ArrayList<>();
    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Order> transactions = new ArrayList<>();
    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public Account(Long id, int role, String username, String email) {
    }


    // --- Spring Security Methods ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String role;

        if (Objects.equals(this.getKind(), BetaConstant.USER_KIND_ADMIN)) {
            role = "ROLE_ADMIN";
        } else {
            role = "ROLE_USER";
        }

        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.attemptLogin == null || this.attemptLogin < 5;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getAccountName() {
        return this.username;
    }
}