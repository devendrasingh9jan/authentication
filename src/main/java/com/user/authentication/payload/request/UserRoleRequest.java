package com.user.authentication.payload.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserRoleRequest {
    private Integer userId; // for which user roles need to be updated.
    private String roleName; // role name that needs to be updated.
}
