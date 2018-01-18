/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

/*
 * User Model class
 */
public class User extends RecursiveTreeObject<User> {

    public BooleanProperty checkbox = new SimpleBooleanProperty();
    public ObjectProperty<ImageView> userPhoto = new SimpleObjectProperty();
    public SimpleIntegerProperty id = new SimpleIntegerProperty();
    public StringProperty userName = new SimpleStringProperty();
    public StringProperty password = new SimpleStringProperty();
    public StringProperty firstName = new SimpleStringProperty();
    public StringProperty lastName = new SimpleStringProperty();
    public SimpleIntegerProperty mobile = new SimpleIntegerProperty();
    public StringProperty email = new SimpleStringProperty();
    public StringProperty location = new SimpleStringProperty();
    public StringProperty gender = new SimpleStringProperty();
    public StringProperty level = new SimpleStringProperty();
    public StringProperty department = new SimpleStringProperty();
    public StringProperty course = new SimpleStringProperty();

    public User() {
    }

    public User(String userName, String password, int mobile) {
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.mobile = new SimpleIntegerProperty(mobile);
    }

    public User(String userName, String password, String firstName, String lastName, int mobile, String email, String location, String gender, String level, String department, String course) {
        this.checkbox = new SimpleBooleanProperty(true);
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.mobile = new SimpleIntegerProperty(mobile);
        this.email = new SimpleStringProperty(email);
        this.location = new SimpleStringProperty(location);
        this.gender = new SimpleStringProperty(gender);
        this.level = new SimpleStringProperty(level);
        this.department = new SimpleStringProperty(department);
        this.course = new SimpleStringProperty(course);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String username) {
        this.userName.set(username);
    }

    public StringProperty userNameProperty() {
        return userName;
    }
    
    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }
    
    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstname) {
        this.firstName.set(firstname);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastname) {
        this.lastName.set(lastname);
    }
    
    public StringProperty lastNameProperty() {
        return lastName;
    }
    
    public int getMobile() {
        return mobile.get();
    }

    public void setMobile(int mobile) {
        this.mobile.set(mobile);
    }

    public SimpleIntegerProperty mobileProperty() {
        return mobile;
    }
    
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }
    
    public String getLocation() {
        return location.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public StringProperty locationProperty() {
        return location;
    }
    
    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty genderProperty() {
        return gender;
    }
    
    public String getLevel() {
        return level.get();
    }

    public void setLevel(String level) {
        this.level.set(level);
    }

    public StringProperty levelProperty() {
        return level;
    }
    
    public String getDepartment() {
        return department.get();
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public StringProperty departmentProperty() {
        return department;
    }
    
    public String getCourse() {
        return course.get();
    }

    public void setCourse(String course) {
        this.course.set(course);
    }

    public StringProperty courseProperty() {
        return course;
    }

    public ObjectProperty userPhotoProperty() {
        return userPhoto;
    }

    public BooleanProperty checkboxProperty() {
        return checkbox;
    }

}
