package com.allinonefx.controllers.table;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.RegisterController;
import com.allinonefx.dao.CountryMapper;
import com.allinonefx.model.Country;
import com.allinonefx.mybatis.MyBatisConnectionFactory;
import com.jfoenix.controls.*;
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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.StackPane;
import javax.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSession;

@ViewController(value = "/fxml/ui/CountryTableView.fxml", title = "Countries Table")
public class CountryTableViewController {

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
    private JFXTreeTableView<Country> editableTreeTableView;
    @FXML
    private JFXTreeTableColumn<Country, Integer> countryIdEditableColumn;
    @FXML
    private JFXTreeTableColumn<Country, String> countryEditableColumn;
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
                Logger.getLogger(CountryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(CountryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        treeTableViewAdd.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("treeTableViewAdd");
            } catch (VetoException ex) {
                Logger.getLogger(CountryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(CountryTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        treeTableViewRemove.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                Country country = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                System.out.println(country.getCountry());
//                CountryDao countryDao = new CountryDao();
//                countryDao.deleteCountry(country.getCountry_id());
                setupEditableTableView();
                MainController.snackbar.show(I18N.get("user.deleted"), 3000);
            } else {
                MainController.snackbar.show(I18N.get("user.no.selected"), 3000);
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
                MainController.snackbar.show(I18N.get("user.no.selected"), 3000);
            }
        });
        contentFlow.withGlobalLink(treeTableViewAdd.getId(), RegisterController.class);
        contentFlow.withGlobalLink(treeTableViewEdit.getId(), RegisterController.class);
    }

    private void localeText() {
        lblTitle.textProperty().bind(I18N.createStringBinding("label.countries"));
        searchField.promptTextProperty().bind(I18N.createStringBinding("text.search"));
        countryIdEditableColumn.textProperty().bind(I18N.createStringBinding("column.id"));
        countryEditableColumn.textProperty().bind(I18N.createStringBinding("column.country"));
        
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

    private void setupEditableTableView() {
        // mybatis code generator and sakila
        countryEditableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Country, String>("country"));
        
        // add editors
        

        // Set Data Table
        // Mybatis Code Generator Mapper
        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            ObservableList<Country> dataCountry = FXCollections.observableArrayList(mapper.selectByExample(null));

            editableTreeTableView.setRoot(new RecursiveTreeItem<>(dataCountry, RecursiveTreeObject::getChildren));
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
    //    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Country> tableView) {
    //        return (o, oldVal, newVal)
    //                -> tableView.setPredicate(countryProp -> {
    //                    final Country country = countryProp.getValue();
    //                    return country.firstName.get().contains(newVal)
    //                            || country.lastName.get().contains(newVal)
    //                            || Integer.toString(country.mobile.get()).contains(newVal);
    //                });
    //    }

}
