/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.gui.uicomponents;

import com.allinonefx.controllers.MainController;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javax.annotation.PostConstruct;
import org.fxmisc.livedirs.LiveDirs;

/**
 *
 * @author lrodriguez
 */
@ViewController(value = "/fxml/ui/StackPane.fxml", title = "LiveDirsFX")
public class LiveDirsFXController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private StackPane root;

    enum ChangeSource {
        INTERNAL,
        EXTERNAL
    }

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws IOException {
        MainController.lblTitle.setText("LiveDirsFX");
        // create LiveDirs to watch a directory
        LiveDirs<ChangeSource> liveDirs = new LiveDirs<>(ChangeSource.EXTERNAL);
        Path dir = Paths.get(System.getProperty("user.home"), "Documents").toAbsolutePath();
        liveDirs.addTopLevelDirectory(dir);

        // use LiveDirs as a TreeView model
        TreeView<Path> treeView = new TreeView<>(liveDirs.model().getRoot());
        treeView.setShowRoot(false);

        // handle external changes
        liveDirs.model().modifications().subscribe(m -> {
            if (m.getInitiator() == ChangeSource.EXTERNAL) {
                // handle external modification, e.g. reload the modified file
//                reload(m.getPath());
            } else {
                // modification done by this application, no extra action needed
            }
        });

        // Use LiveDirs's I/O facility to write to the filesystem,
        // in order to be able to distinguish between internal and external changes.
//        Path file = dir.resolve("some/file.txt");
//        liveDirs.io().saveUTF8File(file, "Hello text file!", ChangeSource.INTERNAL);
        // clean up
        liveDirs.dispose();

        // stop DirWatcher's thread
        root.getChildren().addAll(treeView);

    }
}
