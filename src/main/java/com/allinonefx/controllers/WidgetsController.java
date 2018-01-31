/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.controllers;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.form.RegisterController;
import com.allinonefx.controllers.table.CategoryTableViewController;
import com.allinonefx.controllers.table.CountryTableViewController;
import com.allinonefx.controllers.table.CustomerTableViewController;
import com.allinonefx.controllers.table.FilmTableViewController;
import com.allinonefx.dao.CategoryMapper;
import com.allinonefx.dao.CountryMapper;
import com.allinonefx.dao.CustomerMapper;
import com.allinonefx.dao.CustomerMapperExtend;
import com.allinonefx.dao.FilmMapper;
import com.allinonefx.gui.uicomponents.TreeTableViewController;
import com.allinonefx.model.Category;
import com.allinonefx.model.Customer;
import com.allinonefx.mybatis.MyBatisConnectionFactory;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSession;

@ViewController(value = "/fxml/dashboard/Widgets.fxml", title = "Dashboard")
public class WidgetsController {
    @FXMLViewFlowContext
    private ViewFlowContext context;

    SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
    @FXML
    private Pane paneCustomers;
    @FXML
    private Label lblCustomers;
    @FXML
    private Label totalCustomers;
    @FXML
    private Pane paneFilms;
    @FXML
    private Label lblFilms;
    @FXML
    private Label totalFilms;
    @FXML
    private Pane paneCategories;
    @FXML
    private Label lblCategories;
    @FXML
    private Label totalCategories;
    @FXML
    private Pane paneCountries;
    @FXML
    private Label lblCountries;
    @FXML
    private Label totalCountries;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        setTotals();
        setLocale();
    }

    public void setTotals() {
        //flow
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlow.withGlobalLink(paneCustomers.getId(), CustomerTableViewController.class);
        contentFlow.withGlobalLink(paneFilms.getId(), FilmTableViewController.class);
        contentFlow.withGlobalLink(paneCategories.getId(), CategoryTableViewController.class);
        contentFlow.withGlobalLink(paneCountries.getId(), CountryTableViewController.class);
        
        // Customers
        CustomerMapperExtend customerMapper = sqlSession.getMapper(CustomerMapperExtend.class);
        totalCustomers.setText(Long.toString(customerMapper.countByExample(null)));
        paneCustomers.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("paneCustomers");
            } catch (VetoException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Films
        FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);
        totalFilms.setText(Long.toString(filmMapper.countByExample(null)));
        paneFilms.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("paneFilms");
            } catch (VetoException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Categories
        CategoryMapper categoryMapper = sqlSession.getMapper(CategoryMapper.class);
        totalCategories.setText(Long.toString(categoryMapper.countByExample(null)));
        paneCategories.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("paneCategories");
            } catch (VetoException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Countries
        CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
        totalCountries.setText(Long.toString(countryMapper.countByExample(null)));
        paneCountries.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("paneCountries");
            } catch (VetoException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void setLocale() {
        lblCustomers.textProperty().bind(I18N.createStringBinding("label.customers"));
        lblFilms.textProperty().bind(I18N.createStringBinding("label.films"));
        lblCategories.textProperty().bind(I18N.createStringBinding("label.categories"));
        lblCountries.textProperty().bind(I18N.createStringBinding("label.countries"));
    }

}
