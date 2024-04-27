package com.user.authentication.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCred {
  private String email;
  private String password;

}
