package com.save_help.Save_Help.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//자체 회원가입 구현
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private int age;
    private String gender;

}
