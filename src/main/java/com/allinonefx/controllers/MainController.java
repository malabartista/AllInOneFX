package com.allinonefx.controllers;

import com.allinonefx.config.I18N;
import com.allinonefx.datafx.ExtendedAnimatedFlowContainer;
import com.allinonefx.model.Staff;
import com.allinonefx.utils.ImageUtils;
import com.jfoenix.controls.*;
import io.datafx.controller.ViewConfiguration;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import static io.datafx.controller.flow.container.ContainerAnimations.FADE;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Transition;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

@ViewController(value = "/fxml/Main.fxml", title = "Material Design Example")
public final class MainController {

    // datafx - flow
    @FXMLViewFlowContext
    private ViewFlowContext context;
    private FlowHandler contentFlowHandler;
    public Flow contentFlow;

    @FXML
    public static StackPane root;
    @FXML
    public static Label lblTitle;
    @FXML
    private ImageView btnHome;
    @FXML
    private StackPane btnDrawerLeft;
    @FXML
    private StackPane btnDrawerRight;
    @FXML
    private StackPane btnCalendar;
    @FXML
    private StackPane btnGMaps;
    @FXML
    private JFXButton btnProfile;
    @FXML
    private ImageView profileImage;
    @FXML
    private StackPane titleBurgerContainer;
    @FXML
    private JFXHamburger titleBurger;
    @FXML
    private StackPane btnOptions;
    @FXML
    private JFXRippler optionsRippler;
    @FXML
    private JFXDrawersStack drawersStack;
    @FXML
    private JFXDrawer drawerLeft;
    @FXML
    private JFXDrawer drawerRight;
    @FXML
    private JFXBadge badgeNotification;
    @FXML
    public static JFXSnackbar snackbar;
    private int count = 1;
    private JFXPopup toolbarPopup;
    private JFXPopup profilePopup;

    /**
     * init fxml when loaded.
     *
     * @throws java.lang.Exception
     */
    @PostConstruct
    public void init() throws Exception {
        //sets
        setFlow();
        setToolbarActions();
        setNotifications();
        setProfileButton();
    }

    private void setFlow() throws FlowException {
        ViewConfiguration viewConfig = new ViewConfiguration();
        viewConfig.setResources(ResourceBundle.getBundle("lang.message", I18N.getLocale()));
        contentFlow = new Flow(DashboardController.class, viewConfig);
        contentFlowHandler = new FlowHandler(contentFlow, context, viewConfig);
        context.register("ContentFlowHandler", contentFlowHandler);
        context.register("ContentFlow", contentFlow);
        final Duration containerAnimationDuration = Duration.millis(320);
        drawersStack.setContent(contentFlowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration, FADE)));
        context.register("ContentPane", drawersStack.getContent());
        // side controller will add links to the content flow
        Flow sideMenuFlow = new Flow(SideMenuController.class);
        final FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(context);
        drawerLeft.setSidePane(sideMenuFlowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration,
                FADE)));
        drawerRight.setSidePane(sideMenuFlowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration,
                FADE)));
        setFlowLinks();
    }

    private void setFlowLinks() {

    }

    private void setToolbarActions() {
        btnDrawerLeft.addEventHandler(MOUSE_PRESSED, e -> drawersStack.toggle(drawerLeft));
        btnDrawerRight.addEventHandler(MOUSE_PRESSED, e -> drawersStack.toggle(drawerRight));
        btnHome.setOnMouseClicked(e -> {
            try {
                contentFlowHandler.handle("dashboard");
            } catch (VetoException | FlowException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnCalendar.setOnMouseClicked(e -> {
            try {
                contentFlowHandler.handle("calendarfx");
            } catch (VetoException | FlowException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnGMaps.setOnMouseClicked(e -> {
            try {
                contentFlowHandler.handle("gmapsfx");
            } catch (VetoException | FlowException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnProfile.setOnMouseClicked((e) -> {
            ResourceBundle bundle = ResourceBundle.getBundle("lang.message", I18N.getLocale());
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/ui/popup/ProfilePopup.fxml"), bundle);
            loader.setController(new ToolbarPopupController());
            try {
                profilePopup = new JFXPopup(loader.load());
                profilePopup.show(btnProfile,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.RIGHT,
                        0,
                        45);
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnOptions.setOnMouseClicked((e) -> {
            ResourceBundle bundle = ResourceBundle.getBundle("lang.message", I18N.getLocale());
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/ui/popup/MainPopup.fxml"), bundle);
            loader.setController(new ToolbarPopupController());
            try {
                toolbarPopup = new JFXPopup(loader.load());
                toolbarPopup.show(btnOptions,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.RIGHT,
                        -12,
                        15);
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        drawerLeft.setOnDrawerOpening(e -> {
            final Transition animation = titleBurger.getAnimation();
            animation.setRate(1);
            animation.play();
        });
        drawerLeft.setOnDrawerClosing(e -> {
            final Transition animation = titleBurger.getAnimation();
            animation.setRate(-1);
            animation.play();
        });
    }

    private void setNotifications() {
        snackbar.registerSnackbarContainer(root);
        badgeNotification.setOnMouseClicked((click) -> {
            int value = Integer.parseInt(badgeNotification.getText());
            if (click.getButton() == MouseButton.PRIMARY) {
                value++;
            } else if (click.getButton() == MouseButton.SECONDARY) {
                value--;
            }

            if (value == 0) {
                badgeNotification.setEnabled(false);
            } else {
                badgeNotification.setEnabled(true);
            }
            badgeNotification.setText(String.valueOf(value));

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
    }

    public void setProfileButton() throws IOException {
        Staff staff = (Staff) context.getRegisteredObject("User");
        btnProfile.setText(staff.getFirst_name() + " " + staff.getLast_name());
        InputStream in = new ByteArrayInputStream(staff.getPicture());
        BufferedImage imageRounded = ImageUtils.makeRoundedCorner(ImageIO.read(in), 160);
        Image image = SwingFXUtils.toFXImage(imageRounded, null);
        profileImage.setImage(image);
    }
}
