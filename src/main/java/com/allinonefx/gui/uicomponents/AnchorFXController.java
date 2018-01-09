package com.allinonefx.gui.uicomponents;

import com.allinonefx.controllers.MainController;
import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.stations.DockStation;
import com.anchorage.system.AnchorageSystem;
import io.datafx.controller.ViewController;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/AnchorFX.fxml", title = "AnchorFX")
public class AnchorFXController {

    @FXML
    private StackPane root;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws IOException {
        MainController.lblTitle.setText("AnchorFX");

        // Content for Anchorage
        final File f = new File(getClass().getClassLoader().getResource("media/big_buck_bunny.mp4").getFile());
        final Media m = new Media(f.toURI().toURL().toString());
        final MediaPlayer mp = new MediaPlayer(m);
        final MediaView mv = new MediaView(mp);
        mp.setOnReady(new Runnable() {
            // run comment
            @Override
            public void run() {
                int w = mp.getMedia().getWidth();
                int h = mp.getMedia().getHeight();
                mv.setFitHeight(h - 100.0);
            }
        });

        mv.setPreserveRatio(true);
        mp.play();

        // Anchorage System
        DockStation station = AnchorageSystem.createStation();

        DockNode node1 = AnchorageSystem.createDock("Not floatable", generateRandomTree());
        node1.dock(station, DockNode.DockPosition.LEFT);
        node1.floatableProperty().set(false);

        DockNode node2 = AnchorageSystem.createDock("Not resizable", mv);
        node2.dock(station, DockNode.DockPosition.RIGHT);
        node2.resizableProperty().set(false);

        DockNode node3 = AnchorageSystem.createDock("Not maximizable", generateRandomTree());
        node3.dock(station, DockNode.DockPosition.TOP);
        node3.maximizableProperty().set(false);

        DockNode node4 = AnchorageSystem.createDock("Not closeable", generateRandomTree());
        node4.dock(station, DockNode.DockPosition.BOTTOM);
        node4.closeableProperty().set(false);

        AnchorageSystem.installDefaultStyle();

        root.getChildren().addAll(station);
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
