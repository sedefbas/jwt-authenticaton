package com.sedef.blogum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.util.Set;

@Data
@Entity()
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 45)
    @NotEmpty
    private String firstName;

    @NotEmpty
    @Column(length = 45)
    private String lastName;

    @NotEmpty
    @Column(length = 45)
    private String userName;

    @NotEmpty
    @Column(length = 255)
    private String password;

    private Blob photo;
    private Boolean accountNonExpired;
    private Boolean isEnabled;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> authorities;

}
