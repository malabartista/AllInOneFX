/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.gui.uicomponents;

import io.datafx.controller.ViewController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.HTMLEditor;
import javax.annotation.PostConstruct;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import org.dockfx.demo.DockFX;


@ViewController(value = "/fxml/ui/StackPane.fxml", title = "DockFX")
public class DockFXController {
    
    // fxml
    @FXML
    public static StackPane root;

    /**
     * init fxml when loaded.
     *
     * @throws java.lang.Exception
     */
    @PostConstruct
    public void init() throws Exception {
        // create a dock pane that will manage our dock nodes and handle the layout
        DockPane dockPane = new DockPane();

        // create a default test node for the center of the dock area
        TabPane tabs = new TabPane();
        HTMLEditor htmlEditor = new HTMLEditor();
        try {
            htmlEditor.setHtmlText(new String(Files.readAllBytes(Paths.get("readme.html"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // empty tabs ensure that dock node has its own background color when floating
        tabs.getTabs().addAll(new Tab("Tab 1", htmlEditor), new Tab("Tab 2"), new Tab("Tab 3"));

        TableView<String> tableView = new TableView<String>();
        // this is why @SupressWarnings is used above
        // we don't care about the warnings because this is just a demonstration
        // for docks not the table view
        tableView.getColumns().addAll(new TableColumn<String, String>("A"),
                new TableColumn<String, String>("B"), new TableColumn<String, String>("C"));

        // load an image to caption the dock nodes
        Image dockImage = new Image(DockFX.class.getResource("docknode.png").toExternalForm());

        // create and dock some prototype dock nodes to the middle of the dock pane
        // the preferred sizes are used to specify the relative size of the node
        // to the other nodes
        // we can use this to give our central content a larger area where
        // the top and bottom nodes have a preferred width of 300 which means that
        // when a node is docked relative to them such as the left or right dock below
        // they will have 300 / 100 + 300 (400) or 75% of their previous width
        // after both the left and right node's are docked the center docks end up with 50% of the width
        DockNode tabsDock = new DockNode(tabs, "Tabs Dock", new ImageView(dockImage));
        tabsDock.setPrefSize(300, 100);
        tabsDock.dock(dockPane, DockPos.TOP);
        DockNode tableDock = new DockNode(tableView);
        // let's disable our table from being undocked
        tableDock.setDockTitleBar(null);
        tableDock.setPrefSize(300, 100);
        tableDock.dock(dockPane, DockPos.BOTTOM);

        // can be created and docked before or after the scene is created
        // and the stage is shown
        DockNode treeDock = new DockNode(generateRandomTree(), "Tree Dock", new ImageView(dockImage));
        treeDock.setPrefSize(100, 100);
        treeDock.dock(dockPane, DockPos.LEFT);
        treeDock = new DockNode(generateRandomTree(), "Tree Dock", new ImageView(dockImage));
        treeDock.setPrefSize(100, 100);
        treeDock.dock(dockPane, DockPos.RIGHT);
        
        root.getChildren().add(dockPane);
    }

    private TreeView<String> generateRandomTree() {
        // create a demonstration tree view to use as the contents for a dock node
        TreeItem<String> root = new TreeItem<String>("Root");
        TreeView<String> treeView = new TreeView<String>(root);
        treeView.setShowRoot(false);

        // populate the prototype tree with some random nodes
        Random rand = new Random();
        for (int i = 4 + rand.nextInt(8); i > 0; i--) {
            TreeItem<String> treeItem = new TreeItem<String>("Item " + i);
            root.getChildren().add(treeItem);
            for (int j = 2 + rand.nextInt(4); j > 0; j--) {
                TreeItem<String> childItem = new TreeItem<String>("Child " + j);
                treeItem.getChildren().add(childItem);
            }
        }

        return treeView;
    }
}
