/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.controllers.form;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.table.FilmTableViewController;
import com.allinonefx.dao.FilmMapper;
import com.allinonefx.dao.LanguageMapper;
import static com.allinonefx.gui.uicomponents.DialogController.CONTENT_PANE;
import com.allinonefx.gui.uicomponents.TreeTableViewController;
import com.allinonefx.model.Film;
import com.allinonefx.model.Language;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.util.VetoException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/form/Film.fxml", title = "Film")
public class FilmFormController extends FormController {

    FilmMapper mapper = sqlSession.getMapper(FilmMapper.class);
    LanguageMapper languageMapper = sqlSession.getMapper(LanguageMapper.class);
    ObservableList<Language> lstLanguages = FXCollections.observableArrayList(languageMapper.selectByExample(null));
    private Film filmEdit;

    @FXML
    private JFXTextField txtTitle;
    @FXML
    private JFXTextField txtDescription;
    @FXML
    private JFXTextField txtSpecialFeatures;
    @FXML
    private JFXTextField txtRentalDuration;
    @FXML
    private JFXTextField txtRentalRate;
    @FXML
    private JFXTextField txtReplacementCost;
    @FXML
    private JFXComboBox<Language> comboLanguages;
    @FXML
    private JFXDatePicker dateReleaseYear;

    //form progress
    private static double progress1 = 0;
    private static double progress2 = 0;
    private static double progress3 = 0;
    private static double progress4 = 0;
    private static double progress5 = 0;
    private static double progress6 = 0;
    private static double progress7 = 0;
    private static double progress8 = 0;
    private static double progress9 = 0;
    private static double progress10 = 0;

    /**
     * init fxml when loaded.
     *
     * @throws java.io.IOException
     */
    @PostConstruct
    public void init() throws IOException {
        // title
        MainController.lblTitle.setText("Film");
        // form init
        handleValidation();
        updateProgress();
        setLanguagesToCombo();
        setTextFields();
        setLocale();
        // flow
        contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlow.withGlobalLink("filmTableView", FilmTableViewController.class);
        // dialog
        root.getChildren().remove(dialog);
        acceptButton.setOnMouseClicked((e) -> dialog.close());
    }

    private void updateProgress() {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        progressForm.prefWidthProperty().bind(root.widthProperty());
        progressForm.setProgress(0.00);
        progress1 = 0;
        progress2 = 0;
        progress3 = 0;
        progress4 = 0;
        progress5 = 0;
        lblComplete.setText("0% " + I18N.get("complete"));

        txtTitle.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.isEmpty()) {
                progress1 = 0.2;
            } else {
                progress1 = 0.0;
            }
            setProgress();
        });

        txtDescription.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.isEmpty()) {
                progress2 = 0.2;
            } else {
                progress2 = 0.0;
            }
            setProgress();
        });

        txtSpecialFeatures.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.isEmpty()) {
                progress3 = 0.2;
            } else {
                progress3 = 0.0;
            }
            setProgress();
        });

        txtRentalDuration.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.isEmpty()) {
                progress4 = 0.2;
            } else {
                progress4 = 0.0;
            }
            setProgress();
        });

        txtRentalRate.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.isEmpty()) {
                progress5 = 0.2;
            } else {
                progress5 = 0.0;
            }
            setProgress();
        });

        txtReplacementCost.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.isEmpty()) {
                progress6 = 0.2;
            } else {
                progress6 = 0.0;
            }
            setProgress();
        });

        dateReleaseYear.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (newValue != null) {
                progress7 = 0.2;
            } else {
                progress7 = 0.0;
            }
            setProgress();
        });

        comboLanguages.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Language> observable, Language oldValue, Language newValue) -> {
            if (newValue != null) {
                progress8 = 0.2;
            } else {
                progress8 = 0.0;
            }
            setProgress();
        });
    }

    public void setProgress() {
        double sum = (progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8);
        super.setProgress(sum);
    }

    private void setTextFields() {
        filmEdit = (Film) context.getRegisteredObject("editFilm");
        if (filmEdit != null && filmEdit.getFilm_id() != 0) {
            context.register("editFilm", new Film());
            txtTitle.setText(filmEdit.getTitle());
            txtDescription.setText(filmEdit.getDescription());
            txtSpecialFeatures.setText(filmEdit.getSpecial_features());
            txtRentalDuration.setText(Byte.toString(filmEdit.getRental_duration()));
            txtRentalRate.setText(filmEdit.getRental_rate().toString());
            txtReplacementCost.setText(filmEdit.getReplacement_cost().toString());
            comboLanguages.setValue(filmEdit.getLanguage());
        }
    }

    private void setLanguagesToCombo() {
        comboLanguages.setItems(lstLanguages);
        StringConverter<Language> converter;
        converter = new StringConverter<Language>() {
            @Override
            public String toString(Language language) {
                return language.getName();
            }

            @Override
            public Language fromString(String id) {
                return lstLanguages.stream()
                        .filter((Language item) -> item.getLanguage_id().equals(id))
                        .collect(Collectors.toList()).get(0);
            }
        };
        comboLanguages.setConverter(converter);
        comboLanguages.getSelectionModel().selectedItemProperty().addListener((o, ol, nw) -> {
            System.out.println(comboLanguages.getValue());
        });
    }

    private void validatetext() {
    }

    @FXML
    private void edit(ActionEvent event) {
    }

    @FXML
    private void save(ActionEvent event) throws SQLException, FlowException, ParseException {
        // check if all fields are filled up
        if (progressForm.getProgress() < 0.9) {
            dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
            dialog.show((StackPane) context.getRegisteredObject(CONTENT_PANE));
            return;
        }

        if (filmEdit != null && filmEdit.getFilm_id() != 0) {
            setFilmFields(filmEdit);
            int success = mapper.updateByPrimaryKey(filmEdit);
            if (success == 1) {
                sqlSession.commit();
                JFXSnackbar fXSnackbar = MainController.snackbar;
                fXSnackbar.show(I18N.get("film.updated"), 3000);
            }
        } else {
            Film film = new Film();
            setFilmFields(film);
            int success = mapper.insert(film);
            if (success == 1) {
                sqlSession.commit();
                JFXSnackbar fXSnackbar = MainController.snackbar;
                fXSnackbar.show(I18N.get("film.new.saved"), 3000);
            }
        }

        try {
            contentFlowHandler.handle("filmTableView");
        } catch (VetoException | FlowException ex) {
            Logger.getLogger(TreeTableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleValidation() {
        RequiredFieldValidator fieldValidator = new RequiredFieldValidator();
        fieldValidator.setMessage(I18N.get("input.required"));
        fieldValidator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        txtTitle.getValidators().add(fieldValidator);
        txtTitle.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldVal, Boolean newVal) -> {
            if (!newVal) {
                txtTitle.validate();
            }
        });
        RequiredFieldValidator fieldValidator2 = new RequiredFieldValidator();
        fieldValidator2.setMessage(I18N.get("input.required"));
        fieldValidator2.setIcon(new FontAwesomeIconView(FontAwesomeIcon.TIMES));
        txtDescription.getValidators().add(fieldValidator2);
        txtDescription.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtDescription.validate();
            }
        });
    }

    private Film setFilmFields(Film film) throws ParseException {
        film.setTitle(txtTitle.getText().toUpperCase());
        film.setDescription(txtDescription.getText());
        film.setSpecial_features(txtSpecialFeatures.getText());
        film.setRental_duration(Byte.valueOf(txtRentalDuration.getText()));
        final NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        final Number numberRentalRate = format.parse(txtRentalRate.getText());
        final Number numberReplacementCost = format.parse(txtReplacementCost.getText());
        final BigDecimal rentalRate = new BigDecimal(numberRentalRate.toString());
        final BigDecimal replacementCost = new BigDecimal(numberReplacementCost.toString());
        film.setRental_rate(rentalRate);
        film.setReplacement_cost(replacementCost);
        film.setLanguage_id(comboLanguages.getSelectionModel().getSelectedItem().getLanguage_id());
        film.setLast_update(new Date());
        return film;
    }

    @FXML
    @Override
    public void clearFields() {
        super.clearFields();
        txtTitle.setText("");
        txtDescription.setText("");
        txtSpecialFeatures.setText("");
        txtRentalDuration.setText("");
        txtRentalRate.setText("");
        dateReleaseYear.setValue(null);
        comboLanguages.setValue(null);
    }

    @Override
    public void setLocale() {
        super.setLocale();
        txtTitle.promptTextProperty().bind(I18N.createStringBinding("label.title"));
        txtDescription.promptTextProperty().bind(I18N.createStringBinding("label.description"));
        txtSpecialFeatures.promptTextProperty().bind(I18N.createStringBinding("label.special.features"));
        txtRentalDuration.promptTextProperty().bind(I18N.createStringBinding("label.rental.duration"));
        txtRentalRate.promptTextProperty().bind(I18N.createStringBinding("label.rental.rate"));
        txtReplacementCost.promptTextProperty().bind(I18N.createStringBinding("label.replacement.cost"));
        comboLanguages.promptTextProperty().bind(I18N.createStringBinding("label.language"));
        dateReleaseYear.promptTextProperty().bind(I18N.createStringBinding("label.release.year"));
    }
}
