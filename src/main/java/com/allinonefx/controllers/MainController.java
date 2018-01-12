package com.allinonefx.controllers;

import com.allinonefx.MainDemo;
import com.allinonefx.datafx.ExtendedAnimatedFlowContainer;
import com.allinonefx.gui.uicomponents.JFoenixController;
import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import io.datafx.controller.ViewConfiguration;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import static io.datafx.controller.flow.container.ContainerAnimations.SWIPE_LEFT;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/Main.fxml", title = "Material Design Example")
public final class MainController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private StackPane root;
    @FXML
    private StackPane titleBurgerContainer;
    @FXML
    private JFXHamburger titleBurger;
    @FXML
    private StackPane optionsBurger;
    @FXML
    private JFXButton profileButton;
    @FXML
    private JFXRippler optionsRippler;
    @FXML
    private JFXDrawer drawer;
    @FXML
    public static Label lblTitle;
    @FXML
    private JFXBadge badgeNotification;
    @FXML
    private JFXSnackbar snackbar;
    private int count = 1;
    private JFXPopup toolbarPopup;
    private JFXPopup profilePopup;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws Exception {
        // init the title hamburger icon
        drawer.setOnDrawerOpening(e -> {
            final Transition animation = titleBurger.getAnimation();
            animation.setRate(1);
            animation.play();
        });
        drawer.setOnDrawerClosing(e -> {
            final Transition animation = titleBurger.getAnimation();
            animation.setRate(-1);
            animation.play();
        });
        titleBurgerContainer.setOnMouseClicked(e -> {
            if (drawer.isHidden() || drawer.isHiding()) {
                drawer.open();
            } else {
                drawer.close();
            }
        });

        //set locale
        loadLanguage(Locale.ENGLISH);

        optionsBurger.setOnMouseClicked(e -> toolbarPopup.show(optionsBurger,
                PopupVPosition.TOP,
                PopupHPosition.RIGHT,
                -12,
                15));
        profileButton.setOnMouseClicked(e -> profilePopup.show(profileButton,
                PopupVPosition.TOP,
                PopupHPosition.RIGHT,
                -12,
                15));

        //set language smatcsv
        ViewConfiguration viewConfig = new ViewConfiguration();
        viewConfig.setResources(ResourceBundle.getBundle("lang.message", Locale.ENGLISH));
        //viewConfig.setResources(ResourceBundle.getBundle("smartcsv", Locale.ENGLISH));

        // create the inner flow and content
        context = new ViewFlowContext();
        // set the default controller
        Flow innerFlow = new Flow(JFoenixController.class, viewConfig);

        //final FlowHandler flowHandler = innerFlow.createHandler(context);
        FlowHandler flowHandler = new FlowHandler(innerFlow, context, viewConfig);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        final Duration containerAnimationDuration = Duration.millis(320);
        drawer.setContent(flowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration, SWIPE_LEFT)));
        context.register("ContentPane", drawer.getContent().get(0));

        // side controller will add links to the content flow
        Flow sideMenuFlow = new Flow(SideMenuController.class);
        final FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(context);
        drawer.setSidePane(sideMenuFlowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration,
                SWIPE_LEFT)));
        // Notifications
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

    public void loadLanguage(Locale locale) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("lang.message", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui/popup/MainPopup.fxml"), bundle);
        loader.setController(new InputController());
        toolbarPopup = new JFXPopup(loader.load());
        loader = new FXMLLoader(getClass().getResource("/fxml/ui/popup/ProfilePopup.fxml"), bundle);
        loader.setController(new InputController());
        profilePopup = new JFXPopup(loader.load());
    }

//    /**
//     * sets the given Locale in the I18N class and keeps count of the number of
//     * switches.
//     *
//     * @param locale the new local to set
//     */
//    private void switchLanguage(Locale locale) {
//        I18N.setLocale(locale);
//    }
//
//    @Override
//    protected ResourceBundle getResourceBundle(Locale locale) {
//        return ResourceBundle.getBundle("lang.message", locale, new UTF8Control());
//    }
//
//    @Override
//    protected URL getFXMLResource() {
//        return getClass().getResource("/fxml/Main.fxml");
//    }
//
//    @Override
//    protected void onSaveState(StateBundle stateBundle) {
//
//    }
//
//    @Override
//    protected void onLoadState(I18NController newController, I18NLanguage newLanguage, ResourceBundle resourceBundle, StateBundle stateBundle) {
//        try {
//            lblTitle.setText(resourceBundle.getString("window.title"));
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ui/popup/MainPopup.fxml"), resourceBundle);
//            loader.setController(new InputController());
//            toolbarPopup = new JFXPopup(loader.load());
//            loader = new FXMLLoader(getClass().getResource("/fxml/ui/popup/ProfilePopup.fxml"), resourceBundle);
//            loader.setController(new InputController());
//            profilePopup = new JFXPopup(loader.load());
//            loader = new FXMLLoader(getClass().getResource("/fxml/Register.fxml"), resourceBundle);
//            loader.setController(new RegisterController());
//            drawer = new JFXDrawer();
//        } catch (IOException ex) {
//            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
//        }
////        NodeOrientation nodeOrientation = newLanguage.getNodeOrientation();
//    }
    public final class InputController {

        @FXML
        private JFXListView<?> toolbarPopupList;
        @FXML
        private JFXListView<?> profilePopupList;

        // close application
        @FXML
        private void submit() throws IOException {
            Scene scene = root.getScene();
            final ObservableList<String> stylesheets = scene.getStylesheets();
            if (toolbarPopupList != null) {
                if (toolbarPopupList.getSelectionModel().getSelectedIndex() == 0) {
                    stylesheets.removeAll(MainDemo.class.getResource("/css/theme-red.css").toExternalForm());
                    stylesheets.addAll(MainDemo.class.getResource("/css/theme-blue.css").toExternalForm());
                } else if (toolbarPopupList.getSelectionModel().getSelectedIndex() == 1) {
                    stylesheets.removeAll(MainDemo.class.getResource("/css/theme-blue.css").toExternalForm());
                    stylesheets.addAll(MainDemo.class.getResource("/css/theme-red.css").toExternalForm());
                }
            }

            if (profilePopupList != null) {
                if (profilePopupList.getSelectionModel()
                        .getSelectedIndex() == 0) {
//                    switchLanguage(Locale.ENGLISH);
//                    changeLanguage(AppSettings.Language.ENGLISH);
                } else if (profilePopupList.getSelectionModel()
                        .getSelectedIndex() == 1) {
//                    changeLanguage(AppSettings.Language.SPANISH);
                } else if (profilePopupList.getSelectionModel().getSelectedIndex() == 2) {
//                    JFXDialogLayout dialogLayout = new JFXDialogLayout();
//                    JFXDialog dialog = new JFXDialog(dialogLayout, (StackPane) context.getRegisteredObject("ContentPane"), JFXDialog.DialogTransition.TOP);
//                    dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
//                    dialog.show((StackPane) context.getRegisteredObject("ContentPane"));
                } else if (profilePopupList.getSelectionModel().getSelectedIndex() == 3) {
                    Platform.exit();
                }
                profilePopupList.getParent().setVisible(false);
            }
        }
    }
}
