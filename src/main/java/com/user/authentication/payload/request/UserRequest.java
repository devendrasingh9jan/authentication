package com.user.authentication.payload.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserRequest {
    private String userEmail;
    private String firstName;
    private String lastName;
    private String entraId;
    private Boolean isActive;
    private Integer companyId; // for which company user needs to be created
    private String companyLevelRole;

}
