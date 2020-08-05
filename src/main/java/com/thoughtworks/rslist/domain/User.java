package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

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

    private int voteNum = 10;

    public User(String name, int age, String gender, String email, String phone) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonIgnore
    public int getVoteNum() {
        return voteNum;
    }

    @JsonIgnore
    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
