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
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable {
    private GRider oApp;
    private Stage stage;

    @FXML
    private Button btnClose, btnMinimize;

    @FXML
    private MenuItem mnuClientParameter;

    @FXML
    private MenuItem mnuClientTransaction;

    @FXML
    private TabPane tabpane;
    @FXML
    private StackPane workingSpace;

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
        try {
            // Execute query using the initialized GRider object
            ResultSet name;
            String lsQuery = "SELECT b.sCompnyNm "
                    + " FROM xxxSysUser a"
                    + " LEFT JOIN Client_Master b"
                    + " ON a.sEmployNo  = b.sClientID"
                    + " WHERE a.sUserIDxx = " + SQLUtil.toSQL(oApp.getUserID());
            name = oApp.executeQuery(lsQuery);

            // Process query result
            if (name != null && name.next()) {
                AppUserInfo.setText(name.getString("sCompnyNm") + " || " + oApp.getDepartment());
                System.out.println(oApp.getEmployeeLevel() + "  " + oApp.getDepartment());
                System.setProperty("user.name", name.getString("sCompnyNm"));
            }
        } catch (SQLException ex) {
            // Handle SQL exception
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTabPane() {
        // Implement your tab pane setup here
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
                    if (stage != null) {
                        stage.setIconified(true); // Try to minimize the stage
                    } else {
                        System.err.println("Stage is not properly initialized.");
                    }
//                    stage.setIconified(true);
//                    stage.initStyle(StageStyle.DECORATED);
                    break;
                // Add more cases for other buttons if needed
            }
        }
    }
    
    private AnchorPane loadAnimate(String fsFormName){
        ScreenInterface fxObj = getController(fsFormName);
        fxObj.setGRider(oApp);
       
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxObj.getClass().getResource( fsFormName));
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
    private ScreenInterface getController(String fsValue){
        switch (fsValue){
            case "/com/rmj/guanzongroup/cas/maven/views/ClientMasterParameter.fxml":
                return new ClientMasterParameterController();
            default:
            return null;
        }
    }
    private void setScene(AnchorPane foPane){
        workingSpace.getChildren().clear();
        workingSpace.getChildren().add(foPane);
    }
    @FXML
    private void mnuClientParameterClick(ActionEvent event) {
        setScene(loadAnimate("/com/rmj/guanzongroup/cas/maven/views/ClientMasterParameter.fxml"));
    } 
}

