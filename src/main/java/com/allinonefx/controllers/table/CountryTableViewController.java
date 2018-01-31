package com.allinonefx.controllers.table;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.form.RegisterController;
import com.allinonefx.dao.CountryMapper;
import com.allinonefx.model.Country;
import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.util.VetoException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/table/TreeTableView.fxml", title = "Countries")
public class CountryTableViewController extends TreeTableViewController {

    CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);

    // editable table view
    @FXML
    private JFXTreeTableView<Country> editableTreeTableView;
    private JFXTreeTableColumn<Country, Integer> countryIdEditableColumn = new JFXTreeTableColumn();
    private JFXTreeTableColumn<Country, String> countryEditableColumn = new JFXTreeTableColumn();

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        //title
        MainController.lblTitle.setText("Countries");
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
                Logger.getLogger(CountryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(CountryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        treeTableViewEdit.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Country country = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                try {
                    context.register("editCountry", country);
                    contentFlowHandler.handle("treeTableViewEdit");
                } catch (VetoException ex) {
                    Logger.getLogger(CountryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FlowException ex) {
                    Logger.getLogger(CountryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                MainController.snackbar.show(I18N.get("country.no.selected"), 3000);
            }
        });
        treeTableViewRemove.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Country country = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                System.out.println(country.getCountry());
                if (mapper.deleteByPrimaryKey(country.getCountry_id()) == 1) {
                    sqlSession.commit();
                    setupEditableTableView();
                    MainController.snackbar.show(I18N.get("country.deleted"), 3000);
                }
            } else {
                MainController.snackbar.show(I18N.get("country.no.selected"), 3000);
            }
        });
    }

    public void setLocale() {
        super.setLocale();
        lblTitle.textProperty().bind(I18N.createStringBinding("label.countries"));
        countryIdEditableColumn.textProperty().bind(I18N.createStringBinding("column.id"));
        countryEditableColumn.textProperty().bind(I18N.createStringBinding("column.country"));
    }

    private void setupEditableTableView() {
        //set columns styles
        countryIdEditableColumn.getStyleClass().add("cell-right");
        countryEditableColumn.getStyleClass().add("cell-left");
        //set table columns
        editableTreeTableView.getColumns().clear();
        editableTreeTableView.getColumns().addAll(countryIdEditableColumn, countryEditableColumn);
        //set cell values
        countryIdEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Country, Integer>("country_id"));
        countryEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Country, String>("country"));
        // add editors
        countryEditableColumn.setCellFactory((TreeTableColumn<Country, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
        countryEditableColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Country, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<Country, String> t) {
                if (!t.getNewValue().equals(t.getOldValue())) {
                    try {
                        t.getTreeTableView()
                                .getTreeItem(t.getTreeTablePosition()
                                        .getRow())
                                .getValue().setCountry(t.getNewValue());
                        Country country = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                        country.setCountry(t.getNewValue().toUpperCase());
                        if (mapper.updateByPrimaryKey(country) == 1) {
                            sqlSession.commit();
                            MainController.snackbar.show(I18N.get("country.updated"), 3000);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        //set data table
        ObservableList<Country> dataCountry = FXCollections.observableArrayList(mapper.selectByExample(null));
        editableTreeTableView.setRoot(new RecursiveTreeItem<>(dataCountry, RecursiveTreeObject::getChildren));
        editableTreeTableView.setShowRoot(false);
        editableTreeTableView.setEditable(true);
        treeTableViewCount.textProperty().bind(Bindings.createStringBinding(() -> PREFIX + editableTreeTableView.getCurrentItemsCount() + POSTFIX, editableTreeTableView.currentItemsCountProperty()));
        editableTreeTableView.prefHeightProperty().bind(root.widthProperty());
        searchField.textProperty().addListener(setupSearchField(editableTreeTableView));
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Country> tableView) {
        return (o, oldVal, newVal)
                -> tableView.setPredicate(countryProp -> {
                    final Country country = countryProp.getValue();
                    return country.getCountry().contains(newVal);
                });
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Country, T> column, Function<Country, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Country, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

}
