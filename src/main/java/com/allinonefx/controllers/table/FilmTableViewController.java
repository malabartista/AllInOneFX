package com.allinonefx.controllers.table;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.form.RegisterController;
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
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.util.VetoException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
    private final JFXTreeTableColumn<Film, ImageView> filmPhotoEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Film, String> titleEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Film, String> descriptionEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Film, Date> releaseYearEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Film, String> rentalDurationEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Film, BigDecimal> rentalRateEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Film, Integer> lengthEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Film, String> specialFeaturesEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Film, Language> languageEditableColumn = new JFXTreeTableColumn();

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        //title
        MainController.lblTitle.setText("Films");
        // setup table
        setupEditableTableView();
        //locale
        setLocale();
        // flow: add register
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlow.withGlobalLink(treeTableViewAdd.getId(), RegisterController.class);
        contentFlow.withGlobalLink(treeTableViewEdit.getId(), RegisterController.class);
        treeTableViewAdd.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("treeTableViewAdd");
            } catch (VetoException ex) {
                Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        treeTableViewEdit.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Film film = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                try {
                    context.register("editFilm", film);
                    contentFlowHandler.handle("treeTableViewEdit");
                } catch (VetoException ex) {
                    Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FlowException ex) {
                    Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                MainController.snackbar.show(I18N.get("film.no.selected"), 3000);
            }
        });
        treeTableViewRemove.setOnMouseClicked((e) -> {
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

    public void setLocale() {
        super.setLocale();
        lblTitle.textProperty().bind(I18N.createStringBinding("label.films"));
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
        //set columns styles
        titleEditableColumn.getStyleClass().add("cell-left");
        descriptionEditableColumn.getStyleClass().add("cell-left");
        releaseYearEditableColumn.getStyleClass().add("cell-right");
        rentalDurationEditableColumn.getStyleClass().add("cell-right");
        rentalRateEditableColumn.getStyleClass().add("cell-right");
        lengthEditableColumn.getStyleClass().add("cell-right");
        specialFeaturesEditableColumn.getStyleClass().add("cell-left");
        languageEditableColumn.getStyleClass().add("cell-left");
        //set table columns
        editableTreeTableView.getColumns().clear();
        editableTreeTableView.getColumns().addAll(titleEditableColumn, descriptionEditableColumn, specialFeaturesEditableColumn, releaseYearEditableColumn, rentalDurationEditableColumn, rentalRateEditableColumn, lengthEditableColumn, languageEditableColumn);
        //set cell values
        titleEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, String>("title"));
        descriptionEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, String>("description"));
        releaseYearEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, Date>("release_year"));
        rentalDurationEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, String>("rental_duration"));
        rentalRateEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, BigDecimal>("rental_rate"));
        lengthEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, Integer>("length"));
        specialFeaturesEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, String>("special_features"));
//        languageEditableColumn.setCellValueFactory(new Callback<CellDataFeatures<Film, Language>, ObservableValue<Language>>() {
//            @Override
//            public ObservableValue<Language> call(CellDataFeatures<Film, Language> param) {
//                return new SimpleObjectProperty<Language>(param.getValue().getValue().getLanguage());
//            }
//        });
        languageEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, Language>("language"));
        // language combobox
        Callback<TreeTableColumn<Film, Language>, TreeTableCell<Film, Language>> comboBoxCellFactory
                = (TreeTableColumn<Film, Language> param) -> new ComboBoxEditingCell();
        languageEditableColumn.setCellFactory(comboBoxCellFactory);
//        languageEditableColumn.setCellFactory((TreeTableColumn<Film, Language> col) -> {
//            TreeTableCell<Film, Language> c = new TreeTableCell<>();
//            final JFXComboBox<Language> comboBox = new JFXComboBox<>(languages);
//            comboBox.setCellFactory(new Callback<ListView<Language>, ListCell<Language>>() {
//                @Override
//                public ListCell<Language> call(ListView<Language> p) {
//                    final ListCell<Language> cell = new ListCell<Language>() {
//                        @Override
//                        protected void updateItem(Language t, boolean bln) {
//                            super.updateItem(t, bln);
//                            if (t != null) {
//                                setText(t.getName());
//                            } else {
//                                setText(null);
//                            }
//                        }
//                    };
//                    return cell;
//                }
//            });
//            StringConverter<Language> converter = new StringConverter<Language>() {
//                @Override
//                public String toString(Language language) {
//                    return language.getName();
//                }
//
//                @Override
//                public Language fromString(String id) {
//                    return languages.stream()
//                            .filter(item -> item.getName().equals(id))
//                            .collect(Collectors.toList()).get(0);
//                }
//            };
//            comboBox.setConverter(converter);
//            comboBox.getSelectionModel().selectedItemProperty().addListener((o, ol, nw) -> {
//                System.out.println(comboBox.getValue().getName());
//            });
////            comboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
////                if (!c.isEmpty() && newValue != null) {
////                    String lang = editableTreeTableView.getTreeItem(c.getIndex()).getValue().getLanguage().getName();
//////                    String property = option.getValue().apply(lang);
//////                    property = newValue;
////                }
////            });
//            c.itemProperty().addListener((obs, oldItem, newItem) -> {
//                if (!c.isEmpty() && newItem != null) {
//                    comboBox.setValue(newItem);
//                }
//            });
//            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
//            return c;
//        });
        // add editors
        titleEditableColumn.setCellFactory(
                (TreeTableColumn<Film, String> param) -> {
                    return new GenericEditableTreeTableCell<>(
                            new TextFieldEditorBuilder());
                }
        );
        titleEditableColumn.setOnEditCommit(
                new EventHandler<CellEditEvent<Film, String>>() {
            @Override
            public void handle(CellEditEvent<Film, String> t
            ) {
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
            }
        }
        );
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

        editableTreeTableView.setRoot(
                new RecursiveTreeItem<>(dataFilm, RecursiveTreeObject::getChildren));
        editableTreeTableView.setShowRoot(
                false);
        editableTreeTableView.setEditable(
                true);
        treeTableViewCount.textProperty()
                .bind(Bindings.createStringBinding(() -> PREFIX + editableTreeTableView.getCurrentItemsCount() + POSTFIX, editableTreeTableView.currentItemsCountProperty()));
        editableTreeTableView.prefHeightProperty()
                .bind(root.widthProperty());
        searchField.textProperty()
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
