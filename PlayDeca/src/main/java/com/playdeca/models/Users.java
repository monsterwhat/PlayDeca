package com.playdeca.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Al
 */

@Data
@Entity
@Table(name = "Users")
public class Users extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String username;
    private String password;
    private String UUID;
    private String email;
    private String userGroup;
    private Integer coins = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ServerLogs> serverLogs;

    @Column(name = "RegistrationDate", columnDefinition = "TIMESTAMP")
    private LocalDateTime registrationDate;
}
