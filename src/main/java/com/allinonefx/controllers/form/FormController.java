/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.controllers.form;

import com.allinonefx.config.I18N;
import com.allinonefx.datafx.AbstractViewController;
import com.allinonefx.mybatis.MyBatisConnectionFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXProgressBar;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.BackAction;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.apache.ibatis.session.SqlSession;

public class FormController extends AbstractViewController {

    //mybatis
    SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

    // datafx
    @FXMLViewFlowContext
    public ViewFlowContext context;
    public FlowHandler contentFlowHandler;
    public Flow contentFlow;

    @FXML
    public StackPane root;
    @FXML
    @BackAction
    public JFXButton btnBack;
    @FXML
    public JFXButton btnClear;
    @FXML
    public JFXButton btnEdit;
    @FXML
    public JFXButton btnSave;
    @FXML
    public JFXDialog dialog;
    @FXML
    public JFXButton acceptButton;
    @FXML
    public Label lblComplete;
    @FXML
    public JFXProgressBar progressForm;

    public void setLocale() {
        btnBack.textProperty().bind(I18N.createStringBinding("button.back"));
        btnClear.textProperty().bind(I18N.createStringBinding("button.clear"));
        btnEdit.textProperty().bind(I18N.createStringBinding("button.edit"));
        btnSave.textProperty().bind(I18N.createStringBinding("button.save"));
    }

    public void clearFields() {
        lblComplete.setText("0% " + I18N.get("complete"));
        progressForm.setProgress(0);
    }

    public void setProgress(double sum) {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        progressForm.setProgress(sum);
        lblComplete.setText(decimalFormat.format(sum * 100) + "% " + I18N.get("complete"));
    }
}
