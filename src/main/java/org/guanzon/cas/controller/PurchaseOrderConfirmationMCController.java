/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.controller.ScreenInterface;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.model.ImageModel;
import org.guanzon.cas.model.ModelPurchaseOrderMC;
import org.guanzon.cas.model.ModelPurchaseOrder2;
import org.guanzon.cas.parameters.Branch;
import org.guanzon.cas.parameters.Brand;
import org.guanzon.cas.parameters.Color;
import org.guanzon.cas.parameters.Model;
import org.guanzon.cas.parameters.Model_Variant;
import org.guanzon.cas.purchasing.controller.PurchaseOrder;
import org.json.simple.JSONObject;
import org.junit.Assert;

public class PurchaseOrderConfirmationMCController implements Initializable, ScreenInterface {

    private final String pxeModuleName = "Purchase Order Confirmation MC";
    private GRider oApp;
    private PurchaseOrder oTrans;
    private JSONObject poJSON;
    private int pnEditMode;

    private FileChooser fileChooser;
    private String psPrimary = "";
    private boolean pbLoaded = false;
    private int pnIndex;
    private int pnDetailRow;

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
    private Button btnClose;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnAttachment;

    @FXML
    private TableView tblAttachments;

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
    private TextField txtField04;

    @FXML
    private TextField txtField05;

    @FXML
    private TextField txtField06;

    @FXML
    private TextArea txtField07;

    @FXML
    private TextField txtField08;

    @FXML
    private TextField txtField09;

    @FXML
    private TextField txtField10;

    @FXML
    private TextField txtField11;

    @FXML
    private TextField txtField12;

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
    private TextField txtDetail13;

    @FXML
    private TextField txtDetail08;

    @FXML
    private TextField txtDetail09;

    @FXML
    private TextField txtDetail07;

    @FXML
    private TextField txtDetail10;

    @FXML
    private TextField txtDetail11;

    @FXML
    private TextField txtDetail12;

    @FXML
    private TextField txtDetail14;

    @FXML
    private TextField txtDetail15;

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

    @FXML
    private TableColumn index12;

    @FXML
    private TableColumn index13;

    @FXML
    private AnchorPane apBrowse;

    @FXML
    private TextField txtField99;

    @FXML
    private TextField txtField98;

    @FXML
    private TextField txtField97;

    @FXML
    private TextField txtField96;

    @FXML
    private ImageView imageView;

    private ObservableList<ModelPurchaseOrderMC> data = FXCollections.observableArrayList();
    private final ObservableList<ImageModel> img_data = FXCollections.observableArrayList();

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
        txtField96.clear();

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

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    private Stage getStage() {
        return (Stage) txtField01.getScene().getWindow();
    }

    @FXML
    void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {
            case "btnAttachment":
                try {
                // Open file chooser dialog
                fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Image");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
                );
                java.io.File selectedFile = fileChooser.showOpenDialog((Stage) txtField02.getScene().getWindow());

                if (selectedFile != null) {
                    // Read image from the selected file
                    Path imgPath = selectedFile.toPath();
                    Image loimage = new Image(Files.newInputStream(imgPath));
                    imageView.setImage(loimage);

                    String imgPath2 = selectedFile.toURI().toString();
                    img_data.add(new ImageModel(String.valueOf(img_data.size()), imgPath2));
                    initDetailsGrid2();
//                        try (InputStream imgStream = Files.newInputStream(imgPath)) {
//                            BufferedImage image = ImageIO.read(imgStream);
//
//                            // Define the directory path
//                            Path directory = Paths.get(imagePath, (String) "sample");
//                            if (!Files.exists(directory)) {
//                                Files.createDirectories(directory);
//                            }
//
//                            // Define the destination file path
//                            Path imgFilePath = directory.resolve(selectedFile.getName());
//                            try (OutputStream outputStream = Files.newOutputStream(imgFilePath)) {
//                                ImageIO.write(image, "jpg", outputStream);
//                            }
//
//                            // Generate image path for external reference
//                            String imgPathString = "https://restgk.guanzongroup.com.ph/images/mp/uploads/listing/"
//                                    + oTrans.getMasterModel() + "/" + selectedFile.getName();
//
//                            // Add the image to the transaction
//                            if (oTrans.addImage(imgPathString)) {
//                                pnEditMode = oTrans.getEditMode();
//                                img_data.add(new ImageModel(String.valueOf(img_data.size()), imgFilePath.toUri().toString()));
//                                generateImages();
//                            } else {
//                                System.out.println("Failed to add image to transaction.");
//                            }
//                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
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
            case "btnConfirm":
                if (!psPrimary.isEmpty()) {
                    if (btnConfirm.getText().equals("Activate")) {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Activate this Parameter?") == true) {
                            poJSON = oTrans.closeTransaction(oTrans.getMasterModel().getTransactionNo());
                            if ("error".equals((String) poJSON.get("result"))) {
                                System.err.println((String) poJSON.get("message"));

                                return;
                            } else {
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                initButton(pnEditMode);
                                oTrans = new PurchaseOrder(oApp, false);
                                oTrans.setTransactionStatus("12340");
                                pbLoaded = true;

                            }
                        } else {
                            return;
                        }
                    } else {
                        if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Deactivate this Parameter?") == true) {
                            poJSON = oTrans.cancelTransaction(psPrimary);
                            if ("error".equals((String) poJSON.get("result"))) {
                                System.err.println((String) poJSON.get("message"));
                                ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                                return;
                            } else {
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                initButton(pnEditMode);
                                oTrans = new PurchaseOrder(oApp, false);
                                oTrans.setTransactionStatus("12340");
                                pbLoaded = true;
                            }
                        } else {
                            return;
                        }
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to confirm!");
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

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnCancel.setVisible(lbShow);
        btnCancel.setManaged(lbShow);

        btnBrowse.setVisible(!lbShow);
        btnPrint.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnBrowse.setManaged(!lbShow);
        btnPrint.setManaged(!lbShow);
        btnClose.setManaged(!lbShow);

        apBrowse.setDisable(lbShow);
        apMaster.setDisable(!lbShow);
        apDetail.setDisable(!lbShow);
//        apTable.setDisable(!lbShow);

        if (Integer.parseInt(oTrans.getMasterModel().getTransactionStatus()) != 1) {
            btnConfirm.setDisable(false);
        } else {
            btnConfirm.setDisable(true);
        }
        oTrans.setTransType("MC");

    }

    @FXML
    void tblDetails_Clicked(MouseEvent event) {
        pnDetailRow = tblDetails.getSelectionModel().getSelectedIndex();
        if (pnDetailRow >= 0) {
            setSelectedDetail();
        }
    }

    private void setSelectedDetail() {
        Model_Inv_Stock_Request_Detail loModel_Inv_Stock_Request_Detail;
        loModel_Inv_Stock_Request_Detail = oTrans.GetModel_Inv_Stock_Request_Detail(oTrans.getDetailModel(pnDetailRow).getStockID());

        txtDetail01.setText((String) data.get(pnDetailRow).getIndex02());
        txtDetail02.setText((String) data.get(pnDetailRow).getIndex03());
        txtDetail03.setText((String) data.get(pnDetailRow).getIndex04());
        txtDetail04.setText(oTrans.getDetailModel(pnDetailRow).getDescription());
        txtDetail05.setText((String) data.get(pnDetailRow).getIndex08());
        txtDetail06.setText(Integer.toString(oTrans.getDetailModel(pnDetailRow).getQuantity()));

        Inventory loInventory = oTrans.GetInventory((String) oTrans.getDetailModel(pnDetailRow).getValue("sStockIDx"), true);
        InvMaster loInv_Master = oTrans.GetInvMaster((String) loInventory.getMaster("sStockIDx"), true);

        TextField[] textFields = {txtDetail07, txtDetail08, txtDetail10, txtDetail11};
        String[] keys = {"nQtyOnHnd", "nMaxLevel", "nResvOrdr", "nBackOrdr"};

        for (int i = 0; i < textFields.length; i++) {
            try {
                textFields[i].setText((String) loInv_Master.getMaster(keys[i]));
            } catch (Exception e) {
                textFields[i].setText("0");
            }
        }
        try {
            txtDetail09.setText((String) loInv_Master.getMaster("cClassify"));
        } catch (Exception e) {
            txtDetail09.setText("F");
        }
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

        index01.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index08"));
        index09.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index09"));
        index10.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index10"));
        index11.setCellValueFactory(new PropertyValueFactory<ModelPurchaseOrderMC, String>("index11"));

        tblDetails.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblDetails.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblDetails.setItems(data);

    }

    private void loadTableDetail() {
        poJSON.put("result", "success");
        poJSON.put("message", "''");

        int lnCtr;
        data.clear();

        int lnItem = oTrans.getItemCount();
        if (lnItem < 0) {
            return;
        }
        double lnTotalTransaction = 0;
        double ln1 = 0;
        //count size
        for (lnCtr = 0; lnCtr < oTrans.getItemCount() - 1; lnCtr++) {
            System.out.println((String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx"));
        }

        for (lnCtr = 0; lnCtr <= oTrans.getItemCount() - 1; lnCtr++) {
            String lsStockIDx = (String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx");

            Inventory loInventory;
            Color loColor;
            Brand loBrand;
            Model loMdl;
            Model_Variant loMdlVrnt;
            String loInventory2 = "";
            if (lsStockIDx != null && !lsStockIDx.equals("")) {
;
                loInventory = oTrans.GetInventory((String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx"), true);
                
                oTrans.setBrandID((String) loInventory.getMaster("sBrandIDx"));
                loBrand = oTrans.GetBrand(oTrans.getBrandID(), true);
                
                loMdl = oTrans.GetModel((String) loInventory.getMaster("sModelIDx"), true);
                loMdlVrnt = oTrans.GetModel_Variant((String) loMdl.getModel().getVariantID(), true);
//                loColor = oTrans.GetColor((String) loInventory.getMaster("sColorIDx"), true);
                ln1 = Double.parseDouble((oTrans.getDetailModel(lnCtr).getValue("nUnitPrce")).toString()) * Double.parseDouble(oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString());

                try {
                    if (oTrans.getDetailModel(lnCtr).getQuantity() != 0) {
                        lnTotalTransaction += Double.parseDouble((oTrans.getDetailModel(lnCtr).getUnitPrice().toString())) * Double.parseDouble(String.valueOf(oTrans.getDetailModel(lnCtr).getQuantity()));
                    } else {
                        System.out.println(lnTotalTransaction);
                    }
                } catch (Exception e) {
                }

                String lsMaxLevel = "0";
                try {
                    lsMaxLevel = (String) loInventory.getMaster("nMaxLevel");
                } catch (Exception e) {
                }

                if (loInventory.getMaster("xBrandNme").toString().length() == 0) {
                    if (pnEditMode == EditMode.ADDNEW) {
                        loInventory2 = (String) loBrand.getMaster("sDescript");
                    }
                } else {
                    loInventory2 = (String) loInventory.getMaster("xBrandNme");
                }
                data.add(new ModelPurchaseOrderMC(String.valueOf(lnCtr + 1),
                        loInventory2,
                        (String) loInventory.getMaster("xModelNme"),
                        (String) loMdl.getModel().getModelCode(),
                        (String) loInventory.getMaster("xColorNme"),
                        lsMaxLevel,
                        loMdlVrnt.getModel().getVariantName(),
                        oTrans.getDetailModel(lnCtr).getValue("nRecOrder").toString(),
                        oTrans.getDetailModel(lnCtr).getValue("nUnitPrce").toString(),
                        (String) oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString(),
                        String.valueOf(ln1)
                ));

                System.out.println("THIS IS invmaster " + (String) loInventory.getMaster("sStockIDx"));
                InvMaster loInv_Master = oTrans.GetInvMaster((String) loInventory.getMaster("sStockIDx"), true);

                TextField[] textFields = {txtDetail07, txtDetail08, txtDetail10, txtDetail11};
                String[] keys = {"nQtyOnHnd", "nMaxLevel", "nResvOrdr", "nBackOrdr"};

                for (int i = 0; i < textFields.length; i++) {
                    try {
                        textFields[i].setText(String.valueOf(loInv_Master.getMaster(keys[i])) );
                    } catch (Exception e) {
                        textFields[i].setText("0");
                    }
                }
                try {
                    txtDetail09.setText((String) loInv_Master.getMaster("cClassify"));
                } catch (Exception e) {
                    txtDetail09.setText("F");
                }
//                 

            } else {
//                loBrand = oTrans.GetBrand(oTrans.getBrandID(), true);
//                if (pnEditMode == EditMode.ADDNEW) {
//                    loInventory2 = (String) loBrand.getMaster("sDescript");
//                }
                data.add(new ModelPurchaseOrderMC(String.valueOf(lnCtr + 1),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        oTrans.getDetailModel(lnCtr).getValue("nUnitPrce").toString(),
                        (String) oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString(),
                        "0.00"
                ));
            }
        }

        txtField12.setText(String.format("%.2f", lnTotalTransaction));
        oTrans.getMasterModel().setTransactionTotal(Double.valueOf(String.format("%.2f", lnTotalTransaction)));
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
        oTrans.setRowSelect(oTrans.getItemCount() - 1);
        initDetailsGrid();
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
        System.out.println(event.getCode());
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {

                    case 96:
                        /* Browse Company */
                        poJSON = oTrans.searchTransaction("sCompnyID", lsValue, lnIndex == 96);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                    case 97:/*Browse Supplier*/
                        poJSON = oTrans.searchTransaction("sDestinat", lsValue, lnIndex == 97);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;
                    case 98:/*Browse Destination*/
                        poJSON = oTrans.searchTransaction("sSupplier", lsValue, lnIndex == 98);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {

                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                    case 99:/*Browse Primary*/
                        System.out.println("text");
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
        
        txtField02.setText(CommonUtils.dateFormat(oTrans.getMasterModel().getTransactionDate(), "MM-dd-yyyy"));
        txtField03.setText(oTrans.getMasterModel().getCompanyName());
       txtField04.setText(oTrans.getMasterModel().getDestinationOther());
        txtField05.setText(oTrans.getMasterModel().getSupplierName());
        txtField06.setText(oTrans.getMasterModel().getContactPerson1());
        txtField07.setText(oTrans.getMasterModel().getRemarks());
        txtField08.setText(oTrans.getMasterModel().getTermName());
        txtField09.setText(oTrans.getMasterModel().getReferenceNo());
        try {
            txtField10.setText(String.valueOf(oTrans.getMasterModel().getDiscount()));
            txtField11.setText(String.valueOf(oTrans.getMasterModel().getAddDiscount()));
            txtField12.setText(String.valueOf(oTrans.getMasterModel().getTransactionTotal()));

        } catch (Exception e) {
        }
        loadTableDetail();
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
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }

                        break;
                    case 2:
                        /* Barcode & Description */
                        poJSON = oTrans.searchDetail(pnDetailRow, 3, lsValue, lnIndex == 1);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }
                        break;
                }
                loadRecord();
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
        txtField96.focusedProperty().addListener(txtField_Focus);

        txtDetail01.focusedProperty().addListener(txtDetail_Focus);
        txtDetail02.focusedProperty().addListener(txtDetail_Focus);
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail07.focusedProperty().addListener(txtDetail_Focus);

        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);

        txtField99.setOnKeyPressed(this::txtField_KeyPressed);
        txtField98.setOnKeyPressed(this::txtField_KeyPressed);
        txtField97.setOnKeyPressed(this::txtField_KeyPressed);
        txtField96.setOnKeyPressed(this::txtField_KeyPressed);

        txtDetail01.setOnKeyPressed(this::txtDetail_KeyPressed);//barcode
        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
    }

    public void initDetailsGrid2() {
        /*FOCUS ON FIRST ROW*/
        if (pnDetailRow < 0 || pnDetailRow >= img_data.size()) {
            if (!img_data.isEmpty()) {
                /* FOCUS ON FIRST ROW */
                tblAttachments.getSelectionModel().select(0);
                tblAttachments.getFocusModel().focus(0);
                pnDetailRow = tblAttachments.getSelectionModel().getSelectedIndex();
            }
//            setSelectedDetail(); //textfield data
        } else {
            /* FOCUS ON THE ROW THAT pnRowDetail POINTS TO */
            tblAttachments.getSelectionModel().select(pnDetailRow);
            tblAttachments.getFocusModel().focus(pnDetailRow);
//            setSelectedDetail();
        }

        index12.setStyle("-fx-alignment: CENTER;-fx-padding: 0 0 0 5;");
        index13.setStyle("-fx-alignment: CENTER;-fx-padding: 0 0 0 5;");

        index12.setCellValueFactory(new PropertyValueFactory<ImageModel, String>("index12"));
        index13.setCellValueFactory(new PropertyValueFactory<ImageModel, String>("index13"));

        tblAttachments.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblAttachments.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblAttachments.setItems(img_data);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oTrans = new PurchaseOrder(oApp, false);
        oTrans.setTransactionStatus("12340");

        initTextFields();
        initDetailsGrid();
//        initDetailsGrid2();
        clearFields();

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        pbLoaded = true;

    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

}
