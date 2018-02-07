package com.allinonefx.controllers.table;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.mybatis.MyBatisConnectionFactory;
import com.jfoenix.controls.*;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javax.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSession;

public class TreeTableViewController {

    SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

    @FXMLViewFlowContext
    public ViewFlowContext context;
    public FlowHandler contentFlowHandler;
    public Flow contentFlow;

    public static final String PREFIX = "( ";
    public static final String POSTFIX = " )";

    @FXML
    public StackPane root;
    @FXML
    public Label lblTableTitle;
    @FXML
    public Label lblTableCount;
    @FXML
    public JFXButton btnTableAdd;
    @FXML
    public JFXButton btnTableEdit;
    @FXML
    public JFXButton btnTableRemove;
    @FXML
    public JFXTextField txtTableSearch;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        MainController.lblTitle.setText("Tree Table View");
        setLocale();
    }

    public void setLocale() {
        txtTableSearch.promptTextProperty().bind(I18N.createStringBinding("text.search"));
    }

}
