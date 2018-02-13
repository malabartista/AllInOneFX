/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.controllers;

import com.allinonefx.config.I18N;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public final class ToolbarPopupController {

    @FXML
    private JFXListView<?> toolbarPopupList;
    @FXML
    private StackPane profileStackPane;
    @FXML
    private JFXComboBox<Label> cmbLanguage;
    @FXML
    private Label lblEnglish;
    @FXML
    private Label lblSpanish;
    @FXML
    private Label lblContact;
    @FXML
    private Label lblExit;

    public void initialize() throws Exception {
        if (profileStackPane != null) {
            Locale userLocale = I18N.getLocale();
            switch (userLocale.toString()) {
                case "en":
                    cmbLanguage.getSelectionModel().select(0);
                    break;
                case "es_ES":
                    cmbLanguage.getSelectionModel().select(1);
                    break;
            }
            cmbLanguage.setOnAction((e) -> {
                try {
                    System.out.println("Committed: " + cmbLanguage.getSelectionModel().getSelectedItem());
                    switch (cmbLanguage.getSelectionModel().getSelectedItem().getId()) {
                        case "lblEnglish":
                            switchLanguage(Locale.ENGLISH);
                            break;
                        case "lblSpanish":
                            switchLanguage(new Locale("es", "ES"));
                            break;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            Image flagGB = new Image(getClass().getResourceAsStream("/icons/gb.png"));
            Image flagES = new Image(getClass().getResourceAsStream("/icons/es.png"));
            lblEnglish.setGraphic(new ImageView(flagGB));
            lblSpanish.setGraphic(new ImageView(flagES));
            lblExit.setOnMouseClicked(e -> Platform.exit());
        }
    }

    /**
     * sets the given Locale in the I18N class and keeps count of the number of
     * switches.
     *
     * @param locale the new local to set
     */
    private void switchLanguage(Locale locale) throws IOException {
        I18N.setLocale(locale);
//        profilePopup.hide();
    }

    @FXML
    private void submit() throws IOException {
        Scene scene = MainController.root.getScene();
        final ObservableList<String> stylesheets = scene.getStylesheets();

        if (toolbarPopupList != null) {
            if (toolbarPopupList.getSelectionModel().getSelectedIndex() == 0) {
                stylesheets.removeAll(ToolbarPopupController.class.getResource("/css/theme-red.css").toExternalForm());
                stylesheets.addAll(ToolbarPopupController.class.getResource("/css/theme-blue.css").toExternalForm());
            } else if (toolbarPopupList.getSelectionModel().getSelectedIndex() == 1) {
                stylesheets.removeAll(ToolbarPopupController.class.getResource("/css/theme-blue.css").toExternalForm());
                stylesheets.addAll(ToolbarPopupController.class.getResource("/css/theme-red.css").toExternalForm());
            }
        }
    }
}
