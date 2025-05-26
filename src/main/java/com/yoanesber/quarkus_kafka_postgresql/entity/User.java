package com.yoanesber.quarkus_kafka_postgresql.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
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

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone default now()")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at", columnDefinition = "timestamp with time zone")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "deleted_at", columnDefinition = "timestamp with time zone")
    private LocalDateTime deletedAt;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_roles", // nama tabel join
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public List<Role> roles = new ArrayList<>();

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", email=" + email
                + ", firstName=" + firstName + ", lastName=" + lastName + ", isEnabled=" + isEnabled
                + ", isAccountNonExpired=" + isAccountNonExpired + ", isAccountNonLocked=" + isAccountNonLocked
                + ", isCredentialsNonExpired=" + isCredentialsNonExpired + ", isDeleted=" + isDeleted
                + ", accountExpirationDate=" + accountExpirationDate + ", credentialsExpirationDate="
                + credentialsExpirationDate + ", lastLogin=" + lastLogin + ", userType=" + userType
                + ", createdBy=" + createdBy + ", createdAt=" + createdAt + ", updatedBy=" + updatedBy
                + ", updatedAt=" + updatedAt + ", deletedBy=" + deletedBy + ", deletedAt=" + deletedAt +
                ", roles=" + roles + "]";
    }
}
