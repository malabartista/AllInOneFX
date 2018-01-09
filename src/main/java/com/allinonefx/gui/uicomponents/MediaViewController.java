package com.allinonefx.gui.uicomponents;

import io.datafx.controller.ViewController;
import java.io.File;
import java.net.MalformedURLException;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/MediaView.fxml", title = "SigaFX MediaView")
public class MediaViewController {

    @FXML
    private StackPane root;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws MalformedURLException {
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
        mp.setOnEndOfMedia(new Runnable() {
            public void run() {
                //Code to run
//                dialog = new JFXDialog();
//                dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
//                dialog.show(root);
            }
        });
        root.getChildren().addAll(mv);
    }

}
