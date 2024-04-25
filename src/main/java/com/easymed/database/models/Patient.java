package com.easymed.database.models;

public class Patient {

    private final Integer count;

    private final String userId;

    private final String name;

    private final String email;

    private final String phone;

    private final String address;

    private final String bloodGroup;

    private final String dob;

    /**
     * @param count
     * @param userId
     * @param name
     * @param email
     * @param phone
     * @param address
     * @param bloodGroup
     * @param dob
     */
    public Patient(Integer count, String userId, String name, String email, String phone, String address, String bloodGroup, String dob) {
        this.count = count;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.dob = dob;
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

}

