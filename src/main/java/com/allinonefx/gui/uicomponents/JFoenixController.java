/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.gui.uicomponents;

import com.allinonefx.controllers.MainController;
import static com.allinonefx.gui.uicomponents.DialogController.CONTENT_PANE;
import com.github.plushaze.traynotification.notification.Notification;
import com.github.plushaze.traynotification.notification.Notifications;
import com.github.plushaze.traynotification.notification.TrayNotification;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.validation.ValidationFacade;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/JFoenix.fxml", title = "Material Design Example")
public class JFoenixController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private StackPane root;
    @FXML
    private JFXButton notificationButton;
    @FXML
    private JFXComboBox<Label> jfxComboBox;
    @FXML
    private JFXDialog dialog;
    @FXML
    private JFXButton dialogButton;
    @FXML
    private JFXButton acceptButton;
    @FXML
    private JFXListView<?> list1;
    @FXML
    private JFXProgressBar progress1;
    @FXML
    private JFXProgressBar progress2;
    @FXML
    private JFXHamburger burger1;
    @FXML
    private JFXBadge badge1;
    @FXML
    private JFXSnackbar snackbar;
    private int count = 1;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {

        // Main Title
        MainController.lblTitle.setText("JFoenix");
        
        // Notification Button
        notificationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                notification(Pos.TOP_LEFT);
            }
        });

        // ComboBox
        jfxComboBox.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                ValidationFacade.validate(jfxComboBox);
            }
        });

        // Dialog
        root.getChildren().remove(dialog);

        dialogButton.setOnMouseClicked((e) -> {
            dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
            dialog.show((StackPane) context.getRegisteredObject(CONTENT_PANE));
        });

        acceptButton.setOnMouseClicked((e) -> dialog.close());

        // Icons
        burger1.setOnMouseClicked((e) -> {
            final Transition burgerAnimation = burger1.getAnimation();
            burgerAnimation.setRate(burgerAnimation.getRate() * -1);
            burgerAnimation.play();
        });
        snackbar.registerSnackbarContainer(root);
        badge1.setOnMouseClicked((click) -> {
            int value = Integer.parseInt(badge1.getText());
            if (click.getButton() == MouseButton.PRIMARY) {
                value++;
            } else if (click.getButton() == MouseButton.SECONDARY) {
                value--;
            }

            if (value == 0) {
                badge1.setEnabled(false);
            } else {
                badge1.setEnabled(true);
            }
            badge1.setText(String.valueOf(value));

            // trigger snackbar
            if (count++ % 2 == 0) {
                snackbar.fireEvent(new JFXSnackbar.SnackbarEvent("Toast Message " + count));
            } else if (count % 4 == 0) {
                snackbar.fireEvent(new JFXSnackbar.SnackbarEvent("Snackbar Message Persistant " + count,
                        "CLOSE",
                        3000,
                        true,
                        b -> snackbar.close()));
            } else {
                snackbar.fireEvent(new JFXSnackbar.SnackbarEvent("Snackbar Message " + count, "UNDO", 3000, false, (b) -> {
                }));
            }
        });

        // ProgressBar
        Timeline task = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(progress1.progressProperty(), 0),
                        new KeyValue(progress2.progressProperty(), 0),
                        new KeyValue(progress2.secondaryProgressProperty(), 0.5)),
                new KeyFrame(
                        Duration.seconds(1),
                        new KeyValue(progress2.secondaryProgressProperty(), 1)),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(progress1.progressProperty(), 1),
                        new KeyValue(progress2.progressProperty(), 1)));
        task.setCycleCount(Timeline.INDEFINITE);
        task.play();

        // List View
        list1.depthProperty().set(1);
    }
    
    private void notification(Pos pos) {
        // ControlsFX
//        final Image SMALL_GRAPHIC = 
//            new Image("controlsfx-logo.png");
//        
//        String text = "Hello World " + (count++) + "!";
//        
//        Node graphic = new ImageView(SMALL_GRAPHIC);
//        
//        Notifications notificationBuilder = Notifications.create()
//                .title("Title Text")
//                .text(text)
//                .graphic(graphic)
//                .hideAfter(Duration.seconds(10))
//                .position(pos)
//                .onAction(new EventHandler<ActionEvent>() {
//                    @Override public void handle(ActionEvent arg0) {
//                        System.out.println("Notification clicked on!");
//                    }
//                });
//        
//        notificationBuilder.show();
        

        // Tray Notification
        String title = "Congratulations sir";
        String message = "You've successfully created your first Tray Notification";
        Notification notification = Notifications.SUCCESS;
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotification(notification);
        tray.showAndWait();
    }
}
