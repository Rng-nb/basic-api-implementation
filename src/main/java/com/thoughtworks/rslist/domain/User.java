package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull
    @Size(max = 8)
    @JsonProperty("user_name")
    @JsonAlias("name")
    private String name;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty("user_age")
    @JsonAlias("age")
    private int age;

    @NotNull
    @JsonProperty("user_gender")
    @JsonAlias("gender")
    private String gender;

    @Email
    @JsonProperty("user_email")
    @JsonAlias("email")
    private String email;

    @Pattern(regexp = "1\\d{10}")
    @JsonProperty("user_phone")
    @JsonAlias("phone")
    private String phone;
    private int voteNum;
}
