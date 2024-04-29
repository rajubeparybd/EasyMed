package com.easymed.database.models;

public class UserInfo {

    private final Integer count;

    private final String userId;

    private final String name;

    private final String email;

    private final String phone;

    private final String address;

    private final String bloodGroup;

    private final String dob;

    private final String gender;

    /**
     * @param count
     * @param userId
     * @param name
     * @param email
     * @param phone
     * @param address
     * @param bloodGroup
     * @param dob
     * @param gender
     */
    public UserInfo(Integer count, String userId, String name, String email, String phone, String address, String bloodGroup, String dob, String gender) {
        this.count = count;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.dob = dob;
        this.gender = gender;
    }

    public Integer getCount() {
        return count;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

}

