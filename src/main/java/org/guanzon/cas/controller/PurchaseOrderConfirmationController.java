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
    private TableColumn index01, index02, index03, index04, index05, index06, index07;

    private ObservableList<ModelPurchaseOrder> data = FXCollections.observableArrayList();

    @FXML
    private AnchorPane MainAnchorPane;

    @FXML
    private AnchorPane apButton;

    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnPrint;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnClose;

    @FXML
    private AnchorPane apMaster;

    @FXML
    private Label lblStatus;

    @FXML
    private TextField txtField01;

    @FXML
    private TextField txtField02;

    @FXML
    private TextField txtField03;

    @FXML
    private TextField txtField08;

    @FXML
    private TextField txtField09;

    @FXML
    private TextField txtField04;

    @FXML
    private TextField txtField06;

    @FXML
    private TextArea txtField07;

    @FXML
    private TextField txtField05;

    @FXML
    private TextField txtField10;

    @FXML
    private TextField txtField11;

    @FXML
    private TextField txtField12;

    @FXML
    private AnchorPane apDetail;

    @FXML
    private Label lblStatus1;

    @FXML
    private TableColumn<?, ?> index12;

    @FXML
    private TableColumn<?, ?> index13;

    @FXML
    private TextField txtField13;

    @FXML
    private TextField txtField14;

    @FXML
    private TextField txtField15;

    @FXML
    private TextField txtField16;

    @FXML
    private TextField txtField18;

    @FXML
    private TextField txtField17;

    @FXML
    private TextField txtField19;

    @FXML
    private AnchorPane apTable;

    @FXML
    private TableView tblDetails;

  


    @FXML
    private AnchorPane apBrowse;

    @FXML
    private TextField txtField99;

    @FXML
    private TextField txtField98;

    @FXML
    private TextField txtField97;

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
