/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.controllers;

import com.allinonefx.config.DbHandler;
import static com.allinonefx.gui.uicomponents.DialogController.CONTENT_PANE;
import com.allinonefx.gui.uicomponents.TreeTableViewController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/Register.fxml", title = "Register")
public class RegisterController {

    @FXML
    private JFXTextField txtFname;
    @FXML
    private JFXTextField txtLname;
    @FXML
    private JFXTextField txtMobile;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXTextField txtLocation;
    private static double progress1 = 0;
    private static double progress2 = 0;
    private static double progress3 = 0;
    private static double progress4 = 0;
    private static double progress5 = 0;
    private static double progress6 = 0;
    private static double progress7 = 0;
    private static double progress8 = 0;
    private static double progress9 = 0;
    private static double progress10 = 0;
    @FXML
    private ProgressBar progressPersonal;
    @FXML
    private JFXRadioButton rdMale;
    @FXML
    private ToggleGroup gender;
    @FXML
    private JFXRadioButton rdFemale;
    private ObservableList<String> depart_lists;
    @FXML
    private JFXTextField txtCourseName;
    @FXML
    private JFXRadioButton rdDegree;
    @FXML
    private ToggleGroup level;
    @FXML
    private JFXRadioButton rdDiploma;
    @FXML
    private JFXRadioButton rdCertificate;
    @FXML
    private TextField currentTimeTextfield;
    @FXML
    private JFXTextField txtAmount;
    @FXML
    private JFXTextField txtMobile111;
    @FXML
    private Label lblComplete;
    private Connection connection;
    private PreparedStatement pst;
    private DbHandler handler;
    @FXML
    private JFXComboBox<String> comboDepartmenT;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXDialog dialog;
    @FXML
    private JFXButton dialogButton;
    @FXML
    private JFXButton acceptButton;
    @FXML
    private AnchorPane parentPane;
    private FlowHandler contentFlowHandler;
    private Flow contentFlow;
    @FXMLViewFlowContext
    private ViewFlowContext context;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws IOException {
        MainController.lblTitle.setText("Register");
        handler = new DbHandler();
        updateProgress();
        setDepartmentsToCombo();
        // flow
        contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlow.withGlobalLink(btnSave.getId(), TreeTableViewController.class);
        // Dialog
        parentPane.getChildren().remove(dialog);
    }

    private void updateProgress() {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        //progressPersonal.setProgress(0.00);
        double sum_progress = progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9;

        txtFname.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    progress1 = 0.1;
                } else {
                    progress1 = 0.0;
                }

                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });

        txtEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    progress2 = 0.1;
                } else {
                    progress2 = 0.0;
                }
                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });

        txtLname.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    progress3 = 0.1;

                } else {
                    progress3 = 0.0;

                }
                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });

        txtMobile.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 1) {
                    progress4 = 0.1;

                } else {
                    progress4 = 0.0;

                }
                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });

        txtLocation.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    progress5 = 0.1;

                } else {
                    progress5 = 0.0;

                }
                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });
        //Course name
        txtCourseName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    progress6 = 0.1;

                } else {
                    progress6 = 0.0;

                }
                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });
        // Amount paid
        txtAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    progress7 = 0.1;

                } else {
                    progress7 = 0.0;

                }
                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });
        //Gender Radio buttons
        rdMale.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue) {
                    progress8 = 0.1;

                }
                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });
        rdFemale.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue) {
                    progress8 = 0.1;

                }

                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });

        //Levels Radio buttons
        rdDegree.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue) {
                    progress9 = 0.1;

                }
                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });
        rdDiploma.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue) {
                    progress9 = 0.1;

                }
                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });
        rdCertificate.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue) {
                    progress9 = 0.1;

                }
                double sum = (progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(String.valueOf(sum * 100) + "% complete");

            }
        });

        comboDepartmenT.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue,
                    String newValue) {
                if (!newValue.isEmpty()) {
                    progress10 = 0.1;
                } else {
                    progress10 = 0.0;
                }
                double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9);
                progressPersonal.setProgress(sum);
                lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            }
        });

    }

    private void setDepartmentsToCombo() {

        connection = handler.getConnection();
        String query = "SELECT department.`name` FROM department";
        depart_lists = FXCollections.observableArrayList();
        try (PreparedStatement pst1 = connection.prepareStatement(query)) {
            ResultSet resultSet = pst1.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                depart_lists.add(name);
            }
            comboDepartmenT.getItems().addAll(depart_lists);
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void validatetext() {
        txtAmount.textProperty().addListener((ObservableValue<? extends String> observable,
                String oldValue,
                String newValue) -> {

        });
    }

    @FXML
    public void clearFields() {
        txtAmount.setText(null);
        txtCourseName.setText(null);
        txtEmail.setText(null);
        txtFname.setText(null);
        txtLname.setText(null);
        txtLocation.setText(null);
        txtMobile.setText(null);
        txtMobile111.setText(null);

    }

    @FXML
    private void editStudent(ActionEvent event) {
    }

    @FXML
    private void saveStudent(ActionEvent event) throws SQLException {
        // CHECK IF ALL FILEDS ARE FILLED UP
        if (progressPersonal.getProgress() < 0.9) {
            dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
            dialog.show((StackPane) context.getRegisteredObject(CONTENT_PANE));
            return;
        }
        String insert = "INSERT INTO students(fname,lname,location,gender,email,phone,"
                + "`level`,department,course,paid) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        connection = handler.getConnection();
        pst = connection.prepareStatement(insert);
        pst.setString(1, txtFname.getText().toUpperCase());
        pst.setString(2, txtLname.getText().toUpperCase());
        pst.setString(3, txtLocation.getText());
        pst.setString(4, getGender());
        pst.setString(5, txtEmail.getText());
        pst.setString(6, txtMobile.getText());
        pst.setString(7, getLevel());
        pst.setString(8, comboDepartmenT.getSelectionModel().getSelectedItem().toUpperCase());
        pst.setString(9, txtCourseName.getText());
        pst.setString(10, txtAmount.getText());

        int success = pst.executeUpdate();
        if (success == 1) {
            clearFields();
            JFXSnackbar fXSnackbar = new JFXSnackbar(parentPane);
            fXSnackbar.show("New student saved successfully", 3000);

        }

        try {
            contentFlowHandler.handle("btnSave");
        } catch (VetoException ex) {
            Logger.getLogger(TreeTableViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlowException ex) {
            Logger.getLogger(TreeTableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String getLevel() {
        String lvl = "";
        if (rdCertificate.isSelected()) {
            lvl = "CERTIFICATE";
        } else if (rdDegree.isSelected()) {
            lvl = "DEGREE";
        } else if (rdDiploma.isSelected()) {
            lvl = "DIPLOMA";
        }
        return lvl;

    }

    private String getGender() {
        String gdr = "";
        if (rdMale.isSelected()) {
            gdr = "MALE";
        } else if (rdFemale.isSelected()) {
            gdr = "FEMALE";
        }
        return gdr;

    }

}
