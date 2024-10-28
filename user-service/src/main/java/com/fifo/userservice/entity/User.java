package com.fifo.userservice.entity;

import com.fifo.userservice.service.CryptoConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Entity
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Convert(converter = CryptoConverter.class)
    private String name;

    @Convert(converter = CryptoConverter.class)
    private String email;
    
    private String password;

    @Convert(converter = CryptoConverter.class)
    private String address;

    @Convert(converter = CryptoConverter.class)
    private String phone;
    private String roles = "ROLE_USER";

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public List<String> getRoles() {
        return Arrays.stream(roles.split(",")).toList();
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
