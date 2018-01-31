package com.allinonefx.controllers.table;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.form.RegisterController;
import com.allinonefx.dao.CategoryMapper;
import com.allinonefx.model.Category;
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
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/table/TreeTableView.fxml", title = "Categories")
public class CategoryTableViewController extends TreeTableViewController {

    CategoryMapper mapper = sqlSession.getMapper(CategoryMapper.class);

    @FXML
    private JFXTreeTableView<Category> editableTreeTableView;
    private JFXTreeTableColumn<Category, Integer> categoryIdEditableColumn = new JFXTreeTableColumn();
    private JFXTreeTableColumn<Category, String> nameEditableColumn = new JFXTreeTableColumn();

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        //title
        MainController.lblTitle.setText("Categories");
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
                Logger.getLogger(CategoryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(CategoryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        treeTableViewEdit.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Category category = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                try {
                    context.register("editCategory", category);
                    contentFlowHandler.handle("treeTableViewEdit");
                } catch (VetoException ex) {
                    Logger.getLogger(CategoryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FlowException ex) {
                    Logger.getLogger(CategoryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                MainController.snackbar.show(I18N.get("category.no.selected"), 3000);
            }
        });
        treeTableViewRemove.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Category category = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                System.out.println(category.getName());
                if (mapper.deleteByPrimaryKey(category.getCategory_id()) == 1) {
                    sqlSession.commit();
                    setupEditableTableView();
                    MainController.snackbar.show(I18N.get("category.deleted"), 3000);
                }
            } else {
                MainController.snackbar.show(I18N.get("category.no.selected"), 3000);
            }
        });
    }

    public void setLocale() {
        super.setLocale();
        lblTitle.textProperty().bind(I18N.createStringBinding("label.categories"));
        categoryIdEditableColumn.textProperty().bind(I18N.createStringBinding("column.id"));
        nameEditableColumn.textProperty().bind(I18N.createStringBinding("column.name"));
    }

    private void setupEditableTableView() {
        //set columns styles
        categoryIdEditableColumn.getStyleClass().add("cell-right");
        nameEditableColumn.getStyleClass().add("cell-left");
        //set table columns
        editableTreeTableView.getColumns().clear();
        editableTreeTableView.getColumns().addAll(categoryIdEditableColumn, nameEditableColumn);
        //set cell values
        categoryIdEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Category, Integer>("category_id"));
        nameEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Category, String>("name"));
        // add editors
        nameEditableColumn.setCellFactory((TreeTableColumn<Category, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
        nameEditableColumn.setOnEditCommit(new EventHandler<CellEditEvent<Category, String>>() {
            @Override
            public void handle(CellEditEvent<Category, String> t) {
                if (!t.getNewValue().equals(t.getOldValue())) {
                    try {
                        t.getTreeTableView()
                                .getTreeItem(t.getTreeTablePosition()
                                        .getRow())
                                .getValue().setName(t.getNewValue());
                        Category category = (Category) editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                        category.setName(t.getNewValue().toUpperCase());
                        if (mapper.updateByPrimaryKey(category) == 1) {
                            sqlSession.commit();
                            MainController.snackbar.show(I18N.get("category.updated"), 3000);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(FilmTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        //set data table
        ObservableList<Category> dataCategory = FXCollections.observableArrayList(mapper.selectByExample(null));
        editableTreeTableView.setRoot(new RecursiveTreeItem<>(dataCategory, RecursiveTreeObject::getChildren));
        editableTreeTableView.setShowRoot(false);
        editableTreeTableView.setEditable(true);
        editableTreeTableView.prefHeightProperty().bind(root.widthProperty());
        treeTableViewCount.textProperty().bind(Bindings.createStringBinding(() -> PREFIX + editableTreeTableView.getCurrentItemsCount() + POSTFIX, editableTreeTableView.currentItemsCountProperty()));
        searchField.textProperty().addListener(setupSearchField(editableTreeTableView));
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Category> tableView) {
        return (o, oldVal, newVal)
                -> tableView.setPredicate(categoryProp -> {
                    final Category category = categoryProp.getValue();
                    return category.getName().contains(newVal);
                });
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Category, T> column, Function<Category, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Category, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }
}
