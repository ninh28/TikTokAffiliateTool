package com.ninhquachhai.tiktoktool.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    private String email; // Sử dụng email làm ID duy nhất

    private String name;
    private String picture;
    private LocalDateTime lastLogin;
    private int reupCount;
    private LocalDateTime createdAt;
}
