/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane holderPane;
    @FXML
    private ScrollPane contentScrollPane;
    @FXML
    private JFXButton btnHome;
    @FXML
    private JFXButton btnPricing;
    @FXML
    private JFXButton btnContacts;
    @FXML
    private JFXButton btnWidgets;
    @FXML
    private JFXButton btnProfile;
    @FXML
    private JFXButton btnAlerts;

    AnchorPane contacts, alerts, pricing, profiles, widgets, controls;
    @FXML
    private JFXButton btnControls;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Load all fxmls in a cache
        try {
            contacts = FXMLLoader.load(getClass().getResource("/fxml/dashboard/Contacts.fxml"));
            alerts = FXMLLoader.load(getClass().getResource("/fxml/dashboard/Alerts.fxml"));
            pricing = FXMLLoader.load(getClass().getResource("/fxml/dashboard/Pricing.fxml"));
            profiles = FXMLLoader.load(getClass().getResource("/fxml/dashboard/Profiles.fxml"));
            widgets = FXMLLoader.load(getClass().getResource("/fxml/dashboard/Widgets.fxml"));
            controls = FXMLLoader.load(getClass().getResource("/fxml/dashboard/Controls.fxml"));
            setNode(pricing);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Set selected node to a content holder
    private void setNode(Node node) {
        holderPane.getChildren().clear();
        //ScrollPane sp = new ScrollPane();
        //contentScrollPane.setContent(node);

        //holderPane.getChildren().add((ScrollPane) contentScrollPane);
        holderPane.getChildren().add((Node) node);
//        contentScrollPane.setFitToHeight(true);
//        // Always show vertical scroll bar
//        contentScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
//        // Horizontal scroll bar is only displayed when needed
//        contentScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    @FXML
    private void switchPricing(ActionEvent event) {
        setNode(pricing);
    }

    @FXML
    private void switchContacts(ActionEvent event) {
        setNode(contacts);
    }

    @FXML
    private void switchWidget(ActionEvent event) {
        setNode(widgets);
    }

    @FXML
    private void switchProfile(ActionEvent event) {
        setNode(profiles);
    }

    @FXML
    private void switchAlert(ActionEvent event) {
        setNode(alerts);
    }

    @FXML
    private void switchControls(ActionEvent event) {
        setNode(controls);
    }

}
