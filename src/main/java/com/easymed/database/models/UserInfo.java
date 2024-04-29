package com.easymed.database.models;

/**
 * UserInfo Class to store user information
 *
 * @author Asif Ahammed
 * @since 1.0.0
 */
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
     * Constructor for Patient
     *
     * @param count      row count
     * @param userId     user id
     * @param name       name
     * @param email      email
     * @param phone      phone
     * @param address    address
     * @param bloodGroup blood group
     * @param dob        date of birth
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

    /**
     * Get the row count
     *
     * @return row count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Get the user id
     *
     * @return user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Get the name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the email
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the phone
     *
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Get the address
     *
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the blood group
     *
     * @return blood group
     */
    public String getBloodGroup() {
        return bloodGroup;
    }

    /**
     * Get the date of birth
     *
     * @return date of birth
     */
    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

}

