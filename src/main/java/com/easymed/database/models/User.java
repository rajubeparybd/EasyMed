package com.easymed.database.models;

import java.util.HashMap;

/**
 * User class is used to store user data and provide methods to access and modify the data. It is a singleton class.
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class User {

    private static final User INSTANCE = new User();

    protected Integer id;

    protected String name;

    protected String email;

    protected String role;
    protected String profilePic;

    /**
     * Get the singleton instance of the User class
     *
     * @return User instance
     */
    public static User getInstance() {
        return INSTANCE;
    }

    /**
     * Get the user profile picture
     *
     * @return profile picture
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Set the user profile picture
     *
     * @param profilePic profile picture
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * Get the user data
     *
     * @return HashMap of user data
     */
    public HashMap<String, String> getUserData() {
        HashMap<String, String> user = new HashMap<>();
        user.put("id", id.toString());
        user.put("name", name);
        user.put("email", email);
        user.put("role", role);
        user.put("picture", profilePic);
        return user;
    }

    /**
     * Set the user data
     *
     * @param name  user name
     * @param email user email
     */
    public void setUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Set the user data
     *
     * @param name  user name
     * @param email user email
     * @param role  user role
     */
    public void setUser(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    /**
     * Set the user data
     *
     * @param id    user id
     * @param name  user name
     * @param email user email
     * @param role  user role
     */
    public void setUser(Integer id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    /**
     * Get the user id
     *
     * @return user id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the user id
     *
     * @param id user id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the name of the user
     *
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the user
     *
     * @param name user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the user email
     *
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user email
     *
     * @param email user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user role
     *
     * @return user role
     */
    public String getRole() {
        return role;
    }

    /**
     * Set the user role
     *
     * @param role user role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Get the string representation of the user object
     *
     * @return string representation of the user object
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}