package org.example.musicstream.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{
    private String username;

    @Column(name = "hashed_password")
    private String password;

    private String email;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;
}
