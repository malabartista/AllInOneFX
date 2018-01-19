package com.allinonefx.controllers.table;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.RegisterController;
import com.allinonefx.mybatis.MyBatisConnectionFactory;
import com.jfoenix.controls.*;
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
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javax.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSession;
import com.allinonefx.dao.CustomerMapper;
import com.allinonefx.model.Customer;

@ViewController(value = "/fxml/ui/CustomerTableView.fxml", title = "Customers Table")
public class CustomerTableViewController {

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
    private JFXTreeTableView<Customer> editableTreeTableView;
    @FXML
    private JFXTreeTableColumn<Customer, String> firstNameEditableColumn;
    @FXML
    private JFXTreeTableColumn<Customer, String> lastNameEditableColumn;
    @FXML
    private JFXTreeTableColumn<Customer, String> emailEditableColumn;
    @FXML
    private JFXTreeTableColumn<Customer, Boolean> activeEditableColumn;
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
                Logger.getLogger(CustomerTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(CustomerTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        treeTableViewAdd.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("treeTableViewAdd");
            } catch (VetoException ex) {
                Logger.getLogger(CustomerTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(CustomerTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        treeTableViewRemove.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Customer customer = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                System.out.println(customer.getFirst_name());
//                CustomerDao customerDao = new CustomerDao();
//                customerDao.deleteCustomer(customer.getCustomer_id());
                setupEditableTableView();
                MainController.snackbar.show(I18N.get("user.deleted"), 3000);
            } else {
                MainController.snackbar.show(I18N.get("user.no.selected"), 3000);
            }
        });
        treeTableViewEdit.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Customer customer = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                try {
                    context.register("editCustomer", customer);
                    contentFlowHandler.handle("treeTableViewEdit");
                } catch (VetoException ex) {
                    Logger.getLogger(CustomerTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FlowException ex) {
                    Logger.getLogger(CustomerTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                MainController.snackbar.show(I18N.get("user.no.selected"), 3000);
            }
        });
        contentFlow.withGlobalLink(treeTableViewAdd.getId(), RegisterController.class);
        contentFlow.withGlobalLink(treeTableViewEdit.getId(), RegisterController.class);
    }

    private void localeText() {
        lblTitle.textProperty().bind(I18N.createStringBinding("label.customers"));
        searchField.promptTextProperty().bind(I18N.createStringBinding("text.search"));
        firstNameEditableColumn.textProperty().bind(I18N.createStringBinding("column.firstname"));
        lastNameEditableColumn.textProperty().bind(I18N.createStringBinding("column.lastname"));
        emailEditableColumn.textProperty().bind(I18N.createStringBinding("column.email"));
        activeEditableColumn.textProperty().bind(I18N.createStringBinding("column.active"));
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Customer, T> column, Function<Customer, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    private void setupEditableTableView() {
        // mybatis code generator and sakila
        firstNameEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Customer, String>("first_name"));
        lastNameEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Customer, String>("last_name"));
        emailEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Customer, String>("email"));
        Callback<TreeTableColumn<Customer, Boolean>, TreeTableCell<Customer, Boolean>> booleanCellFactory
                = new Callback<TreeTableColumn<Customer, Boolean>, TreeTableCell<Customer, Boolean>>() {
            @Override
            public TreeTableCell<Customer, Boolean> call(TreeTableColumn<Customer, Boolean> p) {
                return new BooleanCell();
            }
        };
        activeEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Customer,Boolean>("active"));
        activeEditableColumn.setCellFactory(booleanCellFactory);
        
        // add editors
        emailEditableColumn.setCellFactory((TreeTableColumn<Customer, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });

        // Set Data Table
        // Mybatis Code Generator Mapper
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
            ObservableList<Customer> dataCustomer = FXCollections.observableArrayList(mapper.selectByExample(null));

            editableTreeTableView.setRoot(new RecursiveTreeItem<>(dataCustomer, RecursiveTreeObject::getChildren));
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
    //    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Customer> tableView) {
    //        return (o, oldVal, newVal)
    //                -> tableView.setPredicate(customerProp -> {
    //                    final Customer customer = customerProp.getValue();
    //                    return customer.firstName.get().contains(newVal)
    //                            || customer.lastName.get().contains(newVal)
    //                            || Integer.toString(customer.mobile.get()).contains(newVal);
    //                });
    //    }

    class BooleanCell extends TreeTableCell<Customer, Boolean> {

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
