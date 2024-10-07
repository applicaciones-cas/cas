/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
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
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.model.ModelPurchaseOrder;
import org.guanzon.cas.parameters.Branch;
import org.guanzon.cas.parameters.Color;
import org.guanzon.cas.parameters.Model;
import org.guanzon.cas.purchasing.controller.PurchaseOrder;
import org.json.simple.JSONObject;
import org.junit.Assert;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PurchaseOrderConfirmationController  implements Initializable, ScreenInterface{
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
    private AnchorPane apBrowse;
    @FXML
    private TextField txtField99;
    @FXML
    private TextField txtField98;
    @FXML
    private TextField txtField97;
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
    private TextField txtField06;
    @FXML
    private TextArea txtField07;
    @FXML
    private TextField txtField09;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField11;
    @FXML
    private TextField txtField12;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField08;
    @FXML
    private AnchorPane apTransactionIssues;
    @FXML
    private Label lblStatus1;
    @FXML
    private TableView tblTransactionIssues;
    @FXML
    private TableColumn index12;
    @FXML
    private TableColumn index13;
    @FXML
    private AnchorPane apDetail;
    @FXML
    private TextField txtDetail01;
    @FXML
    private TextField txtDetail02;
    @FXML
    private TextField txtDetail03;
    @FXML
    private TextField txtDetail04;
    @FXML
    private TextField txtDetail05;
    @FXML
    private TextField txtDetail06;
    @FXML
    private TextField txtDetail07;
    @FXML
    private AnchorPane apTable;
    @FXML
    private TableView tblDetails;
    @FXML
    private TableColumn index01;
    @FXML
    private TableColumn index02;
    @FXML
    private TableColumn index03;
    @FXML
    private TableColumn index04;
    @FXML
    private TableColumn index05;
    @FXML
    private TableColumn index06;
    @FXML
    private TableColumn index07;
    @FXML
    private TableColumn index08;
    @FXML
    private TableColumn index09;
    @FXML
    private TableColumn index10;
    @FXML
    private TableColumn index11;

    /**
     * Initializes the controller class.
     */
    private ObservableList<ModelPurchaseOrder> data = FXCollections.observableArrayList();

    
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 2://dtransact
                    poJSON = oTrans.getMasterModel().setTransactionDate(SQLUtil.toDate(lsValue, "yyyy-MM-dd"));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    txtField.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getTransactionDate()));
                    break;

                case 8://Reference No
                    poJSON = oTrans.getMasterModel().setReferenceNo(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 10://Disc
                    poJSON = oTrans.getMasterModel().setDiscount(Integer.valueOf(lsValue));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 11://AddDisc Rate
                    poJSON = oTrans.getMasterModel().setAddDiscount(Integer.valueOf(lsValue));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 12://Total Order
                    poJSON = oTrans.getMasterModel().setTransactionTotal(Integer.valueOf(lsValue)); // computation
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;

            }
        } else {

            switch (lnIndex) {
                case 2:
                    txtField.setText(CommonUtils.dateFormat(oTrans.getMasterModel().getTransactionDate(), "yyyy-MM-dd"));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;

            }

        }

        txtField.selectAll();
        pnIndex = lnIndex;
    };

    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextArea txtField = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 7://Remarks
                    poJSON = oTrans.getMasterModel().setRemarks(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
            }
        } else {
            txtField.selectAll();
        }
        pnIndex = lnIndex;
    };

    final ChangeListener<? super Boolean> txtDetail_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(9, 11));
        String lsValue = txtField.getText();
        pnIndex = lnIndex;
        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 2:
                    String lsStockID = (String) oTrans.getDetailModel(pnDetailRow).getValue("sStockIDx");
                    if (lsStockID == null || lsStockID.isEmpty()) {
                        if (txtField.getText().length() > 128) {
                            ShowMessageFX.Warning("Max characters for `Descript` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                            txtField.requestFocus();
                            return;
                        }
                        poJSON = oTrans.setDetail(pnDetailRow, "sDescript", lsValue);
                        if ("error".equals((String) poJSON.get("result"))) {
                            System.err.println((String) poJSON.get("message"));
                            return;
                        }
                    }
                    loadTableDetail();
                    break;
                case 5:
                    //Orig Cost inputted by user
                    String lsNewCost = (String) oTrans.getDetailModel(pnDetailRow).getValue("nUnitPrce");
                    if (lsNewCost == null || lsNewCost.isEmpty()) {
                        if (txtField.getText().length() > 128) {
                            ShowMessageFX.Warning("Max characters for `ROQ` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                            txtField.requestFocus();
                            return;
                        }
                        poJSON = oTrans.setDetail(pnDetailRow, "nUnitPrce", lsValue);
                        if ("error".equals((String) poJSON.get("result"))) {
                            System.err.println((String) poJSON.get("message"));
                            return;
                        }
                    }

                    loadTableDetail();
                    break;
                case 7:
                    /*this must be numeric*/
                    int x = 0;
                    try {

                        x = Integer.valueOf(lsValue);
                        if (x > 999999) {
                            x = 0;

                            ShowMessageFX.Warning("Please input not greater than 999999", pxeModuleName, "");
                            txtField.requestFocus();
                        }
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();
                    }

                    poJSON = oTrans.setDetail(pnDetailRow, "nQuantity", x);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }

                    loadTableDetail();
                    break;

            }
        } else {
            txtField.selectAll();
        }

    };
    private void txtField_KeyPressed(KeyEvent event) {

        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = textField.getText();
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 97:/*Browse Supplier*/
                        poJSON = oTrans.searchSupplier("sSupplier", lsValue, lnIndex == 97);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                    case 98:/*Browse Destination*/
                        poJSON = oTrans.searchDestination("sDestinat", lsValue, lnIndex == 98);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                    case 99:/*Browse Primary*/
                        poJSON = oTrans.searchTransaction("sTransNox", lsValue, lnIndex == 99);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                    case 3:
                        /*sDestinat*/
                        poJSON = oTrans.searchMaster(5, lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                    case 4:
                        /*sSupplier*/
                        poJSON = oTrans.searchMaster(6, lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                    case 9:
                        /*sTermCode*/
                        poJSON = oTrans.searchMaster(10, lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                }
                break;
        }
        switch (event.getCode()) {
            case ENTER:
                break;
            case DOWN:
                CommonUtils.SetNextFocus(textField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(textField);
        }

        pnIndex = lnIndex;
    }
    
    
    private void loadRecord() {
        String lsActive = oTrans.getMasterModel().getTransactionStatus();

        switch (lsActive) {
            case "0":
                lblStatus.setText("OPEN");
                break;
            case "1":
                lblStatus.setText("CLOSED");
                break;
            case "2":
                lblStatus.setText("POSTED");
                break;
            case "3":
                lblStatus.setText("CANCELLED");
                break;
            default:
                lblStatus.setText("UNKNOWN");
                break;
        }

        //get supplier id and search contctp & contctno
        String lsClientID = oTrans.getMasterModel().getSupplier();
        if (!lsClientID.isEmpty()) {
            oTrans.searchMaster("sSupplier", lsClientID, pbLoaded);
        }

        psPrimary = oTrans.getMasterModel().getTransactionNo();
        txtField01.setText(psPrimary);
        txtField02.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getTransactionDate()));
        txtField03.setText(oTrans.getMasterModel().getDestinationOther());
        txtField04.setText(oTrans.getMasterModel().getSupplierName());
        txtField05.setText(oTrans.getMasterModel().getContactPerson1());
        txtField06.setText(oTrans.getMasterModel().getMobileNo());
        txtField07.setText(oTrans.getMasterModel().getRemarks());
        txtField08.setText(oTrans.getMasterModel().getReferenceNo());
        txtField09.setText(oTrans.getMasterModel().getTermName());

        try {
            txtField10.setText(String.valueOf(oTrans.getMasterModel().getDiscount()));
            txtField11.setText(String.valueOf(oTrans.getMasterModel().getAddDiscount()));
            txtField12.setText(String.valueOf(oTrans.getMasterModel().getTransactionTotal()));

        } catch (Exception e) {
        }
        loadTableDetail();
    }

    private void loadTableDetail() {
        int lnCtr;
        data.clear();

        int lnItem = oTrans.getItemCount();
        if (lnItem < 0) {
            return;
        }
        //count size
        for (lnCtr = 0; lnCtr < oTrans.getDetailModel(); lnCtr++) {
            System.out.println((String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx"));

        }

//        setDetail( "sStockIDx", (String) loInventory.getMaster("sDescript"));
        double lnTotalTransaction = 0;
        for (lnCtr = 0; lnCtr <= oTrans.getItemCount() - 1; lnCtr++) {
            String lsStockIDx = (String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx");

            Inventory loInventory;
            Color loColor;
            Model loMdl;
//            Model_Variant loMdlVrnt;
            if (lsStockIDx != null && !lsStockIDx.equals("")) {

                loInventory = oTrans.GetInventory(lsStockIDx, true);
                //for the meantime try-catch for Model
                loMdl = oTrans.GetModel((String) loInventory.getMaster("sModelIDx"), true);

                loColor = oTrans.GetColor((String) loInventory.getMaster("sColorIDx"), true);
                data.add(new ModelPurchaseOrder(String.valueOf(lnCtr + 1),
                        (String) loInventory.getMaster("sBarCodex"),
                        (String) oTrans.getDetailModel(lnCtr).getDescription(),
                        (String) loInventory.getMaster("xBrandNme"),
                        (String)loMdl.getModel().getModelCode(),
                        (String)loMdl.getModel().getDescription(),
                        // loMdl.getModel().getModelCode(),
                        // loMdl.getModel().getModelName(),
                        "",
                        //getYearModel method in Model is missing
//                        String.valueOf(loMdl.getModel().getYearModel()),
                        "",
                        (String)loColor.getModel().getDescription(),
                        (String)loInventory.getMaster("nUnitPrce").toString(),
                        (String)oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString()
                ));

                try {
                    if (oTrans.getDetailModel(lnCtr).getQuantity() != 0) {
                        lnTotalTransaction += Double.parseDouble((oTrans.getDetailModel(lnCtr).getUnitPrice().toString())) * Double.parseDouble(String.valueOf(oTrans.getDetailModel(lnCtr).getQuantity()));
                    } else {
                        lnTotalTransaction += Double.parseDouble((oTrans.getDetailModel(lnCtr).getUnitPrice().toString()));
                        System.out.println(lnTotalTransaction);
                    }
                } catch (Exception e) {
                }

            } else {
                data.add(new ModelPurchaseOrder(String.valueOf(lnCtr + 1),
                        "",
                        (String) oTrans.getDetailModel(lnCtr).getValue("sDescript"),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString()));

            }
        }
        txtField12.setText(String.valueOf(lnTotalTransaction));
        lnTotalTransaction = 0;

        /*FOCUS ON FIRST ROW*/
        if (pnDetailRow < 0 || pnDetailRow >= data.size()) {
            if (!data.isEmpty()) {
                /* FOCUS ON FIRST ROW */
                tblDetails.getSelectionModel().select(0);
                tblDetails.getFocusModel().focus(0);
                pnDetailRow = tblDetails.getSelectionModel().getSelectedIndex();
            }
            setSelectedDetail(); //textfield data
        } else {
            /* FOCUS ON THE ROW THAT pnRowDetail POINTS TO */
            tblDetails.getSelectionModel().select(pnDetailRow);
            tblDetails.getFocusModel().focus(pnDetailRow);
            setSelectedDetail();
        }
        initDetailsGrid();
    }
    
     private void setSelectedDetail() {
        txtDetail01.setText((String) data.get(pnDetailRow).getIndex02());
        txtDetail02.setText((String) data.get(pnDetailRow).getIndex03());
        txtDetail03.setText((String) data.get(pnDetailRow).getIndex10());
        txtDetail04.setText(Integer.toString(oTrans.getDetailModel(pnDetailRow).getQtyOnHand()));
        txtDetail05.setText((String) data.get(pnDetailRow).getIndex10());
        try {
            txtDetail06.setText(Integer.toString(oTrans.getDetailModel(pnDetailRow).getRecOrder()));
        } catch (Exception e) {
        }
        txtDetail07.setText(Integer.toString(oTrans.getDetailModel(pnDetailRow).getQuantity()));

    }

    public void initDetailsGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index07.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        index08.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index09.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index10.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index11.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        index01.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index08"));
        index09.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index09"));
        index10.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index10"));
        index11.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrder, String>("index11"));

        tblDetails.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblDetails.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblDetails.setItems(data);

    }

    private void txtDetail_KeyPressed(KeyEvent event) {

        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(9, 11));
        String lsValue = textField.getText();
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 1:
                        poJSON = oTrans.searchDetail(pnDetailRow, 3, lsValue, lnIndex == 1);
//                        System.out.println("poJson Result = " + poJSON.toJSONString());
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }

                        break;
                    case 2:
                        /* Barcode & Description */
                        poJSON = oTrans.searchDetail(pnDetailRow, 3, lsValue, lnIndex == 1);
//                         oTrans.searchDetail("sDescription", lnLastRow, txtField.getText(), false)
//                        System.out.println("poJson Result = " + poJSON.toJSONString());
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }

//                            oTrans.getDetailModel(pnDetailRow);
//                        if (oTrans.getItemCount() > 0 ) {
//                            int lnLastRow = oTrans.getItemCount() - 1;
//                            oTrans.searchDetail(lnLastRow,3, "", false);
//                        }
                        break;

                }
                loadRecord();
                loadTableDetail();
                break;
        }
        switch (event.getCode()) {
            case ENTER:
                break;
            case DOWN:
                CommonUtils.SetNextFocus(textField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(textField);
        }

        pnIndex = lnIndex;
    }
    

    
        private void initTextFields() {
        /*textFields FOCUSED PROPERTY*/
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtArea_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);

        txtField99.focusedProperty().addListener(txtField_Focus);
        txtField98.focusedProperty().addListener(txtField_Focus);
        txtField97.focusedProperty().addListener(txtField_Focus);

        txtDetail01.focusedProperty().addListener(txtDetail_Focus);
        txtDetail02.focusedProperty().addListener(txtDetail_Focus);
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail07.focusedProperty().addListener(txtDetail_Focus);

//      txtField01.setOnKeyPressed(this::txtField_KeyPressed); // TransactionNo
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);

        txtField99.setOnKeyPressed(this::txtField_KeyPressed);
        txtField98.setOnKeyPressed(this::txtField_KeyPressed);
        txtField97.setOnKeyPressed(this::txtField_KeyPressed);

        txtDetail01.setOnKeyPressed(this::txtDetail_KeyPressed);//barcode
        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new PurchaseOrder(oApp, false);
        oTrans.setTransactionStatus("12340");

        initTextFields();
        initDetailsGrid();
        clearFields();

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        pbLoaded = true;
    }
    
        private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

// Manage visibility and managed state of buttons
        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);


// Manage visibility and managed state of other buttons
        btnBrowse.setVisible(!lbShow);
        btnPrint.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnBrowse.setManaged(!lbShow);
        btnPrint.setManaged(!lbShow);
        btnClose.setManaged(!lbShow);

    }
    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        txtField04.clear();
        txtField05.clear();
        txtField06.clear();
        txtField07.clear();
        txtField08.clear();
        txtField09.clear();
        txtField10.clear();
        txtField11.clear();
        txtField12.clear();

        txtField99.clear();
        txtField98.clear();
        txtField97.clear();

        txtDetail01.clear();
        txtDetail02.clear();
        txtDetail03.clear();
        txtDetail04.clear();
        txtDetail05.clear();
        txtDetail06.clear();
        txtDetail07.clear();

        psPrimary = "";
        lblStatus.setText("UNKNOWN");

        pnDetailRow = -1;
        pnIndex = -1;

        data.clear();

    }

 @FXML
    void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {
            case "btnBrowse":
                if (pnIndex < 98) {
                    pnIndex = 99;
                }
                poJSON = oTrans.searchTransaction("sTransNox", "", pnIndex == 99);
                pnEditMode = EditMode.READY;
                //start
                if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                    ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                    txtField01.requestFocus();
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                } else {
                    loadRecord();
                }
                break;

            case "btnNew":
                oTrans.setSavingStatus(true);

                poJSON = oTrans.newTransaction();
                Branch loCompanyID = oTrans.GetBranch(oApp.getBranchCode());

                oTrans.setMaster("sCompnyID", loCompanyID.getModel().getCompanyID());
                oTrans.getMasterModel().setDiscount(0.00);
                oTrans.getMasterModel().setAddDiscount(0.00);
                oTrans.getMasterModel().setTransactionTotal(0.00);
                loadRecord();
                pnEditMode = oTrans.getMasterModel().getEditMode();
                oTrans.setTransactionStatus("12340");
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }

                break;
            case "btnUpdate":
                oTrans.setSavingStatus(true);
                poJSON = oTrans.updateTransaction();
                pnEditMode = oTrans.getMasterModel().getEditMode();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                break;
            case "btnPrint":
                poJSON = oTrans.printRecord();
                if ("error".equals((String) poJSON.get("result"))) {
                    Assert.fail((String) poJSON.get("message"));
                }

                break;
            case "btnClose":
                unloadForm appUnload = new unloadForm();
                if (ShowMessageFX.OkayCancel(null, "Close Tab", "Are you sure you want to close this Tab?") == true) {
                    appUnload.unloadForm(MainAnchorPane, oApp, pxeModuleName);

                } else {
                    return;
                }

                break;
            case "btnFindSource":
                if (pnIndex > 3 || pnIndex < 1) {
                    pnIndex = 1;
                }
                switch (pnIndex) {
                    case 2:
                        /* Barcode & Description */
                        poJSON = oTrans.searchDetail(pnDetailRow, 3, (pnIndex == 1) ? txtDetail01.getText() : "", pnIndex == 1);
//                        System.out.println("poJson Result = " + poJSON.toJSONString());
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }

                        loadTableDetail();
                        break;
                }
                break;

            case "btnSearch":
                if (pnIndex > 3 || pnIndex < 1) {
                    pnIndex = 1;
                }
                switch (pnIndex) {
                    case 1:
                    case 2:
                        /* Barcode & Description */
                        poJSON = oTrans.searchDetail(pnDetailRow, 3, (pnIndex == 1) ? txtDetail01.getText() : "", pnIndex == 1);
//                        System.out.println("poJson Result = " + poJSON.toJSONString());
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }

                        loadTableDetail();
                        break;
                }
                break;
            case "btnSave":

                poJSON = oTrans.getMasterModel().setModifiedBy(oApp.getUserID());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));

                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                poJSON = oTrans.getMasterModel().setModifiedDate(oApp.getServerDate());
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;
                }
                poJSON = oTrans.saveTransaction();

                pnEditMode = oTrans.getMasterModel().getEditMode();
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    pnEditMode = EditMode.UNKNOWN;
                    return;

                } else {
                    oTrans = new PurchaseOrder(oApp, true);
                    pbLoaded = true;
                    oTrans.setTransactionStatus("12340");
                    pnEditMode = EditMode.UNKNOWN;
                    clearFields();
                    ShowMessageFX.Information(null, pxeModuleName, "Transaction successful Saved!");
                }
                break;
            case "btnAddItem":
                // Clearing fields
                oTrans.setSavingStatus(true);
                apMaster.setDisable(true);
                if (oTrans.getDetailModel(pnDetailRow).getQuantity() > 0
                        && !oTrans.getDetailModel(pnDetailRow).getStockID().isEmpty()) {
                    poJSON = oTrans.AddModelDetail();
                }
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    return;
                }
                pnDetailRow = oTrans.getItemCount() - 1;
                loadTableDetail();
                tblDetails.getSelectionModel().select(pnDetailRow + 1);
                break;
            case "btnRemoveItem":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove this item?") == true) {
                    oTrans.RemoveModelDetail(pnDetailRow);
                    pnDetailRow = oTrans.getItemCount() - 1;
                    loadTableDetail();
                    txtField04.requestFocus();
                    oTrans.setSavingStatus(false);
                }
                break;
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
                    oTrans = new PurchaseOrder(oApp, true);
                    oTrans.setTransactionStatus("12340");
                    pbLoaded = true;
                    pnEditMode = EditMode.UNKNOWN;
                    clearFields();
                    break;
                } else {
                    return;
                }
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        initButton(pnEditMode);

    }

    @FXML
    void tblDetails_Clicked(MouseEvent event) {
        pnDetailRow = tblDetails.getSelectionModel().getSelectedIndex();
        if (pnDetailRow >= 0) {
            setSelectedDetail();
        }

    }

    @Override
    public void setGRider(GRider foValue) {
     oApp = foValue;
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
