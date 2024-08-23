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

    // 전화번호
//     private String phonenum;

    //이메일
    @Column(nullable = true)
    private String email;

    private LocalDateTime regDate;

    @Column(nullable = false)
    @ColumnDefault("500.0")
    private int reliability;

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
        }
    }


}
