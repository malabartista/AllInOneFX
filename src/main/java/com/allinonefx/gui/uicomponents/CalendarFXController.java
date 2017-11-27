/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.gui.uicomponents;

import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.PrimaryStageAware;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import com.jfoenix.controls.JFXProgressBar;
import io.datafx.controller.ViewController;
import java.io.IOException;
import java.time.LocalTime;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/CalendarFX.fxml", title = "CalendarFX Example")
public class CalendarFXController implements PrimaryStageAware {

    private StringProperty title = new SimpleStringProperty();

    @FXML
    private StackPane root;

    private Stage stage;

    @Override
    public void setPrimaryStage(Stage primaryStage) {
        this.stage = primaryStage;
    }

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() throws IOException {
        MainController.lblTitle.setText("CalendarFX");
        JFXProgressBar progressBar = new JFXProgressBar();
        CalendarView calendarView = new CalendarView();
        Task calendarTask = new Task<CalendarView>() {
            @Override
            protected CalendarView call() throws Exception {

                Calendar katja = new Calendar("Katja");
                Calendar dirk = new Calendar("Dirk");
                Calendar philip = new Calendar("Philip");
                Calendar jule = new Calendar("Jule");
                Calendar armin = new Calendar("Armin");
                Calendar birthdays = new Calendar("Birthdays");
                Calendar holidays = new Calendar("Holidays");

                katja.setShortName("K");
                dirk.setShortName("D");
                philip.setShortName("P");
                jule.setShortName("J");
                armin.setShortName("A");
                birthdays.setShortName("B");
                holidays.setShortName("H");

                katja.setStyle(Calendar.Style.STYLE1);
                dirk.setStyle(Calendar.Style.STYLE2);
                philip.setStyle(Calendar.Style.STYLE3);
                jule.setStyle(Calendar.Style.STYLE4);
                armin.setStyle(Calendar.Style.STYLE5);
                birthdays.setStyle(Calendar.Style.STYLE6);
                holidays.setStyle(Calendar.Style.STYLE7);

                CalendarSource familyCalendarSource = new CalendarSource("Family");
                familyCalendarSource.getCalendars().addAll(birthdays, holidays, katja, dirk, philip, jule, armin);

                calendarView.getCalendarSources().setAll(familyCalendarSource);
                calendarView.setRequestedTime(LocalTime.now());

                final int count = 1000 - 1;
                for (int i = 1; i <= count; i++) {
                    Thread.sleep(10);
                    updateProgress(i, count);
                }
                return calendarView;
            }
        };
        calendarTask.setOnSucceeded(evt -> {
            // update ui with results
            progressBar.setVisible(false);
            root.getChildren().addAll(calendarView);
        });

        // add progressBar indicator to show progressBar of calendarTask
        progressBar.progressProperty().bind(calendarTask.progressProperty());
        StackPane stackProgress = new StackPane(progressBar);
        stackProgress.setAlignment(Pos.CENTER);
        root.getChildren().addAll(stackProgress);
        new Thread(calendarTask).start();

        /*
        CalendarView calendarView = new CalendarView();
        Calendar katja = new Calendar("Katja");
        Calendar dirk = new Calendar("Dirk");
        Calendar philip = new Calendar("Philip");
        Calendar jule = new Calendar("Jule");
        Calendar armin = new Calendar("Armin");
        Calendar birthdays = new Calendar("Birthdays");
        Calendar holidays = new Calendar("Holidays");

        katja.setShortName("K");
        dirk.setShortName("D");
        philip.setShortName("P");
        jule.setShortName("J");
        armin.setShortName("A");
        birthdays.setShortName("B");
        holidays.setShortName("H");

        katja.setStyle(Calendar.Style.STYLE1);
        dirk.setStyle(Calendar.Style.STYLE2);
        philip.setStyle(Calendar.Style.STYLE3);
        jule.setStyle(Calendar.Style.STYLE4);
        armin.setStyle(Calendar.Style.STYLE5);
        birthdays.setStyle(Calendar.Style.STYLE6);
        holidays.setStyle(Calendar.Style.STYLE7);

        CalendarSource familyCalendarSource = new CalendarSource("Family");
        familyCalendarSource.getCalendars().addAll(birthdays, holidays, katja, dirk, philip, jule, armin);

        calendarView.getCalendarSources().setAll(familyCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());
        root.getChildren().addAll(calendarView);

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
         */
    }

}
