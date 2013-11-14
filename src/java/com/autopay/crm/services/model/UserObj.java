package com.autopay.crm.services.model;

import java.io.Serializable;

/**
 *
 * @author Judy
 */
public final class UserObj implements Serializable{
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String status;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" + "userName=" + userName + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", status=" + status + '}';
    }
    
}