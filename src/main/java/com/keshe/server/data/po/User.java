package com.keshe.server.data.po;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    private String phone;
    private String email;
    private String role; // USER / ADMIN

    private String nickname;
    private String avatar;//头像
    private String gender; 
    private String bio;//个签
}