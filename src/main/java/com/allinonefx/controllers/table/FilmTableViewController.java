package com.allinonefx.controllers.table;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.form.FilmFormController;
import com.allinonefx.dao.FilmMapper;
import com.allinonefx.dao.LanguageMapper;
import com.allinonefx.model.Film;
import com.allinonefx.model.Language;
import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.DoubleTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.IntegerTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/table/TreeTableView.fxml", title = "Films")
public class FilmTableViewController extends TreeTableViewController {

    FilmMapper mapper = sqlSession.getMapper(FilmMapper.class);
    LanguageMapper languageMapper = sqlSession.getMapper(LanguageMapper.class);
    ObservableList<Language> languages = FXCollections.observableArrayList(languageMapper.selectByExample(null));

    // editable table view
    @FXML
    private JFXTreeTableView<Film> editableTreeTableView;
    private final JFXTreeTableColumn<Film, ImageView> filmPhotoEditableColumn;
    private final JFXTreeTableColumn<Film, String> titleEditableColumn;
    private final JFXTreeTableColumn<Film, String> descriptionEditableColumn;
    private final JFXTreeTableColumn<Film, Date> releaseYearEditableColumn;
    private final JFXTreeTableColumn<Film, String> rentalDurationEditableColumn;
    private final JFXTreeTableColumn<Film, BigDecimal> rentalRateEditableColumn;
    private final JFXTreeTableColumn<Film, Integer> lengthEditableColumn;
    private final JFXTreeTableColumn<Film, String> specialFeaturesEditableColumn;
    private final JFXTreeTableColumn<Film, Language> languageEditableColumn;

    public FilmTableViewController() {
        this.languageEditableColumn = new JFXTreeTableColumn();
        this.specialFeaturesEditableColumn = new JFXTreeTableColumn();
        this.lengthEditableColumn = new JFXTreeTableColumn();
        this.rentalRateEditableColumn = new JFXTreeTableColumn();
        this.rentalDurationEditableColumn = new JFXTreeTableColumn();
        this.releaseYearEditableColumn = new JFXTreeTableColumn();
        this.descriptionEditableColumn = new JFXTreeTableColumn();
        this.titleEditableColumn = new JFXTreeTableColumn();
        this.filmPhotoEditableColumn = new JFXTreeTableColumn();
    }

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    @Override
    public void init() {
        super.init();
        MainController.lblTitle.setText("Films");
        setupEditableTableView();
        setLocale();
        // flow: add register
        contentFlow.withGlobalLink(btnTableAdd.getId(), FilmFormController.class);
        contentFlow.withGlobalLink(btnTableEdit.getId(), FilmFormController.class);
        btnTableAdd.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("btnTableAdd");
            } catch (VetoException | FlowException ex) {
                Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnTableEdit.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Film film = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                try {
                    context.register("editFilm", film);
                    contentFlowHandler.handle("btnTableEdit");
                } catch (VetoException | FlowException ex) {
                    Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                MainController.snackbar.show(I18N.get("film.no.selected"), 3000);
            }
        });
        btnTableRemove.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Film film = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                System.out.println(film.getTitle());
                if (mapper.deleteByPrimaryKey(film.getFilm_id()) == 1) {
                    sqlSession.commit();
                    setupEditableTableView();
                    MainController.snackbar.show(I18N.get("film.deleted"), 3000);
                }
            } else {
                MainController.snackbar.show(I18N.get("film.no.selected"), 3000);
            }
        });
    }

    @Override
    public void setLocale() {
        super.setLocale();
        lblTableTitle.textProperty().bind(I18N.createStringBinding("label.films"));
        filmPhotoEditableColumn.textProperty().bind(I18N.createStringBinding("column.photo"));
        titleEditableColumn.textProperty().bind(I18N.createStringBinding("column.title"));
        descriptionEditableColumn.textProperty().bind(I18N.createStringBinding("column.description"));
        releaseYearEditableColumn.textProperty().bind(I18N.createStringBinding("column.release_year"));
        rentalDurationEditableColumn.textProperty().bind(I18N.createStringBinding("column.rental_duration"));
        rentalRateEditableColumn.textProperty().bind(I18N.createStringBinding("column.rental_rate"));
        lengthEditableColumn.textProperty().bind(I18N.createStringBinding("column.length"));
        specialFeaturesEditableColumn.textProperty().bind(I18N.createStringBinding("column.special_features"));
        languageEditableColumn.textProperty().bind(I18N.createStringBinding("column.language"));
    }

    private void setupEditableTableView() {
        // set columns styles
        titleEditableColumn.getStyleClass().add("cell-left");
        descriptionEditableColumn.getStyleClass().add("cell-left");
        releaseYearEditableColumn.getStyleClass().add("cell-right");
        rentalDurationEditableColumn.getStyleClass().add("cell-right");
        rentalRateEditableColumn.getStyleClass().add("cell-right");
        lengthEditableColumn.getStyleClass().add("cell-right");
        specialFeaturesEditableColumn.getStyleClass().add("cell-left");
        languageEditableColumn.getStyleClass().add("cell-left");
        // set table columns
        editableTreeTableView.getColumns().clear();
        editableTreeTableView.getColumns().addAll(titleEditableColumn, descriptionEditableColumn, specialFeaturesEditableColumn, releaseYearEditableColumn, rentalDurationEditableColumn, rentalRateEditableColumn, lengthEditableColumn, languageEditableColumn);
        // set cell values
        titleEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("title"));
        descriptionEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        releaseYearEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("release_year"));
        rentalDurationEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("rental_duration"));
        rentalRateEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("rental_rate"));
        lengthEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("length"));
        specialFeaturesEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("special_features"));
        languageEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("language"));
        // add editors
        Callback<TreeTableColumn<Film, Language>, TreeTableCell<Film, Language>> comboBoxCellFactory
                = (TreeTableColumn<Film, Language> param) -> new ComboBoxEditingCell();
        Callback<TreeTableColumn<Film, Date>, TreeTableCell<Film, Date>> dateCellFactory
                = (TreeTableColumn<Film, Date> param) -> new DateEditingCell();
        languageEditableColumn.setCellFactory(comboBoxCellFactory);
        releaseYearEditableColumn.setCellFactory(dateCellFactory);
        titleEditableColumn.setCellFactory(
                (TreeTableColumn<Film, String> param) -> {
                    return new GenericEditableTreeTableCell<>(
                            new TextFieldEditorBuilder());
                }
        );
        titleEditableColumn.setOnEditCommit((CellEditEvent<Film, String> t) -> {
            if (!t.getNewValue().equals(t.getOldValue())) {
                try {
                    t.getTreeTableView()
                            .getTreeItem(t.getTreeTablePosition()
                                    .getRow())
                            .getValue().setTitle(t.getNewValue());
                    Film film = (Film) editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                    film.setTitle(t.getNewValue().toUpperCase());
                    if (mapper.updateByPrimaryKey(film) == 1) {
                        sqlSession.commit();
                        MainController.snackbar.show(I18N.get("film.updated"), 3000);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        rentalRateEditableColumn.setCellFactory(
                (TreeTableColumn<Film, BigDecimal> param) -> {
                    return new GenericEditableTreeTableCell<>(
                            new DoubleTextFieldEditorBuilder());
                }
        );
        rentalRateEditableColumn.setOnEditCommit(
                (CellEditEvent<Film, BigDecimal> t) -> {
                    if (!t.getNewValue().equals(t.getOldValue())) {
                        try {
                            t.getTreeTableView()
                                    .getTreeItem(t.getTreeTablePosition()
                                            .getRow())
                                    .getValue().setRental_rate(t.getNewValue());
                            Film film = (Film) editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                            film.setRental_rate(t.getNewValue());
                            if (mapper.updateByPrimaryKey(film) == 1) {
                                sqlSession.commit();
                                MainController.snackbar.show(I18N.get("film.updated"), 3000);
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
        );
        lengthEditableColumn.setCellFactory(
                (TreeTableColumn<Film, Integer> param) -> {
                    return new GenericEditableTreeTableCell<>(
                            new IntegerTextFieldEditorBuilder());
                }
        );
        lengthEditableColumn.setOnEditCommit(
                (CellEditEvent<Film, Integer> t) -> {
                    try {
                        t.getTreeTableView()
                                .getTreeItem(t.getTreeTablePosition()
                                        .getRow())
                                .getValue().setLength(t.getNewValue().shortValue());
                        Film film = (Film) editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                        film.setLength(t.getNewValue().shortValue());
                        if (mapper.updateByPrimaryKey(film) == 1) {
                            sqlSession.commit();
                            MainController.snackbar.show(I18N.get("film.updated"), 3000);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        );
        // set data table
        ObservableList<Film> dataFilm = FXCollections.observableArrayList(mapper.selectByExampleWithLanguage(null));
        editableTreeTableView.setRoot(new RecursiveTreeItem<>(dataFilm, RecursiveTreeObject::getChildren));
        editableTreeTableView.setShowRoot(false);
        editableTreeTableView.setEditable(true);
        editableTreeTableView.prefHeightProperty().bind(root.widthProperty());
        lblTableCount.textProperty()
                .bind(Bindings.createStringBinding(() -> PREFIX + editableTreeTableView.getCurrentItemsCount() + POSTFIX, editableTreeTableView.currentItemsCountProperty()));
        txtTableSearch.textProperty()
                .addListener(setupSearchField(editableTreeTableView));

    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Film> tableView) {
        return (o, oldVal, newVal)
                -> tableView.setPredicate(filmProp -> {
                    final Film film = filmProp.getValue();
                    return film.getTitle().contains(newVal)
                            || film.getDescription().contains(newVal)
                            || film.getSpecial_features().contains(newVal);
                });
    }

    class ComboBoxEditingCell extends TreeTableCell<Film, Language> {

        private JFXComboBox<Language> comboBox;

        private ComboBoxEditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createComboBox();
                setText(null);
                setGraphic(comboBox);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getLanguage().getName());
            setGraphic(null);
        }

        @Override
        public void updateItem(Language item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (comboBox != null) {
                        comboBox.setValue(getLanguage());
                    }
                    setText(getLanguage().getName());
                    setGraphic(comboBox);
                } else {
                    setText(getLanguage().getName());
                    setGraphic(null);
                }
            }
        }

        private void createComboBox() {
            comboBox = new JFXComboBox<>(languages);
            comboBoxConverter(comboBox);
            comboBox.valueProperty().set(getLanguage());
            comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            comboBox.setOnAction((e) -> {
                System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem().getName());
                commitEdit(comboBox.getSelectionModel().getSelectedItem());
                try {
                    Film film = (Film) editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                    Language lang = comboBox.getSelectionModel().getSelectedItem();
                    film.setLanguage_id(lang.getLanguage_id());
                    if (mapper.updateByPrimaryKey(film) == 1) {
                        sqlSession.commit();
                        MainController.snackbar.show(I18N.get("film.updated"), 3000);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
//            comboBox.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(comboBox.getSelectionModel().getSelectedItem());
//                }
//            });
        }

        private void comboBoxConverter(JFXComboBox<Language> comboBox) {
            // Define rendering of the list of values in ComboBox drop down.
            comboBox.setCellFactory((c) -> {
                return new ListCell<Language>() {
                    @Override
                    protected void updateItem(Language item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            });
        }

        private Language getLanguage() {
            return getItem() == null ? new Language() : getItem();
        }
    }

}
