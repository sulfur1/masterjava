package ru.javaops.masterjava.xml.model;

import java.util.Objects;

public class User {
    private final Integer id;
    private final String fullName;
    private final String email;
    private final UserFlag flag;


    public User(String fullName, String email, UserFlag flag) {
        this(null, fullName, email, flag);
    }

    public User(Integer id, String fullName, String email, UserFlag flag) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.flag = flag;
    }

    public Integer getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public UserFlag getFlag() {
        return flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId().equals(user.getId()) && getFullName().equals(user.getFullName()) && getEmail().equals(user.getEmail()) && getFlag() == user.getFlag();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFullName(), getEmail(), getFlag());
    }
}
