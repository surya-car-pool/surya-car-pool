package com.surya.carpool.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // your MySQL table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;       // regName

    @Column(nullable = false, unique = true)
    private String email;      // regEmail

    @Column(nullable = false, unique = true)
    private String phone;      // regPhone

    @Column(nullable = false)
    private String password;   // encoded regPassword

    @Column(nullable = false)
    private boolean enabled = true;

    public User() {}

    // getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
