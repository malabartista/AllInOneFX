/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.controllers;

import com.allinonefx.config.DbHandler;
import com.allinonefx.dao.UserDao;
import static com.allinonefx.gui.uicomponents.DialogController.CONTENT_PANE;
import com.allinonefx.gui.uicomponents.TreeTableViewController;
import com.allinonefx.models.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.BackAction;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/Register.fxml", title = "Register")
public class RegisterController {

    @FXML
    private StackPane root;

    // form
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
    @FXML
    private ToggleGroup gender;
    @FXML
    private JFXRadioButton rdMale;
    @FXML
    private JFXRadioButton rdFemale;
    @FXML
    private ToggleGroup level;
    @FXML
    private JFXRadioButton rdDegree;
    @FXML
    private JFXRadioButton rdDiploma;
    @FXML
    private JFXRadioButton rdCertificate;
    @FXML
    private JFXComboBox<String> comboDepartmenT;
    private ObservableList<String> depart_lists;
    @FXML
    private JFXTextField txtCourseName;
    @FXML
    private JFXDatePicker txtBirthdate;
    @FXML
    private JFXTextField txtAmount;

    @FXML
    @BackAction
    private JFXButton btnBack;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnSave;

    // dialog
    @FXML
    private JFXDialog dialog;
    @FXML
    private JFXButton acceptButton;

    // progressbar
    @FXML
    private Label lblComplete;
    @FXML
    private JFXProgressBar progressPersonal;
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

    // database
    private Connection connection;
    private PreparedStatement pst;
    private DbHandler handler;

    // DataFX - Flow    
    @FXMLViewFlowContext
    private ViewFlowContext context;
    private FlowHandler contentFlowHandler;
    private Flow contentFlow;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws IOException {
        // title
        MainController.lblTitle.setText("Register");
        // database handler
        handler = new DbHandler();
        // form init
        handleValidation();
        updateProgress();
        setDepartmentsToCombo();
        setTextFields();
        // flow
        contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlow.withGlobalLink(btnSave.getId(), TreeTableViewController.class);
        // dialog
        root.getChildren().remove(dialog);
        acceptButton.setOnMouseClicked((e) -> dialog.close());
    }

    private void updateProgress() {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        progressPersonal.prefWidthProperty().bind(root.widthProperty());
        progressPersonal.setProgress(0.00);
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

    private void setTextFields() {
        // force the field to be numeric only
        txtMobile.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (txtMobile.getText().length() > 9) {
                    String s = txtMobile.getText().substring(0, 9);
                    txtMobile.setText(s);
                }
                if (!newValue.matches("\\d*")) {
                    txtMobile.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private void setDepartmentsToCombo() {
        connection = handler.getConnection();
        String query = "SELECT name FROM department";
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
        progressPersonal.setProgress(0);
        txtFname.setText(null);
        txtLname.setText(null);
        txtMobile.setText(null);
        txtEmail.setText(null);
        txtLocation.setText(null);
        rdMale.setSelected(false);
        rdFemale.setSelected(false);
        rdDegree.setSelected(false);
        rdDiploma.setSelected(false);
        rdCertificate.setSelected(false);
        txtCourseName.setText(null);
        txtBirthdate.setValue(null);
        txtAmount.setText(null);
    }

    @FXML
    private void editUser(ActionEvent event) {
    }

    @FXML
    private void saveUser(ActionEvent event) throws SQLException {
        // check if all fields are filled up
        if (progressPersonal.getProgress() < 0.9) {
            dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
            dialog.show((StackPane) context.getRegisteredObject(CONTENT_PANE));
            return;
        }

        //User u = new User(txtFname.getText().toUpperCase(), txtLname.getText().toUpperCase(), Integer.parseInt(txtMobile.getText()));
        User u = new User(txtFname.getText().toUpperCase(), txtLname.getText().toUpperCase(), Integer.parseInt(txtMobile.getText()), txtEmail.getText().toUpperCase(), txtLocation.getText().toUpperCase(), getGender(), getLevel(), comboDepartmenT.getSelectionModel().getSelectedItem().toUpperCase(), txtCourseName.getText().toUpperCase());
        UserDao dao = new UserDao();
        boolean success = dao.insertUser(u);
        if (success) {
            clearFields();
            JFXSnackbar fXSnackbar = new JFXSnackbar(root);
            fXSnackbar.show("New user saved successfully", 3000);
        }

        try {
            contentFlowHandler.handle("btnSave");
        } catch (VetoException ex) {
            Logger.getLogger(TreeTableViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FlowException ex) {
            Logger.getLogger(TreeTableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleValidation() {
        RequiredFieldValidator fieldValidator = new RequiredFieldValidator();
        fieldValidator.setMessage("Input required");
        fieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        txtFname.getValidators().add(fieldValidator);
        txtFname.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldVal, Boolean newVal) -> {
            if (!newVal) {
                txtFname.validate();
            }
        });
        RequiredFieldValidator fieldValidator2 = new RequiredFieldValidator();
        fieldValidator2.setMessage("Input required");
        fieldValidator2.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        txtLname.getValidators().add(fieldValidator2);
        txtLname.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtLname.validate();
            }
        });
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
