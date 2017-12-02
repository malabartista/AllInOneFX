package com.allinonefx.gui.uicomponents;

import io.datafx.controller.ViewController;
import java.io.File;
import java.net.MalformedURLException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/MediaView.fxml", title = "MediaView")
public class MediaViewController {

    @FXML
    private AnchorPane root;
    @FXML
    private MediaView mediaView;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws MalformedURLException {
        final File f = new File(getClass().getClassLoader().getResource("media/big_buck_bunny.mp4").getFile());
        final Media m = new Media(f.toURI().toURL().toString());
        final MediaPlayer mp = new MediaPlayer(m);
        final MediaView mv = new MediaView(mp);
        
        mediaView.setMediaPlayer(mp);
        
        DoubleProperty mvw = mv.fitWidthProperty();
        DoubleProperty mvh = mv.fitHeightProperty();
        mvw.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        mvh.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        
        

//        mv.setPreserveRatio(true);
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
