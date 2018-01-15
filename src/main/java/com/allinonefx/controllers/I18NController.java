/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.controllers;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.stage.Stage;


public abstract class I18NController {
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage){this.primaryStage = primaryStage;}
    public Stage getPrimaryStage(){return primaryStage;}

    public final void changeLanguage(I18NLanguage language) throws IOException
    {
        StateBundle stateBundle = new StateBundle();
        onSaveState(stateBundle);

        Locale locale = language.getLocale();
        Locale.setDefault(locale);

        ResourceBundle resourceBundle = getResourceBundle(locale);
        URL fxml = getFXMLResource();
        FXMLLoader loader = new FXMLLoader(fxml, resourceBundle);
        Parent root = loader.load();
        NodeOrientation nodeOrientation = language.getNodeOrientation();
        root.setNodeOrientation(nodeOrientation);

//        primaryStage.setScene(new Scene(root));
//        primaryStage.sizeToScene();

//        I18NController newController = loader.getController();
//        newController.setPrimaryStage(primaryStage);

        onLoadState(this, language, resourceBundle, stateBundle);
    }

    protected abstract ResourceBundle getResourceBundle(Locale locale);
    protected abstract URL getFXMLResource();
    protected abstract void onSaveState(StateBundle stateBundle);
    protected abstract void onLoadState(I18NController newController, I18NLanguage newLanguage, ResourceBundle resourceBundle, StateBundle stateBundle);

    public static interface I18NLanguage
    {
        Locale getLocale();
        NodeOrientation getNodeOrientation();
    }

    public static class StateBundle
    {
        private Map<String, Object> sMap = new HashMap<>();

        StateBundle(){}

        public void putData(String key, Object value)
        {
            sMap.put(key, value);
        }

        public <T> T getDate(String key, Class<T> type)
        {
            return type.cast(sMap.get(key));
        }
    }
}

