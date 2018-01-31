package com.allinonefx.controllers.table;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.dao.FilmMapper;
import com.allinonefx.mybatis.MyBatisConnectionFactory;
import com.jfoenix.controls.*;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javax.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSession;

public class TreeTableViewController {

    SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
    FilmMapper mapper = sqlSession.getMapper(FilmMapper.class);

    public static final String PREFIX = "( ";
    public static final String POSTFIX = " )";

    @FXMLViewFlowContext
    public ViewFlowContext context;

    // editable table view
    @FXML
    public StackPane root;
    @FXML
    public Label lblTitle;
    @FXML
    public Label treeTableViewCount;
    @FXML
    public JFXButton treeTableViewAdd;
    @FXML
    public JFXButton treeTableViewEdit;
    @FXML
    public JFXButton treeTableViewRemove;
    @FXML
    public JFXTextField searchField;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        //title
        MainController.lblTitle.setText("Tree Table View");
        //locale
        setLocale();
    }

    public void setLocale() {
        searchField.promptTextProperty().bind(I18N.createStringBinding("text.search"));
    }

}
