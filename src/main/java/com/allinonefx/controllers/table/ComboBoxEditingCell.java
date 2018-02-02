/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.controllers.table;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeTableCell;

public class ComboBoxEditingCell<Object> extends TreeTableCell<Object, String> {

    private final ObservableList<String> data;
    
    private ComboBox<String> comboBox;

    ComboBoxEditingCell(ObservableList<String> list) {
        this.data = list;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createComboBox();
            setText(null);
            setGraphic(comboBox);
        }
    }
    
    public void commitEdit(String value) {
        super.commitEdit(value);
        
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getElement());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else if (isEditing()) {
            if (comboBox != null) {
                comboBox.setValue(getElement());
            }
            setText(getElement());
            setGraphic(comboBox);
        } else {
            setText(getElement());
            setGraphic(null);
        }
    }

    private void createComboBox() {
        comboBox = new ComboBox<>(data);
        comboBoxConverter(comboBox);
        comboBox.valueProperty().set(getElement());
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        comboBox.setOnAction((e) -> {
            System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem());
            commitEdit(comboBox.getSelectionModel().getSelectedItem());
        });
//            comboBox.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(comboBox.getSelectionModel().getSelectedItem());
//                }
//            });
    }

    private void comboBoxConverter(ComboBox<String> comboBox) {
        // Define rendering of the list of values in ComboBox drop down. 
        comboBox.setCellFactory((c) -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
        });
    }

    private String getElement() {
        return getItem() == null ? new String("") : getItem();
    }
}
