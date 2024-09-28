/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.controller.ScreenInterface;
import org.guanzon.cas.controller.unloadForm;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.model.ModelPurchaseOrder;
import org.guanzon.cas.purchasing.controller.PurchaseOrder;
import org.json.simple.JSONObject;

/**
 * s
 *
 * @author User
 */
//PO_Main_1Controller
public class PurchaseOrderConfirmationController implements Initializable, ScreenInterface {

    private final String pxeModuleName = "Purchase Order(for Motorcycle, Mobile Phone, Cars)";
    private GRider oApp;
    private PurchaseOrder oTrans;
    private JSONObject poJSON;
    private int pnEditMode;

    private String psPrimary = "";

    private boolean pbLoaded = false;
    private int pnIndex;
    private int pnDetailRow;
    
    @FXML
    private AnchorPane MainAnchorPane;

    @FXML
    private AnchorPane apButton;
    @FXML
    private AnchorPane apDetail;
    @FXML
    private AnchorPane apMaster;
    @FXML
    private AnchorPane apTable;
    @FXML
    private AnchorPane apBrowse;
    @FXML
    private AnchorPane apTransactionIssues;
//    @FXML
//    private TableColumn index01, index02, index03, index04, index05, index06, index07;


    @FXML
    private Button btnBrowse, btnNew, btnUpdate, btnPrint, btnClose,
            btnFindSource, btnSearch, btnSave, btnRemoveItem, btnCancel, btnAddItem;


    @FXML
    private HBox hbButtons;

    @FXML
    private HBox hbButtons1;

    @FXML
    private TextField txtField01, txtField02, txtField03, txtField04, txtField05, txtField06, txtField08, txtField09, txtField10,
            txtField11, txtField12, txtField99, txtField98, txtField97;

    @FXML
    private TextArea txtField07;
    

    @FXML
    private Label lblStatus;


    @FXML
    private TextField txtDetail01, txtDetail02, txtDetail03, txtDetail04, txtDetail05, txtDetail06, txtDetail07;

    @FXML
    private TableView tblDetails;

    @FXML
    private TableColumn index01, index02, index03, index04, index05, index06, index07, index08, index09, index10, index11;
    
    
    

    @FXML
    private Label lblStatus1;

    @FXML
    private TableColumn index12;

    @FXML
    private TableColumn  index13;


   
    
    
    private ObservableList<ModelPurchaseOrder> data = FXCollections.observableArrayList();




    @FXML
    void cmdButton_Click(ActionEvent event) {

    }

    @FXML
    void tblDetails_Clicked(MouseEvent event) {

    }

    public void initDetailsGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index07.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");

        index01.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index07"));

        tblDetails.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblDetails.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblDetails.setItems(data);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        oTrans = new PurchaseOrder(oApp, false);
        oTrans.setTransactionStatus("12340");

//        initTextFields();
        initDetailsGrid();
//        clearFields();

        pnEditMode = EditMode.UNKNOWN;
//        initButton(pnEditMode);
        pbLoaded = true;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
