package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"messages"})
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = true)
    private String introduction;

    //이메일
    @Column(nullable = true)
    private String email;

    private LocalDateTime regDate;

    @Column(nullable = false)
    @ColumnDefault("500.0")
    private int reliability;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int memberStatus;

    // OAuth2 Client
    @Column
    private String provider;
    private String providerId;

    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER)
    @JsonManagedReference
    @ToString.Exclude
    private List<Message> messages;

    private String role;

    @PrePersist
    protected void onCreate() {
        this.regDate = LocalDateTime.now();
        if (this.reliability == 0) {
            this.reliability = 500;
        }if (this.memberStatus == 0) {
            this.memberStatus = 0;  // 회원가입 시 기본값 설정
        }
    }
}
