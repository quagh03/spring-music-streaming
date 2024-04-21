package org.example.musicstream.dtos.requests.user;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest implements Serializable {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Retype password is required")
    private String retypePassword;

    @NotBlank(message = "Email is required")
    private String email;

    @Temporal(TemporalType.DATE)
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    public boolean isPasswordMatch() {
        return password.equals(retypePassword);
    }
}
