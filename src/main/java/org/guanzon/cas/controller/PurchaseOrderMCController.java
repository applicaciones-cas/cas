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
public class PurchaseOrderMCController implements Initializable, ScreenInterface {

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
    private TextField txtDetail01, txtDetail02, txtDetail03, txtDetail04, txtDetail05, txtDetail06, txtDetail07;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView tblDetails;

    @FXML
    private TableColumn index01, index02, index03, index04, index05, index06, index07, index08, index09, index10, index11;
    private ObservableList<ModelPurchaseOrder> data = FXCollections.observableArrayList();

    @FXML
    void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {
            case "btnBrowse":
                if (pnIndex < 98) {
                    pnIndex = 99;
                }
                poJSON = oTrans.searchTransaction("sTransNox", txtField01.getText(), pnIndex == 99);
                pnEditMode = EditMode.READY;
                 
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
                poJSON = oTrans.newTransaction();
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
                break;
            case "btnRemoveItem":
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

        psPrimary = oTrans.getMasterModel().getTransactionNo();
        txtField01.setText(psPrimary);
        txtField02.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getTransactionDate()));
        txtField03.setText(oTrans.getMasterModel().getDestinationOther());
        txtField04.setText(oTrans.getMasterModel().getSupplierName());
        txtField05.setText(oTrans.getMasterModel().getContactPerson1());
        txtField06.setText(oTrans.getMasterModel().getMobileNo());
//        txtField07.setText(oTrans.getMasterModel().getRemarks());
//        txtField08.setText(oTrans.getMasterModel().getReferenceNo());
        txtField09.setText(oTrans.getMasterModel().getTermName());
        try{
        txtField10.setText(String.valueOf(oTrans.getMasterModel().getDiscount()));
//        txtField11.setText(oTrans.getMasterModel().get);
        txtField12.setText(String.valueOf(oTrans.getMasterModel().getTransactionTotal()));
        }catch(Exception e){}
        loadTableDetail();
    }

    private void loadTableDetail() {
        int lnCtr;
        data.clear();

        int lnItem = oTrans.getItemCount();
        if (lnItem < 0) {
            return;
        }
        Inventory loInventory;
        for (lnCtr = 0; lnCtr <= lnItem - 1; lnCtr++) {
            String lsStockIDx = (String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx");

            if (lsStockIDx != null && !lsStockIDx.equals("")) {
                loInventory = oTrans.GetInventory(lsStockIDx, true);

                data.add(new ModelPurchaseOrder(String.valueOf(lnCtr + 1),
                        (String) loInventory.getMaster("sBarCodex"),
                        (String) loInventory.getMaster("sDescript"),
                        (String) loInventory.getMaster("xBrandNme"),
                        (String) loInventory.getMaster("sModelID"),
                        (String) loInventory.getMaster("sModelID"),
                        (String) loInventory.getMaster("sModelID"),
                        "year model",
                        (String) loInventory.getMaster("xColorNme"),
                        "",""
//                        (String) loInventory.getMaster("nUnitPrce"),
//                        (String) loInventory.getMaster("nUnitPrce")
//                        oTrans.getDetailModel(lnCtr).getValue("nOrderxxx").toString()
                ));

                //display fetched detail on console
//                System.out.println("\nNo == " + String.valueOf(lnCtr + 1));
//                System.out.println("\nsBarCodex == " + (String) loInventory.getMaster("sBarCodex"));
//                System.out.println("\nsDescript == " + (String) loInventory.getMaster("sDescript"));
//                System.out.println("\nxModelNme == " + (String) loInventory.getMaster("sBrandxxx"));
//                System.out.println("\nxColorNme == " + (String) loInventory.getMaster("xModelCde"));
//                System.out.println("\nxMeasurNm == " + (String) loInventory.getMaster("xModelNme"));
//                System.out.println("\nxMeasurNm == " + (String) loInventory.getMaster("xModelVnt"));
//                System.out.println("\nxMeasurNm == " + (String) loInventory.getMaster("xYrModelx"));
//                System.out.println("\nxMeasurNm == " + (String) loInventory.getMaster("sColorxxx"));
//                System.out.println("\nxMeasurNm == " + (String) loInventory.getMaster("nCostxxxx"));
//                System.out.println("\nQuantity == " + oTrans.getDetailModel(lnCtr).getValue("nOrderxxx").toString());
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
    
    
    private void setSelectedDetail() {
        txtDetail01.setText((String) data.get(pnDetailRow).getIndex02());
        txtDetail02.setText((String) data.get(pnDetailRow).getIndex03());
        txtDetail03.setText((String) data.get(pnDetailRow).getIndex03());
        txtDetail04.setText((String) data.get(pnDetailRow).getIndex04());
        txtDetail05.setText((String) data.get(pnDetailRow).getIndex05());
        txtDetail06.setText((String) data.get(pnDetailRow).getIndex06());
        txtDetail07.setText((String) data.get(pnDetailRow).getIndex07());

    }

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
//                case 4://referno
//                    poJSON = oTrans.getMasterModel().setReferenceNo(lsValue);
//                    if ("error".equals((String) poJSON.get("result"))) {
//                        System.err.println((String) poJSON.get("message"));
//                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
//                        return;
//                    }
//                    break;
//                case 6://expecteddate
//                    poJSON = oTrans.getMasterModel().setExpectedPurchaseDate(SQLUtil.toDate(lsValue, "yyyy-MM-dd"));
//                    if ("error".equals((String) poJSON.get("result"))) {
//                        System.err.println((String) poJSON.get("message"));
//                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
//                        return;
//                    }
//                    txtField.setText(CommonUtils.xsDateLong(oTrans.getMasterModel().getExpectedPurchaseDate()));
//                    break;

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
//                case 6:
//                    txtField.setText(CommonUtils.dateFormat(oTrans.getMasterModel().getExpectedPurchaseDate(), "yyyy-MM-dd"));
//                    if ("error".equals((String) poJSON.get("result"))) {
//                        System.err.println((String) poJSON.get("message"));
//                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
//                        return;
//                    }
//                    break;
            }

        }

        txtField.selectAll();
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
                        
//                        poJSON = oTrans.getDetailModel(pnDetailRow).setQtyOnHand((int)1);
//                        if ("error".equals((String) poJSON.get("result"))) {
//                            System.err.println((String) poJSON.get("message"));
//                            return;
//                        }
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

    private void initTextFields() {
        /*textFields FOCUSED PROPERTY*/
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
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
        
        
//        txtField01.setOnKeyPressed(this::txtField_KeyPressed); // TransactionNo
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);

        txtField99.setOnKeyPressed(this::txtField_KeyPressed);
        txtField98.setOnKeyPressed(this::txtField_KeyPressed);
        txtField97.setOnKeyPressed(this::txtField_KeyPressed);

        txtDetail01.setOnKeyPressed(this::txtDetail_KeyPressed);//barcode
        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
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
        private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        btnCancel.setVisible(lbShow);

        btnSearch.setManaged(lbShow);
        btnSave.setManaged(lbShow);
        btnCancel.setManaged(lbShow);
        btnBrowse.setManaged(!lbShow);
        btnNew.setManaged(!lbShow);
        btnUpdate.setManaged(!lbShow);
        btnClose.setManaged(!lbShow);
        btnFindSource.setManaged(!lbShow);
        btnRemoveItem.setManaged(!lbShow);
        btnPrint.setManaged(!lbShow);
        btnAddItem.setManaged(!lbShow);

        btnBrowse.setManaged(!lbShow);
        btnNew.setManaged(!lbShow);
        btnUpdate.setManaged(!lbShow);
        btnClose.setManaged(!lbShow);

        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnFindSource.setVisible(!lbShow);
        btnRemoveItem.setVisible(!lbShow);
        btnPrint.setVisible(!lbShow);
        btnAddItem.setVisible(!lbShow);

        apBrowse.setDisable(lbShow);
        apMaster.setDisable(!lbShow);
        apDetail.setDisable(!lbShow);
        apTable.setDisable(!lbShow);

    }
      private void txtField_KeyPressed(KeyEvent event) {

        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = textField.getText();
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 97:/*Browse Destination*/
                        poJSON = oTrans.searchSupplier("sDestinat", lsValue, lnIndex == 97);
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
//                    case 5:/*sCategCd*/
//                        poJSON = oTrans.searchMaster(9, lsValue, false);
//                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
//
//                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
//                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
//                            txtField01.requestFocus();
//                        } else {
//                            loadRecord();
//                        }
//                        break;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        oTrans = new PurchaseOrder(oApp, false);
        oTrans.setTransactionStatus("12340");

        initTextFields();
        initDetailsGrid();
        clearFields();

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        pbLoaded = true;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
