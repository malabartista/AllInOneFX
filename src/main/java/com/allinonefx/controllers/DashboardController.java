/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.controllers;

import com.allinonefx.config.I18N;
import com.allinonefx.controllers.table.CategoryTableViewController;
import com.allinonefx.controllers.table.CountryTableViewController;
import com.allinonefx.controllers.table.CustomerTableViewController;
import com.allinonefx.controllers.table.FilmTableViewController;
import com.allinonefx.dao.CategoryMapper;
import com.allinonefx.dao.CountryMapper;
import com.allinonefx.dao.CustomerMapperExtend;
import com.allinonefx.dao.FilmMapper;
import com.allinonefx.model.Category;
import com.allinonefx.model.Staff;
import com.allinonefx.mybatis.MyBatisConnectionFactory;
import com.allinonefx.utils.ImageUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import org.apache.ibatis.session.SqlSession;

/**
 * FXML Controller class
 *
 */
@ViewController(value = "/fxml/Dashboard.fxml", title = "Dashboard")
public class DashboardController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    SqlSession sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession();

    @FXML
    private GridPane root;
    @FXML
    private Pane paneCustomers;
    @FXML
    private Pane paneFilms;
    @FXML
    private Pane paneCategories;
    @FXML
    private Pane paneCountries;
    @FXML
    private Label lblCustomers;
    @FXML
    private Label lblSepCustomers;
    @FXML
    private Label totalCustomers;
    @FXML
    private Label totalNumberCustomers;
    @FXML
    private Label lblFilms;
    @FXML
    private Label lblSepFilms;
    @FXML
    private Label totalFilms;
    @FXML
    private Label totalNumberFilms;
    @FXML
    private Label lblCategories;
    @FXML
    private Label lblSepCategories;
    @FXML
    private Label totalCategories;
    @FXML
    private Label totalNumberCategories;
    @FXML
    private Label lblCountries;
    @FXML
    private Label lblSepCountries;
    @FXML
    private Label totalCountries;
    @FXML
    private Label totalNumberCountries;
    @FXML
    private Label lblFilmsCategories;
    @FXML
    private Label lblRecentNotifications;

    //profile
    @FXML
    private Label lblProfileName;
    @FXML
    private Label lblProfileSubname;
    @FXML
    private Label lblProfileEmail;
    @FXML
    private Label lblProfileAddress;
    @FXML
    private Label lblProfileCreated;
    @FXML
    private Label lblProfileLastUpdate;
    @FXML
    private ImageView imgProfile;

    //categories
    @FXML
    private StackPane catStackPane;
    @FXML
    private JFXListView<Category> categoryList;
    private ObservableList<Category> categories;
    @FXML
    private Button btnAddCategory;
    JFXTextField txtCategory;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        setLocale();
        setProfile();
        setRipples();
        setTotals();
        setUpCategories();
    }

    private void setLocale() {
        lblCategories.textProperty().bind(I18N.createStringBinding("label.categories"));
        totalNumberCategories.textProperty().bind(I18N.createStringBinding("label.total.categories"));
        lblCountries.textProperty().bind(I18N.createStringBinding("label.countries"));
        totalNumberCountries.textProperty().bind(I18N.createStringBinding("label.total.countries"));
        lblCustomers.textProperty().bind(I18N.createStringBinding("label.customers"));
        totalNumberCustomers.textProperty().bind(I18N.createStringBinding("label.total.customers"));
        lblFilms.textProperty().bind(I18N.createStringBinding("label.films"));
        totalNumberFilms.textProperty().bind(I18N.createStringBinding("label.total.films"));
        lblFilmsCategories.textProperty().bind(I18N.createStringBinding("label.films.categories"));
        lblRecentNotifications.textProperty().bind(I18N.createStringBinding("label.recent.notifications"));
    }

    private void setProfile() {
        try {
            //get user
            Staff staff = (Staff) context.getRegisteredObject("User");
            InputStream in = new ByteArrayInputStream(staff.getPicture());
            BufferedImage imageRounded = ImageUtils.makeRoundedCorner(ImageIO.read(in), 160);
            Image image = SwingFXUtils.toFXImage(imageRounded, null);
            imgProfile.setImage(image);
            lblProfileName.setText(staff.getFirst_name() + " " + staff.getLast_name());
            lblProfileSubname.setText(staff.getFirst_name() + " " + staff.getLast_name());
            lblProfileEmail.setText(staff.getEmail());
            lblProfileAddress.setText(staff.getAddress().getAddress());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            lblProfileCreated.setText(df.format(staff.getLast_update()));
            lblProfileLastUpdate.setText(df.format(staff.getLast_update()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setRipples() {
//        JFXRippler rippler1 = new JFXRippler(paneFilms);
//        JFXRippler rippler2 = new JFXRippler(paneCustomers);
//        JFXRippler rippler3 = new JFXRippler(paneCategories);
//        JFXRippler rippler4 = new JFXRippler(paneCountries);
//        rippler1.setMaskType(JFXRippler.RipplerMask.RECT);
//        rippler2.setMaskType(JFXRippler.RipplerMask.RECT);
//        rippler3.setMaskType(JFXRippler.RipplerMask.RECT);
//        rippler4.setMaskType(JFXRippler.RipplerMask.RECT);
//
//        rippler1.setRipplerFill(Paint.valueOf("#1564C0"));
//        rippler2.setRipplerFill(Paint.valueOf("#00AACF"));
//        rippler3.setRipplerFill(Paint.valueOf("#00B3A0"));
//        rippler4.setRipplerFill(Paint.valueOf("#F87951"));
//
//        root.getChildren().addAll(rippler1, rippler2, rippler3, rippler4);
//        root.getChildren().add(rippler4);
//        root.getChildren().add(rippler2);
    }

    private void setTotals() {
        //flow
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        contentFlow.withGlobalLink(paneCustomers.getId(), CustomerTableViewController.class);
        contentFlow.withGlobalLink(paneFilms.getId(), FilmTableViewController.class);
        contentFlow.withGlobalLink(paneCategories.getId(), CategoryTableViewController.class);
        contentFlow.withGlobalLink(paneCountries.getId(), CountryTableViewController.class);

        // Customers
        CustomerMapperExtend customerMapper = sqlSession.getMapper(CustomerMapperExtend.class);
        totalCustomers.setText(Long.toString(customerMapper.countByExample(null)));
        paneCustomers.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("paneCustomers");
            } catch (VetoException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Films
        FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);
        totalFilms.setText(Long.toString(filmMapper.countByExample(null)));
        paneFilms.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("paneFilms");
            } catch (VetoException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Categories
        CategoryMapper categoryMapper = sqlSession.getMapper(CategoryMapper.class);
        totalCategories.setText(Long.toString(categoryMapper.countByExample(null)));
        paneCategories.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("paneCategories");
            } catch (VetoException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Countries
        CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
        totalCountries.setText(Long.toString(countryMapper.countByExample(null)));
        paneCountries.setOnMouseClicked((e) -> {
            try {
                contentFlowHandler.handle("paneCountries");
            } catch (VetoException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FlowException ex) {
                Logger.getLogger(WidgetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // label separators
        lblSepCustomers.prefWidthProperty().bind(paneCustomers.widthProperty().subtract(22));
        lblSepFilms.prefWidthProperty().bind(paneFilms.widthProperty().subtract(22));
        lblSepCategories.prefWidthProperty().bind(paneCategories.widthProperty().subtract(22));
        lblSepCountries.prefWidthProperty().bind(paneCountries.widthProperty().subtract(22));

        dragAndDrop(paneFilms, root);
    }

    private void setUpCategories() {
        categoryList.getItems().clear();
        categoryList.setCellFactory(param -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        try {
            CategoryMapper mapper = sqlSession.getMapper(CategoryMapper.class);
            categories = FXCollections.observableArrayList(mapper.selectByExample(null));
            categoryList.getItems().addAll(categories);
        } catch (Exception ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
        btnAddCategory.setOnMouseClicked((e) -> {
            addCategory(e);
        });
    }

    private void addCategory(MouseEvent event) {
        //Body Input text
        txtCategory = new JFXTextField();
        txtCategory.setPromptText("Enter new category name");
        txtCategory.setLabelFloat(false);
        txtCategory.setPrefSize(150, 50);
        txtCategory.setPadding(new Insets(10, 5, 10, 5));
        txtCategory.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");
        // Heading text
        Text t = new Text("Add New Category");
        t.setStyle("-fx-font-size:14px;");

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(t);
        dialogLayout.setBody(txtCategory);

        JFXDialog dialog = new JFXDialog(catStackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        // close button
        JFXButton closeButton = new JFXButton("Dismiss");
        closeButton.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;");
        //Add button
        JFXButton addBtn = new JFXButton("Add");
        addBtn.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;"
                + "");
        closeButton.setOnAction((ActionEvent event1) -> {
            dialog.close();
        });
        addBtn.setOnAction((ActionEvent event1) -> {
            System.out.println(txtCategory.getText());
            saveCategory();
            setUpCategories();
            dialog.close();
        });

        HBox box = new HBox();
        box.setSpacing(20);
        box.setPrefSize(200, 50);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().addAll(addBtn, closeButton);

        dialogLayout.setActions(box);
        dialog.show();
    }

    private void saveCategory() {
        String name = txtCategory.getText().trim();
        try {
            if (name.isEmpty() || name.equals("")) {
                return;
            }
            Category category = new Category();
            category.setName(name);
            category.setLast_update(new Date());
            CategoryMapper categoryMapper = sqlSession.getMapper(CategoryMapper.class);
            if (categoryMapper.insert(category) == 1) {
                sqlSession.commit();
                MainController.snackbar.show(I18N.get("category.new.saved"), 3000);
            }
        } catch (Exception ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void dragAndDrop(Pane pane, GridPane board) {
        List<Node> squares = board.getChildrenUnmodifiable();
        for (int i = 0; i < 6; i++) {
            Node target = squares.get(i);
            pane.setOnDragDetected(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    Dragboard db = pane.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(imgProfile.getImage());
                    db.setContent(content);
                    System.out.println("Drag detected");
                    event.consume();
                }
            });
            target.setOnDragOver(new EventHandler<DragEvent>() {
                public void handle(DragEvent event) {
                    if (event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.ANY);
                    }
                    int col = GridPane.getColumnIndex(target);
                    int row = GridPane.getRowIndex(target);
                    System.out.println("Drag Over Detected over column " + col + " and row " + row);
                    event.consume();
                }
            });
            target.setOnDragDropped(new EventHandler<DragEvent>() {
                public void handle(DragEvent event) {
                    event.acceptTransferModes(TransferMode.ANY);
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasImage()) {
                        Pane piece = new Pane();
                        piece.setClip(new ImageView(db.getImage()));
                        dragAndDrop(piece, board);
                        board.add(piece, GridPane.getColumnIndex(target), GridPane.getRowIndex(target));
                        success = true;
                    }
                    event.setDropCompleted(success);
                    System.out.println("Drop detected");
                    event.consume();
                }
            });
            pane.setOnDragDone(new EventHandler<DragEvent>() {
                public void handle(DragEvent event) {
                    if (event.getTransferMode() == TransferMode.MOVE) {
                        pane.setClip(null);
                    }
                    System.out.println("Drag Complete!");
                    event.consume();
                }
            });
        }
    }

}
