package com.kma.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data //toString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @JsonProperty("userName")
    @NotBlank(message = "User name is required")
    private String userName;

    @NotBlank(message = "Password can not be blank")
    private String password;

    public @NotBlank(message = "User name is required") String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank(message = "User name is required") String userName) {
        this.userName = userName;
    }

    public @NotBlank(message = "Password can not be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password can not be blank") String password) {
        this.password = password;
    }
}
