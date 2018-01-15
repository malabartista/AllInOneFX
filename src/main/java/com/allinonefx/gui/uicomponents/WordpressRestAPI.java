/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.gui.uicomponents;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.model.Page;
import com.afrozaar.wordpress.wpapi.v2.model.Title;
import com.afrozaar.wordpress.wpapi.v2.request.Request;
import com.afrozaar.wordpress.wpapi.v2.request.SearchRequest;
import com.afrozaar.wordpress.wpapi.v2.response.PagedResponse;
import com.allinonefx.controllers.MainController;
import com.allinonefx.models.User;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.ViewController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/StackPane.fxml", title = "Wordpress")
public class WordpressRestAPI {

    @FXML
    private StackPane root;
    @FXML
    private JFXTreeTableView<User> wordpressTreeTableView;
    @FXML
    private JFXTreeTableColumn<Page, String> titleColumn;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws IOException, Exception {
        MainController.lblTitle.setText("Wordpress");
        String baseUrl = "";
        String username = "";
        String password = "";
        boolean debug = true;

        final Wordpress client = ClientFactory.fromConfig(ClientConfig.of(baseUrl, username, password, false, debug));
        final PagedResponse<Page> response = client.search(SearchRequest.Builder.aSearchRequest(Page.class)
                .withUri(Request.PAGES)
                //                .withParam("filter[meta_key]", "baobab_indexed")
                //                .withParam("filter[meta_compare]", "NOT EXISTS") //RestTemplate takes care of escaping values ('space' -> '%20')
                .build());
        List<Page> lstPage = response.getList();
        List<Title> lstTitle = new ArrayList(); 
        for(int i=0;i<lstPage.size();i++){
            lstTitle.add(lstPage.get(i).getTitle());
        }
        ObservableList<Title> data = FXCollections.observableArrayList(lstTitle);
//        setupCellValueFactory(titleColumn, Page::getTitle().toString());
//        wordpressTreeTableView.setRoot(new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren));
        TableColumn<Title, String> crewNameCol = new TableColumn<Title, String>("Title");
        crewNameCol.setCellValueFactory(new PropertyValueFactory<Title, String>("rendered"));
        crewNameCol.setMinWidth(180);
        TableView<Title> table = new TableView<Title>(data);
        table.getColumns().addAll(crewNameCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        root.getChildren().add(table);
    }
}
