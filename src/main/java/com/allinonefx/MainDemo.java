package com.allinonefx;

import com.allinonefx.controllers.LoginController;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyphLoader;
import io.datafx.controller.ViewConfiguration;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.allinonefx")
@PropertySource(value = "classpath:/application.properties")
public class MainDemo extends Application {

    @FXMLViewFlowContext
    private ViewFlowContext flowContext;

    private AnnotationConfigApplicationContext appContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        appContext = new AnnotationConfigApplicationContext(MainDemo.class);

        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(MainDemo.class.getResourceAsStream("/fonts/icomoon.svg"),
                        "icomoon.svg");
            } catch (IOException ioExc) {
                ioExc.printStackTrace();
            }
        }).start();

        //set language
        ViewConfiguration viewConfig = new ViewConfiguration();
        Locale locale = new Locale("es", "ES");
        viewConfig.setResources(ResourceBundle.getBundle("application", locale));

        //set controller
        Flow flow = new Flow(LoginController.class, viewConfig);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        flowContext.register("Stage", stage);
        //flow.createHandler(flowContext).start(container);
        FlowHandler flowHandler = new FlowHandler(flow, new ViewFlowContext(), viewConfig);
        flowHandler.start(container);

        JFXDecorator decorator = new JFXDecorator(stage, container.getView());
        decorator.setCustomMaximize(true);

        double width = 800;
        double height = 600;
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
//            width = bounds.getWidth() / 2.5;
//            height = bounds.getHeight() / 1.35;
            width = bounds.getWidth() / 1.5;
            height = bounds.getHeight() / 1.15;
        } catch (Exception e) {
        }

        Scene scene = new Scene(decorator, width, height);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(MainDemo.class.getResource("/css/jfoenix-fonts.css").toExternalForm(),
                MainDemo.class.getResource("/css/jfoenix-design.css").toExternalForm(),
                MainDemo.class.getResource("/css/jfoenix-main-demo.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

}
