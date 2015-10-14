package com.gauchoshout.android.data;

//Class that has attributes for user information

public class User {
    
    private final String id;
    private final String username;
    private final String email;
    private final String access;
    private final String details;
    private final String gender;
    
    public User(String id, String username, String email, String access,
            String details, String gender) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.access = access;
        this.details = details;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAccess() {
        return access;
    }

    public String getDetails() {
        return details;
    }

    public String getGender() {
        return gender;
    }
    
}
