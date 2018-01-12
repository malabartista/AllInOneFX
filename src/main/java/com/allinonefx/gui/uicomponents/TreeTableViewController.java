package com.allinonefx.gui.uicomponents;

import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.RegisterController;
import com.allinonefx.dao.UserDao;
import com.allinonefx.models.User;
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
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/TreeTableView.fxml", title = "Tree Table View")
public class TreeTableViewController {

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
    private JFXTreeTableView<User> editableTreeTableView;
    @FXML
    private JFXTreeTableColumn<User, ImageView> userPhotoEditableColumn;
    @FXML
    private JFXTreeTableColumn<User, String> firstNameEditableColumn;
    @FXML
    private JFXTreeTableColumn<User, String> lastNameEditableColumn;
    @FXML
    private JFXTreeTableColumn<User, Integer> mobileEditableColumn;
    @FXML
    private JFXTreeTableColumn<User, String> emailEditableColumn;
    @FXML
    private JFXTreeTableColumn<User, String> locationEditableColumn;
    @FXML
    private JFXTreeTableColumn<User, String> genderEditableColumn;
    @FXML
    private JFXTreeTableColumn<User, String> levelEditableColumn;
    @FXML
    private JFXTreeTableColumn<User, String> departmentEditableColumn;
    @FXML
    private JFXTreeTableColumn<User, String> courseEditableColumn;
    @FXML
    private JFXTreeTableColumn<User, Boolean> checkboxEditableColumn;
    @FXML
    private Label treeTableViewCount;
    @FXML
    private JFXButton treeTableViewAdd;
    @FXML
    private JFXButton treeTableViewRemove;
    @FXML
    private Label editableTreeTableViewCount;
    @FXML
    private JFXTextField searchField2;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        //title
        MainController.lblTitle.setText("Tree Table View");
        // setup table
        setupEditableTableView();
        // flow: add register
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        addRegister.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("addRegister");
            } catch (VetoException ex) {
                Logger.getLogger(TreeTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(TreeTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        treeTableViewAdd.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("addRegister");
            } catch (VetoException ex) {
                Logger.getLogger(TreeTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(TreeTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        contentFlow.withGlobalLink(addRegister.getId(), RegisterController.class);
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<User, T> column, Function<User, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    private void setupEditableTableView() {
        setupCellValueFactory(checkboxEditableColumn, User::checkboxProperty);
        setupCellValueFactory(userPhotoEditableColumn, User::userPhotoProperty);
        setupCellValueFactory(firstNameEditableColumn, User::firstNameProperty);
        setupCellValueFactory(lastNameEditableColumn, User::lastNameProperty);
//        setupCellValueFactory(mobileEditableColumn, User::mobileProperty);
        setupCellValueFactory(mobileEditableColumn, p -> p.mobile.asObject());
        setupCellValueFactory(emailEditableColumn, User::emailProperty);
        setupCellValueFactory(locationEditableColumn, User::locationProperty);
        setupCellValueFactory(genderEditableColumn, User::genderProperty);
        setupCellValueFactory(levelEditableColumn, User::levelProperty);
        setupCellValueFactory(departmentEditableColumn, User::departmentProperty);
        setupCellValueFactory(courseEditableColumn, User::courseProperty);

        // add editors
        Callback<TreeTableColumn<User, Boolean>, TreeTableCell<User, Boolean>> booleanCellFactory
                = new Callback<TreeTableColumn<User, Boolean>, TreeTableCell<User, Boolean>>() {
            @Override
            public TreeTableCell<User, Boolean> call(TreeTableColumn<User, Boolean> p) {
                return new BooleanCell();
            }
        };
        checkboxEditableColumn.setCellFactory(booleanCellFactory);
//        checkboxEditableColumn.setCellFactory( tc -> new CheckBoxTreeTableCell<>());
        firstNameEditableColumn.setCellFactory((TreeTableColumn<User, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
        firstNameEditableColumn.setOnEditCommit((CellEditEvent<User, String> t) -> {
            t.getTreeTableView()
                    .getTreeItem(t.getTreeTablePosition()
                            .getRow())
                    .getValue().firstName.set(t.getNewValue());
        });
        lastNameEditableColumn.setCellFactory((TreeTableColumn<User, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
        lastNameEditableColumn.setOnEditCommit((CellEditEvent<User, String> t) -> {
            t.getTreeTableView()
                    .getTreeItem(t.getTreeTablePosition()
                            .getRow())
                    .getValue().lastName.set(t.getNewValue());
        });
        mobileEditableColumn.setCellFactory((TreeTableColumn<User, Integer> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new IntegerTextFieldEditorBuilder());
        });
        mobileEditableColumn.setOnEditCommit((CellEditEvent<User, Integer> t) -> {
            t.getTreeTableView()
                    .getTreeItem(t.getTreeTablePosition()
                            .getRow())
                    .getValue().mobile.set(t.getNewValue());
        });

        // Set Data Table
        UserDao userDao = new UserDao();
        ObservableList<User> data = FXCollections.observableArrayList(userDao.getAllUsers());

        editableTreeTableView.setRoot(new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren));
        editableTreeTableView.setShowRoot(false);
        editableTreeTableView.setEditable(true);
        editableTreeTableViewCount.textProperty()
                .bind(Bindings.createStringBinding(() -> PREFIX + editableTreeTableView.getCurrentItemsCount() + POSTFIX,
                        editableTreeTableView.currentItemsCountProperty()));
        editableTreeTableView.prefHeightProperty().bind(root.widthProperty());
        searchField2.textProperty()
                .addListener(setupSearchField(editableTreeTableView));
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<User> tableView) {
        return (o, oldVal, newVal)
                -> tableView.setPredicate(userProp -> {
                    final User user = userProp.getValue();
                    return user.firstName.get().contains(newVal)
                            || user.lastName.get().contains(newVal)
                            || Integer.toString(user.mobile.get()).contains(newVal);
                });
    }

    class BooleanCell extends TreeTableCell<User, Boolean> {

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
                checkBox.setSelected(item);
            }
        }
    }
}
