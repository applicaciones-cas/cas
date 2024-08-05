/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.cas.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.guanzon.appdriver.base.GRider;

/**
 *
 * @author Arsiela
 * unloadForm: To be called on close button on every opened tab.
 * 
 */
public class unloadForm {
    
    public enum TYPE{
        ClientMasterParameter,
        ClientMasterTransactionCompany,
        ClientMasterTransactionIndividual,
        NewCustomer,
        FrmAccountsPayable,
        FrmAccountsAccreditation,
        InventoryParam,
        InventoryDetail
    }
    
    private Object parentController;
    public String SetTabTitle(String menuaction) {
        switch (menuaction) {
            /*DIRECTORY*/
            case "/org/guanzon/cas/views/ClientMasterParameter.fxml":
                return "Client Parameter";
            case "/org/guanzon/cas/views/ClientMasterTransactionCompany.fxml":
                return "Client Transactions Company";
            case "/org/guanzon/cas/views/ClientMasterTransactionIndividual.fxml":
                return "Client Transactions Individual";
            case "/org/guanzon/cas/views/NewCustomer.fxml":
                return "Client Transactions Standard";
            case "/org/guanzon/cas/views/FrmAccountsPayable.fxml":
                return "Accounts Payable Clients";
            case "/org/guanzon/cas/views/FrmAccountsAccreditation.fxml":
                return "Accounts Accreditation";
            case "/org/guanzon/cas/views/InventoryParam.fxml":
                return "Inventory Parameter";
            case "/org/guanzon/cas/views/InventoryDetail.fxml":
                return "Inventory Details";
            default:
                return null;
        }
    }
    
    // Method to set the parent controller
    public void setParentController(Object parentController) {
        this.parentController = parentController;
    }

    
     public void useParentController(String lsValue) {
        if (parentController instanceof ClientMasterParameterController) {
            ((ClientMasterParameterController) parentController).loadReturn(lsValue);
        } else if (parentController instanceof ClientMasterTransactionCompanyController) {
            ((ClientMasterTransactionCompanyController) parentController).loadReturn(lsValue);
        }else if (parentController instanceof FrmAccountsPayableController) {
            ((FrmAccountsPayableController) parentController).loadReturn(lsValue);
        }else if (parentController instanceof FrmAccountsReceivableController) {
            ((FrmAccountsReceivableController) parentController).loadReturn(lsValue);
        }else if (parentController instanceof InventoryDetailController) {
            ((InventoryDetailController) parentController).setOverlay(false);
        }
    }
     
    public void unloadForm(AnchorPane AnchorMain, GRider oApp, String sTabTitle){
        // Get the parent of the TabContent node
        Node tabContent = AnchorMain.getParent();
        Parent tabContentParent = tabContent.getParent();

        // If the parent is a TabPane, you can work with it directly
        if (tabContentParent instanceof TabPane) {
            TabPane tabpane = (TabPane) tabContentParent;
            // Get the list of tabs in the TabPane
            ObservableList<Tab> tabs = tabpane.getTabs();
            int tabsize = tabpane.getTabs().size();
            List<String> tabName = new ArrayList<>();
//            tabName = TabsStateManager.loadCurrentTab();
            
             // Loop through the tabs and find the one you want to remove
            for (Tab tab : tabs) {
                 if (tab.getText().equals(sTabTitle)) {
                        // Remove the tab from the TabPane
                        tabs.remove(tab);
                        if (tabsize == 1) { 
                            StackPane myBox = (StackPane) tabpane.getParent();
                            myBox.getChildren().clear();
                            myBox.getChildren().add(getScene("/org/guanzon/cas/views/MainScreenBG.fxml", oApp));
                        }
                        if(tabName.size()>0){
                            tabName.remove(sTabTitle);
                        }
                    break;
                 }
            }   
        }
    }
     
     public AnchorPane getScene(String fsFormName, GRider oApp){
        ScreenInterface fxObj = new MainScreenBGController();
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
}
