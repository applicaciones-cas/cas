package org.guanzon.cas.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.guanzon.appdriver.agent.ShowMessageFX;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.controller.ScreenInterface;
import org.guanzon.cas.controller.unloadForm;
import org.guanzon.cas.inventory.base.Inventory;
import org.guanzon.cas.inventory.base.PO_Quotation;
import org.guanzon.cas.inventory.models.Model_Inv_Stock_Request_Detail;
import org.guanzon.cas.model.ModelPurchaseOrderMC;
import org.guanzon.cas.parameters.Branch;
import org.guanzon.cas.parameters.Color;
import org.guanzon.cas.parameters.Model;
import org.guanzon.cas.parameters.Model_Variant;
import org.guanzon.cas.purchasing.controller.PurchaseOrder;
import org.guanzon.cas.validators.ValidatorFactory;
import org.guanzon.cas.validators.purchaseorder.Validator_PurchaseOrder_Master;
import org.json.simple.JSONObject;
import org.junit.Assert;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import org.guanzon.cas.inventory.base.InvMaster;
import org.guanzon.cas.model.ImageModel;
import org.guanzon.cas.parameters.Brand;


public class PurchaseOrderMCController implements Initializable, ScreenInterface {

    private final String pxeModuleName = "Purchase Order MC";
    private GRider oApp;
    private PurchaseOrder oTrans;
    private JSONObject poJSON;
    private int pnEditMode;

    private FileChooser fileChooser;

    private String psPrimary = "";
    private boolean pbLoaded = false;
    private int pnIndex;
    private int pnDetailRow;
    private String tblClicked;

    private int pnAttachmentRow;
    private String imagePath = "D:/Guanzon/MarketPlaceImages";

    private double mouseAnchorX;
    private double mouseAnchorY;
    private double scaleFactor = 1.0;

    @FXML
    private AnchorPane MainAnchorPane;

    @FXML
    private AnchorPane apButton;

    @FXML
    private HBox hbButtons;

    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnPrint;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnFindSource;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnAddItem;

    @FXML
    private Button btnRemoveItem;

    @FXML

    private Button btnCancel;

    @FXML
    private Button btnAttachment;

    @FXML
    private Button btnPreview;

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
    private TableView tblAttachments;

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
    private ImageView imageView;

    @FXML
    private StackPane stackPane1;

    @FXML
    private Group Group1;

    private ObservableList<ModelPurchaseOrderMC> data = FXCollections.observableArrayList();
    private final ObservableList<ImageModel> img_data = FXCollections.observableArrayList();

    public void initDetailsGrid2() {
        /*FOCUS ON FIRST ROW*/

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

        if (pnAttachmentRow < 0 || pnAttachmentRow >= img_data.size()) {
            if (!img_data.isEmpty()) {
                /* FOCUS ON FIRST ROW */
                tblAttachments.getSelectionModel().select(0);
                tblAttachments.getFocusModel().focus(0);
                pnAttachmentRow = tblAttachments.getSelectionModel().getSelectedIndex();
            }
        } else {
            /* FOCUS ON THE ROW THAT pnRowDetail POINTS TO */
            tblAttachments.getSelectionModel().select(pnAttachmentRow);
            tblAttachments.getFocusModel().focus(pnAttachmentRow);
        }
    }

    private void generateImages() {
//        try {
//            img_data.clear();
//            if (oTrans.getMaster("sImagesxx") != null) {
//                JSONParser parser = new JSONParser();
//                JSONArray jsonArray = (JSONArray) parser.parse((String) oTrans.getMaster("sImagesxx").toString().replaceAll("'", "\""));
//
//                for (int i = 0; i < jsonArray.size(); i++) {
//                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                    img_data.add(new ImageModel(String.valueOf(i + 1),
//                            (String) jsonObject.get("sImageURL")));
//                }
//
//            }
//            if (img_data != null) {
//                imageView.setDisable(false);
//                initImageGrid();
//            } else {
//                img_data.add(new ImageModel(String.valueOf(1),
//                        "/org/rmj/marketplace/images/no-image-available_1.png"));
//                imageView.setDisable(true);
//                initImageGrid();
//            }
//        } catch (SQLException | ParseException ex) {
//            System.out.println(ex.getMessage());
//        }
    }

    private Stage getStage() {
        return (Stage) txtField01.getScene().getWindow();
    }

    @FXML
    void cmdButton_Click(ActionEvent event) throws IOException {

        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {
            case "btnPreview":
                String filePath = (String) img_data.get(pnAttachmentRow).getIndex13();
                String fileUrl = new File(filePath).toURI().toString();

                try {
                    // Load FXML and Controller
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/guanzon/cas/views/ImageViewer.fxml"));
                    Parent root = loader.load();

                    // Get the controller to set the image
                    ImageViewerController controller = loader.getController();
                    controller.setImage(fileUrl);

                    // Create a new Stage
                    Stage stage = new Stage(StageStyle.UNDECORATED);
                    stage.setTitle("Image Viewer");
                    stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
                    stage.setScene(new Scene(root));
                    stage.setAlwaysOnTop(true);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case "btnAttachments":
            try {
                FileAttachmentPreviewController controller = new FileAttachmentPreviewController();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/guanzon/cas/views/FileAttachmentPreview.fxml"));
                loader.setController(getClass().getResource("/org/guanzon/cas/controller/FileAttachmentPreviewController.java"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                scene.setFill(null); // Make the scene transparent
                // Create a new Stage
                Stage stage = new Stage(StageStyle.UNDECORATED);
                stage.initStyle(StageStyle.TRANSPARENT); // Remove window decorations
                stage.setTitle("Image Viewer");
                stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
                stage.setScene(scene);
                stage.setAlwaysOnTop(true);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
            break;
            case "btnAttachment":
                try {

                // Open file chooser dialog
                fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Image");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
                );
                java.io.File selectedFile = fileChooser.showOpenDialog((Stage) txtField01.getScene().getWindow());

                if (selectedFile != null) {
                    // Read image from the selected file
                    Path imgPath = selectedFile.toPath();
                    Image loimage = new Image(Files.newInputStream(imgPath));
                    imageView.setImage(loimage);
                    stackPane1.setClip(new javafx.scene.shape.Rectangle(stackPane1.getWidth(), stackPane1.getHeight()));
                    String imgPath2 = selectedFile.toString();
                    img_data.add(new ImageModel(String.valueOf(img_data.size()), imgPath2));

                    if (img_data.size() > 1) {
                        pnAttachmentRow = img_data.size() - 1;
                    }
                    tblClicked = "tblAttachments";
                    loadTableAttachment();
                    setSelectedAttachment();
                    initDetailsGrid2();

                    tblDetails.getSelectionModel().clearSelection();
                    tblDetails.getFocusModel().focus(-1);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
            case "btnBrowse":
                tblClicked = "tblDetails";
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
                tblClicked = "tblDetails";
                clearFields();
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
                tblClicked = "tblDetails";
                try {
                    Validator_PurchaseOrder_Master ValidateMaster = new Validator_PurchaseOrder_Master(oTrans.getMasterModel());

                    if (!ValidateMaster.isEntryOkay()) {
                        poJSON.put("result", "error");
                        poJSON.put("message", ValidateMaster.getMessage());
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        pnEditMode = EditMode.UNKNOWN;
                        return;
                    }
                    btnFindSource.setManaged(true);
                } catch (Exception e) {

                }

                pnEditMode = oTrans.getMasterModel().getEditMode();
                poJSON = oTrans.updateTransaction();
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
                tblClicked = "tblDetails";
                tblAttachments.getSelectionModel().clearSelection();
                tblDetails.getFocusModel().focus(- 1);
                tblDetails.getSelectionModel().select(oTrans.getItemCount() - 1);
                tblDetails.getFocusModel().focus(oTrans.getItemCount() - 1);

                if (pnIndex < 98) {
                    pnIndex = 99;
                }
                poJSON = oTrans.searchMaster("sSourceNo", "", false);
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

            case "btnSearch":
                tblClicked = "tblDetails";
                tblAttachments.getSelectionModel().clearSelection();
                tblDetails.getFocusModel().focus(- 1);
                tblDetails.getSelectionModel().select(oTrans.getItemCount() - 1);
                tblDetails.getFocusModel().focus(oTrans.getItemCount() - 1);

                if (pnIndex > 3 || pnIndex < 1) {
                    pnIndex = 1;
                }
                switch (pnIndex) {
                    case 1:
                    case 2:
                        if (oTrans.getItemCount() - 1 < 0) {
                            poJSON.put("result", "error");
                            poJSON.put("message", "No row in the table");
                        } else {
//                            oTrans.setRowSelect(pnDetailRow);
                            poJSON = oTrans.searchDetail(pnDetailRow, 3, "", pnIndex == 1); //(pnIndex == 1) ? txtDetail01.getText() :
                            try {
//                                pnDetailRow = oTrans.getRowSelect();
                            } catch (Exception e) {
                            }
                        }
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }

                        loadTableDetail();
                        break;
                }
                break;

            case "btnSave":
                tblClicked = "tblDetails";
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
//                    pnEditMode = EditMode.UNKNOWN;

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
                tblClicked = "tblDetails";
                tblAttachments.getSelectionModel().clearSelection();
                tblDetails.getFocusModel().focus(- 1);
                tblDetails.getSelectionModel().select(oTrans.getItemCount() - 1);
                tblDetails.getFocusModel().focus(oTrans.getItemCount() - 1);

                if (oTrans.getItemCount() - 1 < 0) {
                    poJSON = oTrans.AddModelDetail();
                    pnDetailRow = oTrans.getItemCount() - 1;
//                    oTrans.setBrandID("");
                    loadTableDetail();
                    poJSON.put("result", "success");
                    poJSON.put("message", "''");
                    setSelectedDetail();
                } else {
                    if (oTrans.getDetailModel(oTrans.getItemCount() - 1).getQuantity() > 0
                            && !oTrans.getDetailModel(oTrans.getItemCount() - 1).getStockID().isEmpty()) {
                        poJSON = oTrans.AddModelDetail();
                        pnDetailRow = oTrans.getItemCount() - 1;
//                        oTrans.setBrandID("");
                        loadTableDetail();
                        setSelectedDetail();

                    } else {
                        try {
                            if (oTrans.getDetailModel(oTrans.getItemCount() - 1).getStockID().isEmpty()) {
                                poJSON.put("result", "error");
                                poJSON.put("message", "'Please Fill all the required fields'");
                            } else {
                                if (oTrans.getDetailModel(oTrans.getItemCount() - 1).getQuantity() <= 0) {
                                    poJSON.put("result", "error");
                                    poJSON.put("message", "'Recent Order number should be greater than 0'");
                                }
                            }
                        } catch (Exception e) {
                            poJSON.put("result", "error");
                            poJSON.put("message", "'Please Fill all the required fields'");
                        }
                    }
                }
                if ("error".equals((String) poJSON.get("result"))) {
                    System.err.println((String) poJSON.get("message"));
                    ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    return;
                }

                break;
            case "btnRemoveItem":

                if (tblClicked == "tblDetails") {
                    if (oTrans.getItemCount() - 1 < 0) {
                        poJSON.put("result", "error");
                        poJSON.put("message", "'No rows in the table'");
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                    } else {
                        if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove this item?") == true) {
                            oTrans.RemoveModelDetail(pnDetailRow);
                            pnDetailRow = oTrans.getItemCount() - 1;
                            loadTableDetail();
                            txtField04.requestFocus();
                        }
                    }
                } else {
                    img_data.remove(pnAttachmentRow);
                    pnAttachmentRow -= 1;
                    setSelectedAttachment();
                    loadTableAttachment();
                    initDetailsGrid2();
                }

                break;
            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
                    oTrans = new PurchaseOrder(oApp, true);
                    oTrans.setTransactionStatus("12340");
                    pbLoaded = true;
                    pnEditMode = EditMode.UNKNOWN;
                    clearFields();
                    setSelectedAttachment();
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

    private void setTableSelection() {
        switch (tblClicked) {
            case "tblDetails":
                tblAttachments.getSelectionModel().clearSelection();
                tblAttachments.getFocusModel().focus(-1);
                break;
            case "tblAttachments":
                tblDetails.getSelectionModel().clearSelection();
                tblDetails.getFocusModel().focus(-1);
                break;

        }
    }

    @FXML
    void tblDetails_Clicked(MouseEvent event) {
        pnDetailRow = tblDetails.getSelectionModel().getSelectedIndex();
//        oTrans.setRowSelect(pnDetailRow);
        if (pnDetailRow >= 0) {
            setSelectedDetail();
            tblClicked = "tblDetails";
            setTableSelection();
        }

    }

    @FXML
    void tblAttachments_Clicked(MouseEvent event) {
        pnAttachmentRow = tblAttachments.getSelectionModel().getSelectedIndex();
        if (pnAttachmentRow >= 0) {
            setSelectedAttachment();
            tblClicked = "tblAttachments";
            setTableSelection();
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

    private void setSelectedAttachment() {
        try {
            if (pnAttachmentRow >= 0 && pnEditMode != EditMode.UNKNOWN) {
                String filePath = (String) img_data.get(pnAttachmentRow).getIndex13();
                if (filePath.length() != 0) {
                    Path imgPath = Paths.get(filePath);
                    Image loimage = new Image(Files.newInputStream(imgPath));
                    imageView.setImage(loimage);
                    stackPane1.setClip(new javafx.scene.shape.Rectangle(stackPane1.getWidth(), stackPane1.getHeight()));
                } else {
                    imageView.setImage(null);
                    stackPane1.setClip(new javafx.scene.shape.Rectangle(stackPane1.getWidth(), stackPane1.getHeight()));
                }
            } else {
                imageView.setImage(null);
                stackPane1.setClip(new javafx.scene.shape.Rectangle(stackPane1.getWidth(), stackPane1.getHeight()));
                pnAttachmentRow = 0;
            }
        } catch (IOException ex) {
            Logger.getLogger(PurchaseOrderMCController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setSelectedDetail() {
        txtDetail01.setText((String) data.get(pnDetailRow).getIndex02());
        txtDetail02.setText((String) data.get(pnDetailRow).getIndex03());
        txtDetail03.setText((String) data.get(pnDetailRow).getIndex04());
        txtDetail04.setText(oTrans.getDetailModel(pnDetailRow).getDescription());
        txtDetail05.setText((String) data.get(pnDetailRow).getIndex08());
        txtDetail06.setText(Integer.toString(oTrans.getDetailModel(pnDetailRow).getQuantity()));

//        InvMaster loInv_Master = oTrans.GetInvMaster((String) oTrans.getDetailModel(pnDetailRow).getValue("sStockIDx"), true);

        TextField[] textFields = {txtDetail07, txtDetail08, txtDetail10, txtDetail11};
        String[] keys = {"nQtyOnHnd", "nMaxLevel", "nResvOrdr", "nBackOrdr"};

        for (int i = 0; i < textFields.length; i++) {
            try {
//                textFields[i].setText(String.valueOf(loInv_Master.getMaster(keys[i])));

            } catch (Exception e) {
                textFields[i].setText("");
            }
        }
        try {
//            txtDetail09.setText((String) loInv_Master.getMaster("cClassify"));
        } catch (Exception e) {
        }

    }

    private void loadTableAttachment() {
        //load image data
//        img_data.clear();
        List<ImageModel> tempData = new ArrayList<>();

        // Copy the data
        for (int i = 0; i < img_data.size(); i++) {
            tempData.add(new ImageModel(String.valueOf(i), img_data.get(i).getIndex13()));
        }

        // Clear the original data
        img_data.clear();

        // Add the copied data back if needed
        img_data.addAll(tempData);

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

                loInventory = oTrans.GetInventory((String) oTrans.getDetailModel(lnCtr).getValue("sStockIDx"), true);
//                loBrand = oTrans.GetBrand((String) loInventory.getMaster("sBrandIDx"), true);

                loMdl = oTrans.GetModel((String) loInventory.getMaster("sModelIDx"), true);
                loMdlVrnt = oTrans.GetModel_Variant((String) loMdl.getModel().getVariantID(), true);
//                loColor = oTrans.GetColor((String) loInventory.getMaster("sColorIDx"), true);
                try {
                    ln1 = Double.parseDouble((oTrans.getDetailModel(lnCtr).getValue("nUnitPrce")).toString()) * Double.parseDouble(oTrans.getDetailModel(lnCtr).getValue("nQuantity").toString());
                } catch (Exception e) {
                }

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
                
                try{
                    if (loInventory.getMaster("xBrandNme").toString().length() == 0) {
                        if (pnEditMode == EditMode.ADDNEW) {
//                            loInventory2 = (String) loBrand.getMaster("sDescript");
                        }
                    } else {
                        loInventory2 = (String) loInventory.getMaster("xBrandNme");
                    }
                }catch(Exception e){
                    
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

            } else {
                try {
//                    loBrand = oTrans.GetBrand(oTrans.getBrandID(), true);
                    if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
//                        loInventory2 = (String) loBrand.getMaster("sDescript");
                    }
                } catch (Exception e) {

                }

                data.add(new ModelPurchaseOrderMC(String.valueOf(lnCtr + 1),
                        loInventory2,
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

        tblClicked = "tblDetails";
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

//        oTrans.setRowSelect(oTrans.getItemCount() - 1);
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
                    txtField.setText(CommonUtils.dateFormat(oTrans.getMasterModel().getTransactionDate(), "MM-dd-yyyy"));
                    break;

                case 8://Reference No
                    if (txtField.getText().length() > 12) {
                        ShowMessageFX.Warning("Max characters for `Reference No` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                        txtField.requestFocus();
                        return;
                    }
                    poJSON = oTrans.getMasterModel().setReferenceNo(lsValue);
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 10://Disc
                    if (txtField.getText().length() > 7) {
                        ShowMessageFX.Warning("Max characters for `Discount Rate` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                        txtField.requestFocus();
                        return;
                    }
                    try {
                        double x = Double.valueOf(lsValue);
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();
                    }
                    poJSON = oTrans.getMasterModel().setDiscount(Double.valueOf(lsValue));
                    if ("error".equals((String) poJSON.get("result"))) {
                        System.err.println((String) poJSON.get("message"));
                        ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                        return;
                    }
                    break;
                case 11://AddDisc Rate
                try {
                    double x = Double.valueOf(lsValue);
                } catch (Exception e) {
                    ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                    txtField.requestFocus();
                }
                if (txtField.getText().length() > 10) {
                    ShowMessageFX.Warning("Max characters for `Discount Rate` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                    txtField.requestFocus();
                    return;
                }
                poJSON = oTrans.getMasterModel().setAddDiscount(Double.valueOf(lsValue));
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
                case 1:
                    break;
                case 2:
                    String lsStockID = (String) oTrans.getDetailModel(pnDetailRow).getValue("sStockIDx");
                    if (lsStockID == null || lsStockID.isEmpty()) {
                        if (txtField.getText().length() > 128) {
                            ShowMessageFX.Warning("Max characters for `Item Description` exceeds the limit.", pxeModuleName, "Please verify your entry.");
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

                case 6:
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
                    case 3:
                        /*sCompany*/
                        poJSON = oTrans.searchMaster(4, lsValue, true);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                            txtField01.requestFocus();

                        } else {
                            loadRecord();
                        }
                        break;

                    case 4:
                        /*sDestinat*/
                        poJSON = oTrans.searchMaster(5, lsValue, false);
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                            ShowMessageFX.Information(null, pxeModuleName, (String) poJSON.get("message"));
                            txtField01.requestFocus();

                            ShowMessageFX.Information((String) poJSON.get("message"), (String) poJSON.get("message"), lsValue);
                            txtField01.requestFocus();
                        } else {
                            loadRecord();
                        }
                        break;

                    case 5:
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
                    case 8:
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

    private void txtDetail_KeyPressed(KeyEvent event) {

        TextField textField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(9, 11));
        String lsValue = textField.getText();
        if (lsValue == null) {
            lsValue = "";
        }
        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {
                    case 1: //Brand
                        oTrans.getDetailModel(pnDetailRow).setStockID("");
                        oTrans.getDetailModel(pnDetailRow).setUnitPrice(0);
                        oTrans.getDetailModel(pnDetailRow).setQuantity(0);
                        oTrans.getDetailModel(pnDetailRow).setDescription("");
                        txtDetail04.clear();
                        poJSON = oTrans.searchDetail(pnDetailRow, "sBrandIDx", lsValue, false);
                        try {
                            pnDetailRow = tblDetails.getSelectionModel().getSelectedIndex();
                        } catch (Exception e) {
                        }
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        } else {

                        }

                        
                        
                        break;
                    case 2: //Model
                        poJSON = oTrans.searchDetail(pnDetailRow, "sModelIDx", lsValue, true);
                        try {
//                            pnDetailRow = oTrans.getRowSelect();
                        } catch (Exception e) {
                        }
                        if ("error".equalsIgnoreCase(poJSON.get("result").toString())) {
                            ShowMessageFX.Information((String) poJSON.get("message"), "Computerized Acounting System", pxeModuleName);
                        }
                        break;

                    case 3: //Color
                        poJSON = oTrans.searchDetail(pnDetailRow, "sColorxxx", lsValue, false);
                        try {
//                            pnDetailRow = oTrans.getRowSelect();
                        } catch (Exception e) {
                        }
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

    public void initDetailsGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index06.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index07.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
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

        txtDetail01.focusedProperty().addListener(txtDetail_Focus);
        txtDetail02.focusedProperty().addListener(txtDetail_Focus);
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail07.focusedProperty().addListener(txtDetail_Focus);

        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);

        txtDetail01.setOnKeyPressed(this::txtDetail_KeyPressed);//barcode
        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail03.setOnKeyPressed(this::txtDetail_KeyPressed);

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

        txtDetail01.clear();
        txtDetail02.clear();
        txtDetail03.clear();
        txtDetail04.clear();
        txtDetail05.clear();
        txtDetail06.clear();

        txtDetail07.clear();
        txtDetail08.clear();
        txtDetail09.clear();
        txtDetail10.clear();
        txtDetail11.clear();
        txtDetail12.clear();
        txtDetail13.clear();
        txtDetail14.clear();
        txtDetail15.clear();

        psPrimary = "";
        lblStatus.setText("UNKNOWN");

        pnDetailRow = -1;
        pnIndex = -1;

        img_data.clear();
        data.clear();

    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

// Manage visibility and managed state of buttons
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        btnAddItem.setVisible(lbShow);
        btnRemoveItem.setVisible(lbShow);
        btnAttachment.setVisible(lbShow);
        btnPreview.setVisible(lbShow);

        btnCancel.setManaged(lbShow);
        btnSearch.setManaged(lbShow);
        btnSave.setManaged(lbShow);
        btnAddItem.setManaged(lbShow);
        btnRemoveItem.setManaged(lbShow);
        btnAttachment.setManaged(lbShow);
        btnPreview.setManaged(lbShow);

        if (fnValue == EditMode.ADDNEW) {
            btnFindSource.setManaged(lbShow);
            btnFindSource.setVisible(lbShow);
        } else {
            btnFindSource.setManaged(false);
            btnFindSource.setVisible(false);
        }

// Manage visibility and managed state of other buttons
        btnBrowse.setVisible(!lbShow);
        btnPrint.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnBrowse.setManaged(!lbShow);
        btnPrint.setManaged(!lbShow);
        btnNew.setManaged(!lbShow);
        btnUpdate.setManaged(!lbShow);
        btnClose.setManaged(!lbShow);

        apBrowse.setDisable(lbShow);
        apMaster.setDisable(!lbShow);
        apDetail.setDisable(!lbShow);
        apTable.setDisable(!lbShow);

//        oTrans.setTransType("MC");

    }

    private void initImageView() {

        Group1.setOnScroll((ScrollEvent event) -> {
            double delta = event.getDeltaY();
            if (delta > 0) {
                scaleFactor *= 1.1;
            } else {
                scaleFactor *= 0.9;
            }
            Group1.setScaleX(scaleFactor);
            Group1.setScaleY(scaleFactor);
        });

        Group1.setOnMousePressed((MouseEvent event) -> {
            mouseAnchorX = event.getSceneX() - Group1.getTranslateX();
            mouseAnchorY = event.getSceneY() - Group1.getTranslateY();
        });

        Group1.setOnMouseDragged((MouseEvent event) -> {
            Group1.setTranslateX(event.getSceneX() - mouseAnchorX);
            Group1.setTranslateY(event.getSceneY() - mouseAnchorY);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        oTrans = new PurchaseOrder(oApp, false);
        oTrans.setTransactionStatus("12340");

        initTextFields();
        initDetailsGrid();
        clearFields();
        fileChooser = new FileChooser();
        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        pbLoaded = true;
        initImageView();

    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

}
