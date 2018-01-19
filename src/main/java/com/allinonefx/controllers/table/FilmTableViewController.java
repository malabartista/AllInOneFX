package com.allinonefx.controllers.table;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.RegisterController;
import com.allinonefx.dao.FilmMapper;
import com.allinonefx.model.Film;
import com.allinonefx.mybatis.MyBatisConnectionFactory;
import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.IntegerTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javax.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSession;

@ViewController(value = "/fxml/ui/FilmTableView.fxml", title = "Films Table")
public class FilmTableViewController {

    private static final String PREFIX = "( ";
    private static final String POSTFIX = " )";

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private JFXButton addRegister;

    // editable table view
    @FXML
    private StackPane root;
    @FXML
    private JFXTreeTableView<Film> editableTreeTableView;
    @FXML
    private JFXTreeTableColumn<Film, Boolean> checkboxEditableColumn;
    @FXML
    private JFXTreeTableColumn<Film, ImageView> filmPhotoEditableColumn;
    @FXML
    private JFXTreeTableColumn<Film, String> titleEditableColumn;
    @FXML
    private JFXTreeTableColumn<Film, String> releaseYearEditableColumn;
    @FXML
    private JFXTreeTableColumn<Film, String> rentalDurationEditableColumn;
    @FXML
    private JFXTreeTableColumn<Film, String> rentalRateEditableColumn;
    @FXML
    private JFXTreeTableColumn<Film, Integer> lengthEditableColumn;
    @FXML
    private JFXTreeTableColumn<Film, String> specialFeaturesEditableColumn;
    @FXML
    private Label treeTableViewCount;
    @FXML
    private Label editableTreeTableViewCount;
    @FXML
    private JFXButton treeTableViewAdd;
    @FXML
    private JFXButton treeTableViewEdit;
    @FXML
    private JFXButton treeTableViewRemove;
    @FXML
    private Label lblTitle;
    @FXML
    private JFXTextField searchField;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        //title
        MainController.lblTitle.setText("Tree Table View");
        // setup table
        setupEditableTableView();
        //locale
        localeText();
        // flow: add register
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        addRegister.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("treeTableViewAdd");
            } catch (VetoException ex) {
                Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        treeTableViewAdd.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("treeTableViewAdd");
            } catch (VetoException ex) {
                Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        treeTableViewRemove.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Film film = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                System.out.println(film.getTitle());
//                FilmDao filmDao = new FilmDao();
//                filmDao.deleteFilm(film.getFilm_id());
                setupEditableTableView();
                MainController.snackbar.show(I18N.get("user.deleted"), 3000);
            } else {
                MainController.snackbar.show(I18N.get("user.no.selected"), 3000);
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
                MainController.snackbar.show(I18N.get("user.no.selected"), 3000);
            }
        });
        contentFlow.withGlobalLink(treeTableViewAdd.getId(), RegisterController.class);
        contentFlow.withGlobalLink(treeTableViewEdit.getId(), RegisterController.class);
    }

    private void localeText() {
        lblTitle.textProperty().bind(I18N.createStringBinding("label.films"));
        searchField.promptTextProperty().bind(I18N.createStringBinding("text.search"));
        checkboxEditableColumn.textProperty().bind(I18N.createStringBinding("column.checkbox"));
        filmPhotoEditableColumn.textProperty().bind(I18N.createStringBinding("column.photo"));
        titleEditableColumn.textProperty().bind(I18N.createStringBinding("column.title"));
        releaseYearEditableColumn.textProperty().bind(I18N.createStringBinding("column.release_year"));
        rentalDurationEditableColumn.textProperty().bind(I18N.createStringBinding("column.rental_duration"));
        rentalRateEditableColumn.textProperty().bind(I18N.createStringBinding("column.rental_rate"));
        lengthEditableColumn.textProperty().bind(I18N.createStringBinding("column.length"));
        specialFeaturesEditableColumn.textProperty().bind(I18N.createStringBinding("column.special_features"));
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Film, T> column, Function<Film, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Film, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    private void setupEditableTableView() {
        // mybatis code generator and sakila
        titleEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, String>("title"));
        releaseYearEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, String>("release_year"));
        rentalDurationEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, String>("rental_duration"));
        rentalRateEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, String>("rental_rate"));
        lengthEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, Integer>("length"));
        specialFeaturesEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Film, String>("special_features"));
        // add editors
        Callback<TreeTableColumn<Film, Boolean>, TreeTableCell<Film, Boolean>> booleanCellFactory
                = new Callback<TreeTableColumn<Film, Boolean>, TreeTableCell<Film, Boolean>>() {
            @Override
            public TreeTableCell<Film, Boolean> call(TreeTableColumn<Film, Boolean> p) {
                return new BooleanCell();
            }
        };
        checkboxEditableColumn.setCellFactory(booleanCellFactory);
//        checkboxEditableColumn.setCellFactory( tc -> new CheckBoxTreeTableCell<>());
        rentalDurationEditableColumn.setCellFactory((TreeTableColumn<Film, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
//        rentalDurationEditableColumn.setOnEditCommit((CellEditEvent<Film, String> t) -> {
//            if (!t.getNewValue().equals(t.getOldValue())) {
//                t.getTreeTableView()
//                        .getTreeItem(t.getTreeTablePosition()
//                                .getRow())
//                        .getValue().firstName.set(t.getNewValue());
//                // update database
//                FilmDao filmDao = new FilmDao();
//                Film film = filmDao.getFilm(t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().id.get());
//                film.firstName.set(t.getNewValue().toUpperCase());
//                if (filmDao.updateFilm(film)) {
//                    MainController.snackbar.show(I18N.get("user.updated"), 3000);
//                }
//            }
//        });
        rentalRateEditableColumn.setCellFactory((TreeTableColumn<Film, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
//        rentalRateEditableColumn.setOnEditCommit((CellEditEvent<Film, String> t) -> {
//            t.getTreeTableView()
//                    .getTreeItem(t.getTreeTablePosition()
//                            .getRow())
//                    .getValue().lastName.set(t.getNewValue());
//        });
        lengthEditableColumn.setCellFactory((TreeTableColumn<Film, Integer> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new IntegerTextFieldEditorBuilder());
        });
//        lengthEditableColumn.setOnEditCommit((CellEditEvent<Film, Integer> t) -> {
//            t.getTreeTableView()
//                    .getTreeItem(t.getTreeTablePosition()
//                            .getRow())
//                    .getValue().mobile.set(t.getNewValue());
//        });

        // Set Data Table
        // Mybatis Code Generator Mapper
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            FilmMapper mapper = sqlSession.getMapper(FilmMapper.class);
            ObservableList<Film> dataFilm = FXCollections.observableArrayList(mapper.selectByExample(null));

            editableTreeTableView.setRoot(new RecursiveTreeItem<>(dataFilm, RecursiveTreeObject::getChildren));
            editableTreeTableView.setShowRoot(false);
            editableTreeTableView.setEditable(true);
            editableTreeTableViewCount.textProperty()
                    .bind(Bindings.createStringBinding(() -> PREFIX + editableTreeTableView.getCurrentItemsCount() + POSTFIX,
                            editableTreeTableView.currentItemsCountProperty()));
            editableTreeTableView.prefHeightProperty().bind(root.widthProperty());
//            searchField.textProperty()
//                    .addListener(setupSearchField(editableTreeTableView));
        } finally {
            sqlSession.close();
        }
    }
    //    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Film> tableView) {
    //        return (o, oldVal, newVal)
    //                -> tableView.setPredicate(filmProp -> {
    //                    final Film film = filmProp.getValue();
    //                    return film.firstName.get().contains(newVal)
    //                            || film.lastName.get().contains(newVal)
    //                            || Integer.toString(film.mobile.get()).contains(newVal);
    //                });
    //    }

    class BooleanCell extends TreeTableCell<Film, Boolean> {

        private JFXCheckBox checkBox;

        public BooleanCell() {
            checkBox = new JFXCheckBox();
            checkBox.setDisable(true);
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (isEditing()) {
                        commitEdit(newValue == null ? false : newValue);
                    }
                }
            });
            this.setGraphic(checkBox);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setEditable(true);
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (isEmpty()) {
                return;
            }
            checkBox.setDisable(false);
            checkBox.requestFocus();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            checkBox.setDisable(true);
        }

        public void commitEdit(Boolean value) {
            super.commitEdit(value);
            checkBox.setDisable(true);
        }

        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
//                checkBox.setSelected(item);
            }
        }
    }
}
