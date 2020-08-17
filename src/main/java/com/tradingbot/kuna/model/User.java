package com.tradingbot.kuna.model;


import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "date_registered")
    private Instant dateRegistered;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_state")
    private UserState userState;
}