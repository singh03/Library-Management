package com.project.Library.Management.member.dto;

import jakarta.validation.constraints.NotBlank;

public class MemberDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
