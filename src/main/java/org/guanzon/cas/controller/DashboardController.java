package org.guanzon.cas.controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.SQLUtil;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.guanzon.cas.inventory.stock.request.RequestControllerFactory;

public class DashboardController implements Initializable {

    private GRider oApp;
    private Stage stage;

    private int targetTabIndex = -1;
    private double tabsize;

    List<String> tabName = new ArrayList<>();

    @FXML
    private Button btnClose, btnMinimize;

    @FXML
    private MenuItem mnuClientParameter;

    @FXML
    private MenuItem mnuClientTransaction;

    @FXML
    private MenuItem mnuNewCustomer;

    @FXML
    private MenuItem mnuPOQuotationRequest;

    @FXML
    private MenuItem mnuParameterBranch;
    
    @FXML
    private MenuItem mnuPOQuotation;
    @FXML
    private Menu menuRequestRegular, menuRequestROQ, menuRequestCancel, menuHistStockRequest, menuHistStockRequestCancel;
    
    @FXML
    private MenuItem mnuStockRequestWithoutROQ, mnuStockRequestWithoutROQSP, mnuStockRequestWithoutROQGI;
    @FXML
    private MenuItem mnuStockRequestROQ, mnuStockRequestROQSP, mnuStockRequestROQGI;
    @FXML
    private MenuItem mnuStockRequestCancellation, mnuStockRequestCancellationSP, mnuStockRequestCancellationGI;
    
    @FXML
    private MenuItem mnuHistStockRequest, mnuHistStockRequestSP, mnuHistStockRequestGI;
    @FXML
    private MenuItem mnuHistStockRequestCancellation, mnuHistStockRequestSP1, mnuHistStockRequestGI1;
    

    @FXML
    private TabPane tabpane;
    @FXML
    StackPane workingSpace;

    @FXML
    private Label AppBranch;

    @FXML
    private Label AppUserInfo;

    @FXML
    private Label AppDateTime;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize GRider object
        if (oApp != null) {
            // Set branch name and user info
            AppBranch.setText(oApp.getBranchName());
            loadUserInfo();
        } else {
            // Handle case where GRider object is not initialized
            System.out.println("GRider object is not properly initialized.");
        }
        
        String industry = System.getProperty("store.inventory.industry");

        RequestControllerFactory.RequestType types = null;
        String[] category = industry.split(";");
        
        // Print the resulting array
        for (String type : category) {
            if (types == null) {
                menuRequestRegular.getItems().clear();
                menuRequestROQ.getItems().clear();
                menuRequestCancel.getItems().clear();
                menuHistStockRequest.getItems().clear();
                menuHistStockRequestCancel.getItems().clear();
                if ("0001".equals(type)) {
                   menuRequestRegular.getItems().add(mnuStockRequestWithoutROQ);
                   menuRequestRegular.getItems().add(mnuStockRequestWithoutROQSP);
                   menuRequestROQ.getItems().add(mnuStockRequestROQ);
                   menuRequestROQ.getItems().add(mnuStockRequestROQSP);
                   menuRequestCancel.getItems().add(mnuStockRequestCancellation);
                   menuRequestCancel.getItems().add(mnuStockRequestCancellationSP);
                   menuHistStockRequest.getItems().add(mnuHistStockRequest);
                   menuHistStockRequest.getItems().add(mnuHistStockRequestSP);
                   menuHistStockRequestCancel.getItems().add(mnuHistStockRequestCancellation);
                   menuHistStockRequestCancel.getItems().add(mnuHistStockRequestSP1);
                } else if ("0002".equals(type)) {
                   menuRequestRegular.getItems().add(mnuStockRequestWithoutROQ);
                   menuRequestROQ.getItems().add(mnuStockRequestROQ);
                   menuRequestCancel.getItems().add(mnuStockRequestCancellation);
                   menuHistStockRequest.getItems().add(mnuHistStockRequest);
                   menuHistStockRequest.getItems().add(mnuHistStockRequest);
                   menuHistStockRequestCancel.getItems().add(mnuHistStockRequestCancellation);
                } else if ("0003".equals(type)) {
                   menuRequestRegular.getItems().add(mnuStockRequestWithoutROQ);
                   menuRequestROQ.getItems().add(mnuStockRequestROQ);
                   menuRequestCancel.getItems().add(mnuStockRequestCancellation);
                   menuHistStockRequest.getItems().add(mnuHistStockRequest);
                   menuHistStockRequest.getItems().add(mnuHistStockRequest);
                   menuHistStockRequestCancel.getItems().add(mnuHistStockRequestCancellation);
                }else if ("0004".equals(type)) {
                   menuRequestRegular.getItems().add(mnuStockRequestWithoutROQGI);
                   menuRequestROQ.getItems().add(mnuStockRequestROQGI);
                   menuRequestCancel.getItems().add(mnuStockRequestCancellationGI);
                   menuHistStockRequest.getItems().add(mnuHistStockRequestGI);
                   menuHistStockRequestCancel.getItems().add(mnuHistStockRequestGI1);
                }

                System.out.println("type value = " + types);
            }
        }
        for(int lnCtr = 0; lnCtr <= menuRequestRegular.getItems().size()-1; lnCtr++){
            
                
            System.out.println(menuRequestRegular.getItems().get(lnCtr));
        }
        getTime();
        loadUserInfo();
        setTabPane();
        initButtons();
    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void loadUserInfo() {
        AppUserInfo.setText(oApp.getLogName() + " || " + oApp.getDivisionName());
    }

    public TabPane loadAnimate(String fsFormName) {
        //set fxml controller class
        if (tabpane.getTabs().isEmpty()) {
            tabpane = new TabPane();
        }

        setTabPane();

        ScreenInterface fxObj = getController(fsFormName);
        fxObj.setGRider(oApp);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
        fxmlLoader.setController(fxObj);

        //Add new tab;
        Tab newTab = new Tab(SetTabTitle(fsFormName));
//        newTab.setStyle("-fx-font-weight: bold; -fx-pref-width: 180; -fx-font-size: 10.5px; -fx-border-color: #F88222 #F88222 transparent #F88222;");
        //tabIds.add(fsFormName);
        newTab.setContent(new javafx.scene.control.Label("Content of Tab " + fsFormName));
        newTab.setContextMenu(createContextMenu(tabpane, newTab, oApp));
        // Attach a context menu to each tab
        tabName.add(SetTabTitle(fsFormName));

        // Save the list of tab IDs to the JSON file
//        TabsStateManager.saveCurrentTab(tabName);
        try {
            Node content = fxmlLoader.load();
            newTab.setContent(content);
            tabpane.getTabs().add(newTab);
            tabpane.getSelectionModel().select(newTab);

            //newTab.setOnClosed(event -> {
            newTab.setOnCloseRequest(event -> {
                if (showMessage()) {
                    tabName.remove(newTab.getText());

                    Tabclose();
                } else {
                    event.consume();
                }

            });

            newTab.setOnSelectionChanged(event -> {
                ObservableList<Tab> tabs = tabpane.getTabs();
                for (Tab tab : tabs) {
                    if (tab.getText().equals(newTab.getText())) {
                        tabName.remove(newTab.getText());
                        tabName.add(newTab.getText());
                        // Save the list of tab IDs to the JSON file
//                        TabsStateManager.saveCurrentTab(tabName);
                        break;
                    }
                }

            });
            return (TabPane) tabpane;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Load Main Screen if no tab remain
    public void Tabclose() {
        int tabsize = tabpane.getTabs().size();
        if (tabsize == 0) {
            setScene(loadAnimateAnchor("Dashboard.fxml"));
        }
    }

    public String SetTabTitle(String menuaction) {
        switch (menuaction) {
            /*Files Menu > Accounts*/
            case "/org/guanzon/cas/views/FrmAccountsPayable.fxml":
                return "Accounts Payable Clients";
            case "/org/guanzon/cas/views/FrmAccountsReceivable.fxml":
                return "Accounts Receivable Clients";

            /*Files Menu > Clients*/
            case "/org/guanzon/cas/views/ClientMasterParameter.fxml":
                return "Client Parameter";
            case "/org/guanzon/cas/views/ClientMasterTransactionCompany.fxml":
                return "Client Transactions Company";
            case "/org/guanzon/cas/views/ClientMasterTransactionIndividual.fxml":
                return "Client Transactions Individual";
            case "/org/guanzon/cas/views/NewCustomer.fxml":
                return "Client Transactions Standard";

            /*Files Menu > Inventory*/
            case "/org/guanzon/cas/views/InventoryParam.fxml":
                return "Inventory Parameter";
            case "/org/guanzon/cas/views/InventorySerialParam.fxml":
                return "Inventory Serial Parameter";

            case "/org/guanzon/cas/views/FrmAccountsAccreditation.fxml":
                return "Accounts Accreditation";

            /*Files Menu > Parameters*/
            case "/org/guanzon/cas/views/AffiliatedCompany.fxml":
                return "Affiliated Company";
            case "/org/guanzon/cas/views/Banks.fxml":
                return "Banks";
            case "/org/guanzon/cas/views/BanksBranches.fxml":
                return "Banks Branches";
            case "/org/guanzon/cas/views/Barangay.fxml":
                return "Barangay";
            case "/org/guanzon/cas/views/Branch.fxml":
                return "Branch";
            case "/org/guanzon/cas/views/Brand.fxml":
                return "Brand";
            case "/org/guanzon/cas/views/Category.fxml":
                return "Category";
            case "/org/guanzon/cas/views/CategoryLevel2.fxml":
                return "Category Level 2";
            case "/org/guanzon/cas/views/CategoryLevel3.fxml":
                return "Category Level 3";
            case "/org/guanzon/cas/views/CategoryLevel4.fxml":
                return "Category Level 4";
            case "/org/guanzon/cas/views/Color.fxml":
                return "Color";
            case "/org/guanzon/cas/views/ColorDetail.fxml":
                return "Color Detail";
            case "/org/guanzon/cas/views/Company.fxml":
                return "Company";
            case "/org/guanzon/cas/views/Country.fxml":
                return "Country";
            case "/org/guanzon/cas/views/Department.fxml":
                return "Department";
            case "/org/guanzon/cas/views/InventoryLocation.fxml":
                return "Inventory Location";
            case "/org/guanzon/cas/views/InventoryType.fxml":
                return "Inventory Type";
            case "/org/guanzon/cas/views/Labor.fxml":
                return "Labor";
            case "/org/guanzon/cas/views/LaborCategory.fxml":
                return "Labor Category";
            case "/org/guanzon/cas/views/LaborModel.fxml":
                return "Labor Model";
            case "/org/guanzon/cas/views/Made.fxml":
                return "Made";
            case "/org/guanzon/cas/views/Measure.fxml":
                return "Measure";
            case "/org/guanzon/cas/views/Model.fxml":
                return "Model";
            case "/org/guanzon/cas/views/Province.fxml":
                return "Province";
            case "/org/guanzon/cas/views/Region.fxml":
                return "Region";
            case "/org/guanzon/cas/views/Relationship.fxml":
                return "Relationship";
            case "/org/guanzon/cas/views/Salesman.fxml":
                return "Salesman";
            case "/org/guanzon/cas/views/Section.fxml":
                return "Section";
            case "/org/guanzon/cas/views/Size.fxml":
                return "Size";
            case "/org/guanzon/cas/views/Term.fxml":
                return "Term";
            case "/org/guanzon/cas/views/Warehouse.fxml":
                return "Warehouse";


            /*INVENTORY MENU*/    
            case "/org/guanzon/cas/views/InventoryDetail.fxml":
                return "Inventory Details";  
                
            /*INVENTORY MENU*/    
            case "/org/guanzon/cas/views/InventoryRequestMotorcycle.fxml":
                return "Inventory Request MC"; 
                
            case "/org/guanzon/cas/views/InventoryRequestMobilephoneUnits.fxml":
                return "Inventory Request Mobilephone Units";  
            
            case "/org/guanzon/cas/views/InventoryRequestAppliances.fxml":
                return "Inventory Request Appliances"; 
                
            case "/org/guanzon/cas/views/InventoryRequestVehicle.fxml":
                return "Inventory Request Vehicle"; 
               
            case "/org/guanzon/cas/views/InventoryRequestSpareparts.fxml":
                return "Inventory Request Spareparts";    
                
            /*Inventory Stock Request NEW UI*/    
            case "/org/guanzon/cas/views/InvRequestWithoutROQ.fxml":
                return "Inventory Request";
                
            case "/org/guanzon/cas/views/InvRequestWithoutROQSP.fxml":
                return "Inventory Request SP";
                
            case "/org/guanzon/cas/views/InvRequestWithoutROQGI.fxml":
                return "Inventory Request GI";
            
            case "/org/guanzon/cas/views/InvRequestROQ.fxml":
                return "Inventory Request ROQ";            
            
            case "/org/guanzon/cas/views/InvRequestROQSP.fxml":
                return "Inventory Request ROQ SP";
                
            /*Inventory Stock Request cancellation NEW UI*/    
            case "/org/guanzon/cas/views/InvRequestCancellation.fxml":
                return "Inventory Request Cancel"; 
                
            case "/org/guanzon/cas/views/InvRequestCancellationSP.fxml":
                return "Inventory Request Cancel SP"; 
                
            case "/org/guanzon/cas/views/InvRequestCancellationGI.fxml":
                return "Inventory Request Cancel GI";
            
                
            /*PURCHASE ORDER QUOTATION MENU*/
            case "/org/guanzon/cas/views/PO_Quotation_Request.fxml":
                return "Purchase Quotation Request";
            
            case "/org/guanzon/cas/views/PO_Quotation.fxml":
                return "Purchase Quotation";
                
            case "/org/guanzon/cas/views/PurchaseOrderMC.fxml":
                return "Purchase Order MC";
            case "/org/guanzon/cas/views/PurchaseOrderConfirmationMC.fxml":
                return "Purchase Order Confirmation MC";
            case "/org/guanzon/cas/views/PurchaseOrderHistoryMC.fxml":
                return "Purchase Order History MC";
                
            case "/org/guanzon/cas/views/PurchaseOrderSP.fxml":
                return "Purchase Order SP";
                
            case "/org/guanzon/cas/views/PurchaseOrderConfirmationSP.fxml":
                return "Purchase Order Confirmation SP";
            case "/org/guanzon/cas/views/PurchaseOrderHistorySP.fxml":
                return "Purchase Order History SP";
                
            /*History Menu*/
            case "/org/guanzon/cas/views/InvRequestHistory.fxml":
                return "Inventory Request History";
                
            case "/org/guanzon/cas/views/InvRequestHistorySP.fxml":
                return "Inventory Request History SP";
                
            case "/org/guanzon/cas/views/InvRequestHistoryGI.fxml":
                return "Inventory Request History GI";
              
            case "/org/guanzon/cas/views/InvRequestCancellationHistory.fxml":
                return "Inventory Request Cancellation History";
                
            case "/org/guanzon/cas/views/InvRequestCancellationHistorySP.fxml":
                return "Inventory Request Cancellation History SP";
                
            case "/org/guanzon/cas/views/InvRequestCancellationHistoryGI.fxml":
                return "Inventory Request Cancellation History GI";  
                
            default:
                return null;
        }
    }

    public ContextMenu createContextMenu(TabPane tabPane, Tab tab, GRider oApp) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem closeTabItem = new MenuItem("Close Tab");
        MenuItem closeOtherTabsItem = new MenuItem("Close Other Tabs");
        MenuItem closeAllTabsItem = new MenuItem("Close All Tabs");

        closeTabItem.setOnAction(event -> closeSelectTabs(tabPane, tab));
        closeOtherTabsItem.setOnAction(event -> closeOtherTabs(tabPane, tab));
        closeAllTabsItem.setOnAction(event -> closeAllTabs(tabPane, oApp));

        contextMenu.getItems().add(closeTabItem);
        contextMenu.getItems().add(closeOtherTabsItem);
        contextMenu.getItems().add(closeAllTabsItem);

        tab.setContextMenu(contextMenu);

        closeOtherTabsItem.visibleProperty().bind(Bindings.size(tabPane.getTabs()).greaterThan(1));

        return contextMenu;
    }

    private void closeSelectTabs(TabPane tabPane, Tab tab) {
        if (showMessage()) {
            Tabclose(tabPane);
            tabName.remove(tab.getText());
            tabPane.getTabs().remove(tab);
        }
//        if (ShowMessageFX.YesNo(null, "Close Tab", "Are you sure, do you want to close tab?") == true) {

//            TabsStateManager.saveCurrentTab(tabName);
//            TabsStateManager.closeTab(tab.getText());
//        }
    }
    //Load Main Screen if no tab remain

    public void Tabclose(TabPane tabpane) {
        int tabsize = tabpane.getTabs().size();
        if (tabsize == 0) {
            setScene(loadAnimateAnchor("Dashboard.fxml"));
        }
    }

    private void closeOtherTabs(TabPane tabPane, Tab currentTab) {
//        if (ShowMessageFX.YesNo(null, "Close Other Tab", "Are you sure, do you want to close other tab?") == true) {
        if (showMessage()) {
            tabPane.getTabs().removeIf(tab -> tab != currentTab);
            List<String> currentTabNameList = Collections.singletonList(currentTab.getText());
            tabName.retainAll(currentTabNameList);
//            TabsStateManager.saveCurrentTab(tabName);
            for (Tab tab : tabPane.getTabs()) {
                String formName = tab.getText();
//                TabsStateManager.closeTab(formName);
            }
        }
//        }
    }

    private void closeAllTabs(TabPane tabPane, GRider oApp) {
//        if (ShowMessageFX.YesNo(null, "Close All Tabs", "Are you sure, do you want to close all tabs?") == true) {
        if (showMessage()) {
            tabName.clear();
//            TabsStateManager.saveCurrentTab(tabName);
            // Close all tabs using your TabsStateManager
            for (Tab tab : tabPane.getTabs()) {
                String formName = tab.getText();
//                TabsStateManager.closeTab(formName);
            }
            tabPane.getTabs().clear();
//            unloadForm unload = new unloadForm();
            StackPane myBox = (StackPane) tabpane.getParent();
            myBox.getChildren().clear();
//            myBox.getChildren().add(unload.getScene("FXMLMainScreen.fxml", oApp));
        }

    }

    public void setTabPane() {
        // set up the drag and drop listeners on the tab pane
        tabpane.setOnDragDetected(event -> {
            Dragboard db = tabpane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(tabpane.getSelectionModel().getSelectedItem().getText());
            db.setContent(content);
            event.consume();
        });

        tabpane.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
            }
        });

        tabpane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String tabText = db.getString();
                int draggedTabIndex = findTabIndex(tabText);
                //double mouseP , mousePCom;
                double mouseX = event.getX();
                double mouseY = event.getY();
                Bounds headerBounds = tabpane.lookup(".tab-header-area").getBoundsInParent();
                Point2D mouseInScene = tabpane.localToScene(mouseX, mouseY);
                Point2D mouseInHeader = tabpane.sceneToLocal(mouseInScene);
                double tabHeaderHeight = tabpane.lookup(".tab-header-area").getBoundsInParent().getHeight();
                System.out.println("mouseY " + mouseY);
                System.out.println("tabHeaderHeight " + tabHeaderHeight);

//                    mouse is over the tab header area
//                    mouseP = ((mouseInHeader.getX() / headerBounds.getWidth()));
//                    tabsize = tabpane.getTabs().size();
//                    mousePCom = mouseP * tabsize;
//                    targetTabIndex = (int) Math.round(mousePCom) ;
//
//                    double tabWidth = headerBounds.getWidth() / tabpane.getTabs().size();
//                    targetTabIndex = (int) ((mouseX - headerBounds.getMinX()) / tabWidth);
                targetTabIndex = (int) (mouseX / 180);
                System.out.println("targetTabIndex " + targetTabIndex);
                if (mouseY < tabHeaderHeight) {
                    //if (headerBounds.contains(mouseInHeader)) {
                    System.out.println("mouseInHeader.getX() " + mouseInHeader.getX());
                    System.out.println("headerBounds.getWidth() " + headerBounds.getWidth());
                    System.out.println("tabsize " + tabpane.getTabs().size());
                    System.out.println("tabText " + tabText);
                    System.out.println("draggedTabIndex " + draggedTabIndex);

                    if (draggedTabIndex != targetTabIndex) {
                        Tab draggedTab = tabpane.getTabs().remove(draggedTabIndex);
                        if (targetTabIndex > tabpane.getTabs().size()) {
                            targetTabIndex = tabpane.getTabs().size();
                        }
                        tabpane.getTabs().add(targetTabIndex, draggedTab);
                        tabpane.getSelectionModel().select(draggedTab);
                        success = true;

                    }
                    //}
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        tabpane.setOnDragDone(event -> {
            event.consume();
        });

    }

    private int findTabIndex(String tabText) {
        ObservableList<Tab> tabs = tabpane.getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getText().equals(tabText)) {
                return i;
            }
        }
        return -1;
    }

    private void getTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Calendar cal = Calendar.getInstance();
            int second = cal.get(Calendar.SECOND);

            Date date = new Date();
            String strTimeFormat = "hh:mm:";
            String strDateFormat = "MMMM dd, yyyy";
            String secondFormat = "ss";

            DateFormat timeFormat = new SimpleDateFormat(strTimeFormat + secondFormat);
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

            String formattedTime = timeFormat.format(date);
            String formattedDate = dateFormat.format(date);

            AppDateTime.setText(formattedDate + " | " + formattedTime);
        }),
                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void initButtons() {
        btnClose.setOnAction(this::handleButtonAction);
        btnMinimize.setOnAction(this::handleButtonAction);
        // Add more button initializations here if needed
    }

    private void handleButtonAction(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            switch (clickedButton.getId()) {
                case "btnClose":
                    Platform.exit();
                    break;
                case "btnMinimize":
                    Stage stage = (Stage) btnMinimize.getScene().getWindow();
                    stage.setIconified(true);
//                    stage.setIconified(true); // Try to minimize the stage
//                    stage.setIconified(true);
//                    stage.initStyle(StageStyle.DECORATED);
                    break;
                // Add more cases for other buttons if needed
            }
        }
    }

    /*LOAD ANIMATE FOR ANCHORPANE MAIN HOME*/
    public AnchorPane loadAnimateAnchor(String fsFormName) {
        System.err.println("fsFormName to close == " + String.valueOf(fsFormName));
        ScreenInterface fxObj = getController(fsFormName);
        System.err.println("fxObj to close == " + String.valueOf(fxObj));
        fxObj.setGRider(oApp);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxObj.getClass().getResource(fsFormName));
        fxmlLoader.setController(fxObj);

        AnchorPane root;
        try {
            root = (AnchorPane) fxmlLoader.load();
            FadeTransition ft = new FadeTransition(Duration.millis(1500));
            ft.setNode(root);
            ft.setFromValue(1);
            ft.setToValue(1);
            ft.setCycleCount(1);
            ft.setAutoReverse(false);
            ft.play();

            return root;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }


    private ScreenInterface getController(String fsValue) {
        switch (fsValue) {
            case "/org/guanzon/cas/views/ClientMasterParameter.fxml":
                return new ClientMasterParameterController();
            case "/org/guanzon/cas/views/ClientMasterTransactionCompany.fxml":
                return new ClientMasterTransactionCompanyController();
            case "/org/guanzon/cas/views/ClientMasterTransactionIndividual.fxml":
                return new ClientMasterTransactionIndividualController();
            case "/org/guanzon/cas/views/NewCustomer.fxml":
                return new NewCustomerController();

            case "/org/guanzon/cas/views/FrmAccountsPayable.fxml":
                return new FrmAccountsPayableController();
            case "/org/guanzon/cas/views/FrmAccountsReceivable.fxml":
                return new FrmAccountsReceivableController();

            case "/org/guanzon/cas/views/FrmAccountsAccreditation.fxml":
                return new FrmAccountsAccreditationController();
                
            case "/org/guanzon/cas/views/InventoryParam.fxml":
                return new InventoryParamController();
            
            case "/org/guanzon/cas/views/InventorySerialParam.fxml":
                return new InventorySerialParamController();

            //Parameter MenuControllers
            case "/org/guanzon/cas/views/AffiliatedCompany.fxml":
                return new AffiliatedCompanyController();
            case "/org/guanzon/cas/views/Banks.fxml":
                return new BanksController();
            case "/org/guanzon/cas/views/BanksBranches.fxml":
                return new BankBranchesController();
            case "/org/guanzon/cas/views/Barangay.fxml":
                return new BarangayController();
            case "/org/guanzon/cas/views/Branch.fxml":
                return new BranchController();
            case "/org/guanzon/cas/views/Brand.fxml":
                return new BrandController();
            case "/org/guanzon/cas/views/Category.fxml":
                return new CategoryController();
            case "/org/guanzon/cas/views/CategoryLevel2.fxml":
                return new CategoryLevel2Controller();
            case "/org/guanzon/cas/views/CategoryLevel3.fxml":
                return new CategoryLevel3Controller();
            case "/org/guanzon/cas/views/CategoryLevel4.fxml":
                return new CategoryLevel4Controller();
            case "/org/guanzon/cas/views/Color.fxml":
                return new ColorController();
            case "/org/guanzon/cas/views/ColorDetail.fxml":
                return new ColorDetailController();
            case "/org/guanzon/cas/views/Company.fxml":
                return new CompanyController();
            case "/org/guanzon/cas/views/Country.fxml":
                return new CountryController();
            case "/org/guanzon/cas/views/Department.fxml":
                return new DepartmentController();
            case "/org/guanzon/cas/views/InventoryLocation.fxml":
                return new InventoryLocationController();
            case "/org/guanzon/cas/views/InventoryType.fxml":
                return new InventoryTypeController();
            case "/org/guanzon/cas/views/Labor.fxml":
                return new LaborController();
            case "/org/guanzon/cas/views/LaborCategory.fxml":
                return new LaborCategoryController();
            case "/org/guanzon/cas/views/LaborModel.fxml":
                return new LaborModelController();
            case "/org/guanzon/cas/views/Made.fxml":
                return new MadeController();
            case "/org/guanzon/cas/views/Measure.fxml":
                return new MeasureController();
            case "/org/guanzon/cas/views/Model.fxml":
                return new ModelController();
            case "/org/guanzon/cas/views/Province.fxml":
                return new ProvinceController();
            case "/org/guanzon/cas/views/Region.fxml":
                return new RegionController();
            case "/org/guanzon/cas/views/Relationship.fxml":
                return new RelationshipController();
            case "/org/guanzon/cas/views/Salesman.fxml":
                return new SalesmanController();
            case "/org/guanzon/cas/views/Section.fxml":
                return new SectionController();
            case "/org/guanzon/cas/views/Size.fxml":
                return new SizeController();
            case "/org/guanzon/cas/views/Term.fxml":
                return new TermController();
            case "/org/guanzon/cas/views/Warehouse.fxml":
                return new WarehouseController();

            /*Inventory menu*/
            case "/org/guanzon/cas/views/InventoryDetail.fxml":
                return new InventoryDetailController();
                
            /*Inventory Stock Request NEW */
             case "/org/guanzon/cas/views/InventoryRequestMotorcycle.fxml":
                return  new InventoryRequestMotorcycleController();   
                
             case "/org/guanzon/cas/views/InventoryRequestMobilephoneUnits.fxml":
                return  new InventoryRequestMobilephoneUnitsController();
                
             case "/org/guanzon/cas/views/InventoryRequestAppliances.fxml":
                return  new InventoryRequestAppliancesController();
             
             case "/org/guanzon/cas/views/InventoryRequestVehicle.fxml":
                return  new InventoryRequestVehicleController();
             
             case "/org/guanzon/cas/views/InventoryRequestSpareparts.fxml":
                return  new InventoryRequestSparepartsController();
                
            /*Inventory Stock Request NEW UI*/
             case "/org/guanzon/cas/views/InvRequestWithoutROQ.fxml":
                return  new InvRequestWithoutROQController();
            
             case "/org/guanzon/cas/views/InvRequestWithoutROQSP.fxml":
                return  new InvRequestWithoutROQSPController();
            
             case "/org/guanzon/cas/views/InvRequestWithoutROQGI.fxml":
                return  new InvRequestWithoutROQGIController();
                
             case "/org/guanzon/cas/views/InvRequestROQ.fxml":
                return  new InvRequestROQController();

             case "/org/guanzon/cas/views/InvRequestROQSP.fxml":
                return  new InvRequestROQSPController();
                
             /*Inventory Stock Request cancellation NEW UI*/    
             case "/org/guanzon/cas/views/InvRequestCancellation.fxml":
                return  new InvRequestCancellationController();
                
             case "/org/guanzon/cas/views/InvRequestCancellationSP.fxml":
                return  new InvRequestCancellationSPController();
                
             case "/org/guanzon/cas/views/InvRequestCancellationGI.fxml":
                return  new InvRequestCancellationGIController();
                
            /*PO Menu*/
            case "/org/guanzon/cas/views/PO_Quotation_Request.fxml":
                return new PO_Quotation_RequestController();
                
            case "/org/guanzon/cas/views/PO_Quotation.fxml":
                return new PO_QuotationController();
                
            case "/org/guanzon/cas/views/PurchaseOrderMC.fxml":
                return new PurchaseOrderMCController();
            case "/org/guanzon/cas/views/PurchaseOrderConfirmationMC.fxml":
                return new PurchaseOrderConfirmationMCController();
            case "/org/guanzon/cas/views/PurchaseOrderHistoryMC.fxml":
                return new PurchaseOrderHistoryMCController();
                
            case "/org/guanzon/cas/views/PurchaseOrderSP.fxml":
                return new PurchaseOrderSPController();
            case "/org/guanzon/cas/views/PurchaseOrderConfirmationSP.fxml":
                return new PurchaseOrderConfirmationSPController();
            case "/org/guanzon/cas/views/PurchaseOrderHistorySP.fxml":
                return new PurchaseOrderHistorySPController();
               
            /*History Menu*/
            
            case "/org/guanzon/cas/views/InvRequestHistory.fxml":
                return  new InvRequestHistoryController();
            
            case "/org/guanzon/cas/views/InvRequestHistorySP.fxml":
                return  new InvRequestHistorySPController();
            
            case "/org/guanzon/cas/views/InvRequestHistoryGI.fxml":
                return  new InvRequestHistoryGIController();
                
            case "/org/guanzon/cas/views/InvRequestCancellationHistory.fxml":
                return  new InvRequestCancellationHistoryController();
            
            case "/org/guanzon/cas/views/InvRequestCancellationHistorySP.fxml":
                return  new InvRequestCancellationHistorySPController();
            
            case "/org/guanzon/cas/views/InvRequestCancellationHistoryGI.fxml":
                return  new InvRequestCancellationHistoryGIController();
            
            default:
                return null;
        }
    }

    private void setScene(AnchorPane foPane) {
        workingSpace.getChildren().clear();
        workingSpace.getChildren().add(foPane);
    }

    public int checktabs(String tabtitle) {
        for (Tab tab : tabpane.getTabs()) {
            if (tab.getText().equals(tabtitle)) {
                tabpane.getSelectionModel().select(tab);
                return 0;
            }
        }
        return 1;
    }

    /*SET SCENE FOR WORKPLACE - STACKPANE - TABPANE*/
    public void setScene2(TabPane foPane) {
        workingSpace.getChildren().clear();
        workingSpace.getChildren().add(foPane);
    }

    /*MENU ACTIONS OPENING FXML's*/
    @FXML
    private void mnuClientParameterClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/ClientMasterParameter.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuClientTransactionCompanyClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/ClientMasterTransactionCompany.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuClientTransactionIndividualClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/ClientMasterTransactionIndividual.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuNewCustomerClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/NewCustomer.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterAffiliatedClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/AffiliatedCompany.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterBanksClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Banks.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterBanksBranchesClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/BanksBranches.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterBarangayClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Barangay.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterBranchClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Branch.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterBrandClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Brand.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterCategoryClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Category.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterCategoryLevel2Click(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/CategoryLevel2.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterCategoryLevel3Click(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/CategoryLevel3.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterCategoryLevel4Click(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/CategoryLevel4.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterColorClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Color.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    void mnuParameterColorDetailClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/ColorDetail.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterCompanyClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Company.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterCountryClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Country.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterDepartmentClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Department.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterInvLocationClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InventoryLocation.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterInvTypeClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InventoryType.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterLaborClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Labor.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterLaborCategoryClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/LaborCategory.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterLaborModelClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/LaborModel.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterMadeClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Made.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterMeasureClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Measure.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterModelClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Model.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterProvinceClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Province.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterRegionClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Region.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterRelationshipClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Relationship.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterSalesmanClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Salesman.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterSectionClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Section.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterSizeClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Size.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuParameterTermClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Term.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }    @FXML
    private void mnuParameterWarehouseClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/Warehouse.fxml";
        //check if oApp is not null before calling loadAnimate
        if (oApp != null && checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuAccountsPayable_Clicked(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/FrmAccountsPayable.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuAccountsReceivable_Clicked(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/FrmAccountsReceivable.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuAccountsAccreditation_Clicked(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/FrmAccountsAccreditation.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuInventoryParamClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InventoryParam.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuInventoryClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InventoryDetail.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    /*STOCK REQUEST UI*/
    @FXML
    void mnuInvRequestMotorcycleClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InventoryRequestMotorcycle.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }

    }
    @FXML
    void mnuInvRequestMobileUnitsClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InventoryRequestMobilephoneUnits.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }

    }
    @FXML
    void mnuInvRequestAppliancesClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InventoryRequestAppliances.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }

    }
    @FXML
    void mnuInvRequestVehicleClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InventoryRequestVehicle.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }

    }
    
    @FXML
    void mnuInvRequestSpareparttsClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InventoryRequestSpareparts.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    /*Stock Request NEW UI*/
    
    @FXML
    private void mnuStockRequestWithoutROQClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestWithoutROQ.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuStockRequestWithoutROQSPClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestWithoutROQSP.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuStockRequestWithoutROQGIClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestWithoutROQGI.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuStockRequestROQClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestROQ.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuStockRequestROQSPClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestROQSP.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuStockRequestROQGIClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestROQGI.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    /*Stock Request Cancellation NEW UI*/
    @FXML
    private void mnuStockRequestCancellationClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestCancellation.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuStockRequestCancellationSPClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestCancellationSP.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuStockRequestCancellationGIClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestCancellationGI.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    
    /*PO*/
    @FXML
    private void mnuPOQuotationRequestClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/PO_Quotation_Request.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    @FXML
    private void mnuPurchaseOrderMCClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/PurchaseOrderMC.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    @FXML
    private void mnuPurchaseOrderConfirmationMCClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/PurchaseOrderConfirmationMC.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    @FXML
    private void mnuPurchaseOrderHistoryMCClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/PurchaseOrderHistoryMC.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuPurchaseOrderSPClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/PurchaseOrderSP.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }    @FXML
    private void mnuPurchaseOrderConfirmationSPClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/PurchaseOrderConfirmationSP.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    @FXML
    private void mnuPurchaseOrderHistorySPClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/PurchaseOrderHistorySP.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuPOQuotationClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/PO_Quotation.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }

    @FXML
    private void mnuInventorySerialParamClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InventorySerialParam"
                + ".fxml";
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    /*Stock Request History*/
    @FXML
    private void mnuHistStockRequestClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestHistory.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuHistStockRequestSPClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestHistorySP.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuHistStockRequestGIClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestHistoryGI.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
     @FXML
    private void mnuHistStockRequestCancellationClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestCancellationHistory.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuHistStockRequestCancellationSPClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestCancellationHistorySP.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    
    @FXML
    private void mnuHistStockRequestCancellationGIClick(ActionEvent event) {
        String sformname = "/org/guanzon/cas/views/InvRequestCancellationHistoryGI.fxml";
        //check tab
        if (checktabs(SetTabTitle(sformname)) == 1) {
            setScene2(loadAnimate(sformname));
        }
    }
    private boolean showMessage() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to proceed?");
        alert.setContentText("Choose your option.");

        // Add Yes and No buttons to the alert dialog
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Show the alert and wait for a response
        javafx.scene.control.ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        // Handle the user's response
        return result == buttonTypeYes;
    }

}
