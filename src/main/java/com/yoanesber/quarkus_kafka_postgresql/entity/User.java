package com.yoanesber.quarkus_kafka_postgresql.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 20, unique = true)
    private String userName;

    @Column(nullable = false, length = 150)
    private String password;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "firstname", nullable = false, length = 20)
    private String firstName;

    @Column(name = "lastname", length = 20)
    private String lastName;

    @Column(name = "is_enabled", nullable = false, columnDefinition = "boolean default true")
    private boolean isEnabled;

    @Column(name = "is_account_non_expired", nullable = false, columnDefinition = "boolean default true")
    private boolean isAccountNonExpired;

    @Column(name = "is_account_non_locked", nullable = false, columnDefinition = "boolean default true")
    private boolean isAccountNonLocked;

    @Column(name = "is_credentials_non_expired", nullable = false, columnDefinition = "boolean default true")
    private boolean isCredentialsNonExpired;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    @Column(name = "account_expiration_date", columnDefinition = "timestamp with time zone")
    private Instant accountExpirationDate;

    @Column(name = "credentials_expiration_date", columnDefinition = "timestamp with time zone")
    private Instant credentialsExpirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 20)
    private EUserType userType;

    @Column(name = "last_login", columnDefinition = "timestamp with time zone")
    private Instant lastLogin;

    @Column(name = "created_by", nullable = false, length = 20)
    private Long createdBy;

    @Column(name = "created_date", nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant createdDate;

    @Column(name = "updated_by", nullable = false, length = 20)
    private Long updatedBy;

    @Column(name = "updated_date", nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant updatedDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", email=" + email
                + ", firstName=" + firstName + ", lastName=" + lastName + ", isEnabled=" + isEnabled
                + ", isAccountNonExpired=" + isAccountNonExpired + ", isAccountNonLocked=" + isAccountNonLocked
                + ", isCredentialsNonExpired=" + isCredentialsNonExpired + ", isDeleted=" + isDeleted
                + ", accountExpirationDate=" + accountExpirationDate + ", credentialsExpirationDate="
                + credentialsExpirationDate + ", lastLogin=" + lastLogin + ", userType=" + userType
                + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy
                + ", updatedDate=" + updatedDate + "]";
    }
}
