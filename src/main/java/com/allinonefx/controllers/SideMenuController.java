package com.allinonefx.controllers;

import com.allinonefx.config.I18N;
import com.allinonefx.gui.uicomponents.AnchorFXController;
import com.allinonefx.gui.uicomponents.BootstrapFXController;
import com.allinonefx.gui.uicomponents.CalendarFXController;
import com.allinonefx.gui.uicomponents.ControlsFXController;
import com.allinonefx.gui.uicomponents.DockFXController;
import com.allinonefx.gui.uicomponents.GMapsFXController;
import com.allinonefx.gui.uicomponents.GlyphsBrowserController;
import com.allinonefx.gui.uicomponents.JFoenixController;
import com.allinonefx.gui.uicomponents.LiveDirsFXController;
import com.allinonefx.gui.uicomponents.MediaViewController;
import com.allinonefx.gui.uicomponents.TilesFXController;
import com.allinonefx.gui.uicomponents.WebViewController;
import com.allinonefx.gui.uicomponents.WordpressRestAPI;
import com.jfoenix.controls.JFXListView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/SideMenu.fxml", title = "Material Design Example")
public class SideMenuController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

//    @FXML
//    private JFXListView jfoenixList;
    /*
    @FXML
    @ActionTrigger("buttons")
    private Label button;
    @FXML
    @ActionTrigger("checkbox")
    private Label checkbox;
    @FXML
    @ActionTrigger("combobox")
    private Label combobox;
    @FXML
    @ActionTrigger("dialogs")
    private Label dialogs;
    @FXML
    @ActionTrigger("icons")
    private Label icons;
    @FXML
    @ActionTrigger("listview")
    private Label listview;
    @FXML
    @ActionTrigger("progressbar")
    private Label progressbar;
    @FXML
    @ActionTrigger("radiobutton")
    private Label radiobutton;
    @FXML
    @ActionTrigger("slider")
    private Label slider;
    @FXML
    @ActionTrigger("spinner")
    private Label spinner;
    @FXML
    @ActionTrigger("textfield")
    private Label textfield;
    @FXML
    @ActionTrigger("togglebutton")
    private Label togglebutton;
    @FXML
    @ActionTrigger("popup")
    private Label popup;
    @FXML
    @ActionTrigger("svgLoader")
    private Label svgLoader;
    @FXML
    @ActionTrigger("pickers")
    private Label pickers;
    @FXML
    @ActionTrigger("masonry")
    private Label masonry;
     */
    @FXML
    @ActionTrigger("dashboard")
    private Label dashboard;
    @FXML
    @ActionTrigger("jfoenix")
    private Label jfoenix;
    @FXML
    @ActionTrigger("anchorfx")
    private Label anchorfx;
    @FXML
    @ActionTrigger("bootstrapfx")
    private Label bootstrapfx;
    @FXML
    @ActionTrigger("calendarfx")
    private Label calendarfx;
    @FXML
    @ActionTrigger("controlsfx")
    private Label controlsfx;
    @FXML
    @ActionTrigger("dockfx")
    private Label dockfx;
    @FXML
    @ActionTrigger("glyphsbrowser")
    private Label glyphsbrowser;
    @FXML
    @ActionTrigger("gmapsfx")
    private Label gmapsfx;
    @FXML
    @ActionTrigger("livedirsfx")
    private Label livedirsfx;
    @FXML
    @ActionTrigger("tilesfx")
    private Label tilesfx;
    @FXML
    @ActionTrigger("video")
    private Label video;
    @FXML
    @ActionTrigger("webview")
    private Label webview;
    @FXML
    @ActionTrigger("wordpress")
    private Label wordpress;
    @FXML
    @ActionTrigger("scrollpane")
    private Label scrollpane;
    @FXML
    private JFXListView<Label> sideList;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        sideList.propagateMouseEventsToParent();
        sideList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            new Thread(() -> {
                Platform.runLater(() -> {
                    if (newVal != null) {
                        try {
                            contentFlowHandler.handle(newVal.getId());
                        } catch (VetoException exc) {
                            exc.printStackTrace();
                        } catch (FlowException exc) {
                            exc.printStackTrace();
                        }
                    }
                });
            }).start();
        });
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        /*
        bindNodeToController(button, ButtonController.class, contentFlow, contentFlowHandler);
        bindNodeToController(checkbox, CheckboxController.class, contentFlow, contentFlowHandler);
        bindNodeToController(combobox, ComboBoxController.class, contentFlow, contentFlowHandler);
        bindNodeToController(dialogs, DialogController.class, contentFlow, contentFlowHandler);
        bindNodeToController(icons, IconsController.class, contentFlow, contentFlowHandler);
        bindNodeToController(listview, ListViewController.class, contentFlow, contentFlowHandler);
        bindNodeToController(progressbar, ProgressBarController.class, contentFlow, contentFlowHandler);
        bindNodeToController(radiobutton, RadioButtonController.class, contentFlow, contentFlowHandler);
        bindNodeToController(slider, SliderController.class, contentFlow, contentFlowHandler);
        bindNodeToController(spinner, SpinnerController.class, contentFlow, contentFlowHandler);
        bindNodeToController(textfield, TextFieldController.class, contentFlow, contentFlowHandler);
        bindNodeToController(togglebutton, ToggleButtonController.class, contentFlow, contentFlowHandler);
        bindNodeToController(popup, PopupController.class, contentFlow, contentFlowHandler);
        bindNodeToController(svgLoader, SVGLoaderController.class, contentFlow, contentFlowHandler);
        bindNodeToController(pickers, PickersController.class, contentFlow, contentFlowHandler);
        bindNodeToController(masonry, MasonryPaneController.class, contentFlow, contentFlowHandler);
        bindNodeToController(scrollpane, ScrollPaneController.class, contentFlow, contentFlowHandler);
         */
        bindNodeToController(dashboard, DashboardController.class, contentFlow, contentFlowHandler);
        bindNodeToController(jfoenix, JFoenixController.class, contentFlow, contentFlowHandler);
        bindNodeToController(anchorfx, AnchorFXController.class, contentFlow, contentFlowHandler);
        bindNodeToController(bootstrapfx, BootstrapFXController.class, contentFlow, contentFlowHandler);
        bindNodeToController(calendarfx, CalendarFXController.class, contentFlow, contentFlowHandler);
        bindNodeToController(controlsfx, ControlsFXController.class, contentFlow, contentFlowHandler);
        bindNodeToController(dockfx, DockFXController.class, contentFlow, contentFlowHandler);
        bindNodeToController(glyphsbrowser, GlyphsBrowserController.class, contentFlow, contentFlowHandler);
        bindNodeToController(gmapsfx, GMapsFXController.class, contentFlow, contentFlowHandler);
        bindNodeToController(livedirsfx, LiveDirsFXController.class, contentFlow, contentFlowHandler);
        bindNodeToController(tilesfx, TilesFXController.class, contentFlow, contentFlowHandler);
        bindNodeToController(video, MediaViewController.class, contentFlow, contentFlowHandler);
        bindNodeToController(webview, WebViewController.class, contentFlow, contentFlowHandler);
        bindNodeToController(wordpress, WordpressRestAPI.class, contentFlow, contentFlowHandler);
        setLocale();
//        bindNodeToController(smartcsvfx, SmartCSVController.class, contentFlow, contentFlowHandler);
//        Platform.runLater(new Runnable() {
//            public void run() {
//                try {
//                    new DashBoard().start(new Stage());
//                } catch (Exception ex) {
//                    Logger.getLogger(SideMenuController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });

//        jfoenixList.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                System.out.println("clicked on " + jfoenixList.getSelectionModel().getSelectedItem());
//            }
//        });
    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(node.getId(), controllerClass);
    }

    private void setLocale() {
        jfoenix.textProperty().bind(I18N.createStringBinding("label.jfoenix"));
        anchorfx.textProperty().bind(I18N.createStringBinding("label.anchorfx"));
        bootstrapfx.textProperty().bind(I18N.createStringBinding("label.bootstrapfx"));
        calendarfx.textProperty().bind(I18N.createStringBinding("label.calendarfx"));
        controlsfx.textProperty().bind(I18N.createStringBinding("label.controlsfx"));
        glyphsbrowser.textProperty().bind(I18N.createStringBinding("label.glyphsbrowser"));
        gmapsfx.textProperty().bind(I18N.createStringBinding("label.gmapsfx"));
        livedirsfx.textProperty().bind(I18N.createStringBinding("label.livedirsfx"));
        tilesfx.textProperty().bind(I18N.createStringBinding("label.tilesfx"));
        video.textProperty().bind(I18N.createStringBinding("label.video"));
        webview.textProperty().bind(I18N.createStringBinding("label.webview"));
        wordpress.textProperty().bind(I18N.createStringBinding("label.wordpress"));
    }
}
