package org.example.musicstream.dtos.response.user;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse implements Serializable {
    private Long id;
    private String username;
    private String email;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private boolean isActive;
}
