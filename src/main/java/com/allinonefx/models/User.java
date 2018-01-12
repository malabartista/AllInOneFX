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
    public BooleanProperty checkbox = new SimpleBooleanProperty();

    public User() {
    }
    
    public User(String userName, String password, int mobile) {
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.mobile = new SimpleIntegerProperty(mobile);
    }

    public User(String firstName, String lastName, int mobile, String email, String location, String gender, String level, String department, String course) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.mobile = new SimpleIntegerProperty(mobile);
        this.email = new SimpleStringProperty(email);
        this.location = new SimpleStringProperty(location);
        this.gender = new SimpleStringProperty(gender);
        this.level = new SimpleStringProperty(level);
        this.department = new SimpleStringProperty(department);
        this.course = new SimpleStringProperty(course);
        this.checkbox = new SimpleBooleanProperty(true);

    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }
    
    public StringProperty userNameProperty() {
        return userName;
    }
    
    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public SimpleIntegerProperty mobileProperty() {
        return mobile;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty locationProperty() {
        return location;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public StringProperty levelProperty() {
        return level;
    }

    public StringProperty departmentProperty() {
        return department;
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
