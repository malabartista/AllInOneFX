package com.allinonefx.controllers.table;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.form.RegisterController;
import com.allinonefx.dao.CustomerMapperExtend;
import com.allinonefx.model.Customer;
import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.util.VetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.util.Callback;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/table/TreeTableView.fxml", title = "Customers")
public class CustomerTableViewController extends TreeTableViewController {

    CustomerMapperExtend mapper = sqlSession.getMapper(CustomerMapperExtend.class);

    // editable table view
    @FXML
    public JFXTreeTableView<Customer> editableTreeTableView;
    private final JFXTreeTableColumn<Customer, String> firstNameEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Customer, String> lastNameEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Customer, String> emailEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Customer, String> addressEditableColumn = new JFXTreeTableColumn();
    private final JFXTreeTableColumn<Customer, Boolean> activeEditableColumn = new JFXTreeTableColumn();

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        // title
        MainController.lblTitle.setText("Customers");
        // setup table
        setupEditableTableView();
        // locale
        setLocale();
        // flow: add register
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlow.withGlobalLink(treeTableViewAdd.getId(), RegisterController.class);
        contentFlow.withGlobalLink(treeTableViewEdit.getId(), RegisterController.class);
        // toolbar buttons
        treeTableViewAdd.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("treeTableViewAdd");
            } catch (VetoException ex) {
                Logger.getLogger(CustomerTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(CustomerTableViewController.class.getName()).log(Level.SEVERE, null, ex);
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
                MainController.snackbar.show(I18N.get("customer.no.selected"), 3000);
            }
        });
        treeTableViewRemove.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Customer customer = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                System.out.println(customer.getFirst_name());
                if (mapper.deleteByPrimaryKey(customer.getCustomer_id()) == 1) {
                    sqlSession.commit();
                    setupEditableTableView();
                    MainController.snackbar.show(I18N.get("customer.deleted"), 3000);
                }
            } else {
                MainController.snackbar.show(I18N.get("customer.no.selected"), 3000);
            }
        });
    }

    public void setLocale() {
        super.setLocale();
        lblTitle.textProperty().bind(I18N.createStringBinding("label.customers"));
        firstNameEditableColumn.textProperty().bind(I18N.createStringBinding("column.firstname"));
        lastNameEditableColumn.textProperty().bind(I18N.createStringBinding("column.lastname"));
        emailEditableColumn.textProperty().bind(I18N.createStringBinding("column.email"));
        addressEditableColumn.textProperty().bind(I18N.createStringBinding("column.address"));
        activeEditableColumn.textProperty().bind(I18N.createStringBinding("column.active"));
    }

    private void setupEditableTableView() {
        //set columns styles
        firstNameEditableColumn.getStyleClass().add("cell-left");
        lastNameEditableColumn.getStyleClass().add("cell-left");
        emailEditableColumn.getStyleClass().add("cell-left");
        addressEditableColumn.getStyleClass().add("cell-left");
        //set table columns
        editableTreeTableView.getColumns().clear();
        editableTreeTableView.getColumns().addAll(firstNameEditableColumn, lastNameEditableColumn, emailEditableColumn, addressEditableColumn, activeEditableColumn);
        //set cell values
        firstNameEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Customer, String>("first_name"));
        lastNameEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Customer, String>("last_name"));
        emailEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Customer, String>("email"));
        addressEditableColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Customer, String> p) {
                return new ReadOnlyStringWrapper(p.getValue().getValue().getAddress().getAddress());
            }
        });
        Callback<TreeTableColumn<Customer, Boolean>, TreeTableCell<Customer, Boolean>> booleanCellFactory
                = new Callback<TreeTableColumn<Customer, Boolean>, TreeTableCell<Customer, Boolean>>() {
            @Override
            public TreeTableCell<Customer, Boolean> call(TreeTableColumn<Customer, Boolean> p) {
                BooleanCell<Customer> cell = new BooleanCell() {
                    @Override
                    public void commitEdit(Boolean value) {
                        Customer customer = (Customer) editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                        customer.setActive(value);
                        if (mapper.updateByPrimaryKey(customer) == 1) {
                            sqlSession.commit();
                            MainController.snackbar.show(I18N.get("customer.updated"), 3000);
                        }
                    }
                };;
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };
        activeEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Customer, Boolean>("active"));
        activeEditableColumn.setCellFactory(booleanCellFactory);
        //add editors
        firstNameEditableColumn.setCellFactory((TreeTableColumn<Customer, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
        firstNameEditableColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Customer, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Customer, String> t) {
                if (!t.getNewValue().equals(t.getOldValue())) {
                    try {
                        //update table
                        t.getTreeTableView()
                                .getTreeItem(t.getTreeTablePosition()
                                        .getRow())
                                .getValue().setFirst_name(t.getNewValue());
                        // update database
                        Customer customer = (Customer) editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                        customer.setFirst_name(t.getNewValue().toUpperCase());
                        if (mapper.updateByPrimaryKey(customer) == 1) {
                            sqlSession.commit();
                            MainController.snackbar.show(I18N.get("customer.updated"), 3000);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        lastNameEditableColumn.setCellFactory((TreeTableColumn<Customer, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
        lastNameEditableColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Customer, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Customer, String> t) {
                if (!t.getNewValue().equals(t.getOldValue())) {
                    try {
                        //update table
                        t.getTreeTableView()
                                .getTreeItem(t.getTreeTablePosition()
                                        .getRow())
                                .getValue().setLast_name(t.getNewValue());
                        // update database
                        Customer customer = (Customer) editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                        customer.setLast_name(t.getNewValue().toUpperCase());
                        if (mapper.updateByPrimaryKey(customer) == 1) {
                            sqlSession.commit();
                            MainController.snackbar.show(I18N.get("customer.updated"), 3000);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        emailEditableColumn.setCellFactory((TreeTableColumn<Customer, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
        emailEditableColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Customer, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Customer, String> t) {
                if (!t.getNewValue().equals(t.getOldValue())) {
                    try {
                        //update table
                        t.getTreeTableView()
                                .getTreeItem(t.getTreeTablePosition()
                                        .getRow())
                                .getValue().setEmail(t.getNewValue());
                        // update database
                        Customer customer = (Customer) editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                        customer.setEmail(t.getNewValue());
                        if (mapper.updateByPrimaryKey(customer) == 1) {
                            sqlSession.commit();
                            MainController.snackbar.show(I18N.get("customer.updated"), 3000);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        ObservableList<Customer> dataCustomer = FXCollections.observableArrayList(mapper.selectByExampleWithAddress(null));
        editableTreeTableView.setRoot(new RecursiveTreeItem<>(dataCustomer, RecursiveTreeObject::getChildren));
        editableTreeTableView.setShowRoot(false);
        editableTreeTableView.setEditable(true);
        treeTableViewCount.textProperty().bind(Bindings.createStringBinding(() -> PREFIX + editableTreeTableView.getCurrentItemsCount() + POSTFIX, editableTreeTableView.currentItemsCountProperty()));
        editableTreeTableView.prefHeightProperty().bind(root.widthProperty());
        searchField.textProperty().addListener(setupSearchField(editableTreeTableView));
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Customer> tableView) {
        return (o, oldVal, newVal)
                -> tableView.setPredicate(customerProp -> {
                    final Customer customer = customerProp.getValue();
                    return customer.getFirst_name().contains(newVal)
                            || customer.getLast_name().contains(newVal)
                            || customer.getAddress().getAddress().contains(newVal);
                });
    }

}
