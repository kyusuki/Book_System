package com.Gao.web.dto;

import com.Gao.entity.User;

public class UserDto {

    private String username;
    private String id;
    private String phone;
    private int userType;

    public static UserDto fromEntity(User u) {
        if (u == null) {
            return null;
        }
        UserDto d = new UserDto();
        d.setUsername(u.getUsername());
        d.setId(u.getId());
        d.setPhone(u.getPhone());
        d.setUserType(u.getUserType());
        return d;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
