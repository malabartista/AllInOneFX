package com.allinonefx.gui.uicomponents;

import com.allinonefx.config.DbHandler;
import com.allinonefx.controllers.MainController;
import com.allinonefx.controllers.RegisterController;
import com.allinonefx.models.Person;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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
    private JFXTreeTableView<Person> editableTreeTableView;
    @FXML
    private JFXTreeTableColumn<Person, ImageView> userPhotoEditableColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> firstNameEditableColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> lastNameEditableColumn;
    @FXML
    private JFXTreeTableColumn<Person, Integer> mobileEditableColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> emailEditableColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> locationEditableColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> genderEditableColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> levelEditableColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> departmentEditableColumn;
    @FXML
    private JFXTreeTableColumn<Person, String> courseEditableColumn;
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

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Person, T> column, Function<Person, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Person, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    private void setupEditableTableView() {
        setupCellValueFactory(userPhotoEditableColumn, Person::userPhotoProperty);
        setupCellValueFactory(firstNameEditableColumn, Person::firstNameProperty);
        setupCellValueFactory(lastNameEditableColumn, Person::lastNameProperty);
//        setupCellValueFactory(mobileEditableColumn, Person::mobileProperty);
        setupCellValueFactory(mobileEditableColumn, p -> p.mobile.asObject());
        setupCellValueFactory(emailEditableColumn, Person::emailProperty);
        setupCellValueFactory(locationEditableColumn, Person::locationProperty);
        setupCellValueFactory(genderEditableColumn, Person::genderProperty);
        setupCellValueFactory(levelEditableColumn, Person::levelProperty);
        setupCellValueFactory(departmentEditableColumn, Person::departmentProperty);
        setupCellValueFactory(courseEditableColumn, Person::courseProperty);

        // add editors
        firstNameEditableColumn.setCellFactory((TreeTableColumn<Person, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
        firstNameEditableColumn.setOnEditCommit((CellEditEvent<Person, String> t) -> {
            t.getTreeTableView()
                    .getTreeItem(t.getTreeTablePosition()
                            .getRow())
                    .getValue().firstName.set(t.getNewValue());
        });
        lastNameEditableColumn.setCellFactory((TreeTableColumn<Person, String> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new TextFieldEditorBuilder());
        });
        lastNameEditableColumn.setOnEditCommit((CellEditEvent<Person, String> t) -> {
            t.getTreeTableView()
                    .getTreeItem(t.getTreeTablePosition()
                            .getRow())
                    .getValue().lastName.set(t.getNewValue());
        });
        mobileEditableColumn.setCellFactory((TreeTableColumn<Person, Integer> param) -> {
            return new GenericEditableTreeTableCell<>(
                    new IntegerTextFieldEditorBuilder());
        });
        mobileEditableColumn.setOnEditCommit((CellEditEvent<Person, Integer> t) -> {
            t.getTreeTableView()
                    .getTreeItem(t.getTreeTablePosition()
                            .getRow())
                    .getValue().mobile.set(t.getNewValue());
        });

        // Data Table
        DbHandler handler = new DbHandler();
        Connection con = handler.getConnection();
        ObservableList<Person> data = FXCollections.observableArrayList();
        try {
            // mysql
            //String SQL = "SELECT * FROM students ORDER BY fname";
            // mssql
            String SQL = "SELECT * FROM Gestores ORDER BY NIF";
            ResultSet rs = con.createStatement().executeQuery(SQL);
            while (rs.next()) {
                // mysql
                //Person p = new Person(rs.getString("fname"), rs.getString("lname"), rs.getInt("phone"), rs.getString("email"), rs.getString("location"), rs.getString("gender"), rs.getString("level"), rs.getString("department"), rs.getString("course"));
                // mssql
                Person p = new Person(rs.getString("Nombre"), rs.getString("Apellido1RazonSocial"), rs.getInt("Telef"), rs.getString("EMAIL"), rs.getString("DirProvincia"), rs.getString("Sexo"), rs.getString("OFICINA"), rs.getString("DISCAPACIDAD"), rs.getString("ESTADOWEB"));
                String imagePath = "icons/profile1.png";// + rs.getString("id") + ".png";
                Image img = new Image(imagePath);
                ImageView mv = new ImageView();
                mv.setImage(img);
                mv.setFitWidth(70);
                mv.setFitHeight(80);
                p.userPhoto.set(mv);
//                p.firstName.set(rs.getString("fname"));
//                p.lastName.set(rs.getString("lname"));
//                p.mobile.set(rs.getInt("phone"));
//                p.email.set(rs.getString("email"));
//                p.location.set(rs.getString("location"));
//                p.gender.set(rs.getString("gender"));
                data.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
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

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Person> tableView) {
        return (o, oldVal, newVal)
                -> tableView.setPredicate(personProp -> {
                    final Person person = personProp.getValue();
                    return person.firstName.get().contains(newVal)
                            || person.lastName.get().contains(newVal)
                            || Integer.toString(person.mobile.get()).contains(newVal);
                });
    }

}
