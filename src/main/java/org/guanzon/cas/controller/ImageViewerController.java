package org.guanzon.cas.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ImageViewerController {

    private double mouseAnchorX;
    private double mouseAnchorY;
    private double scaleFactor = 1.0;

    private double xOffset = 0;
    private double yOffset = 0;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    @FXML
    private ImageView imageView;
    
    @FXML
    private AnchorPane mainPane;
   
    @FXML
    private AnchorPane draggablePane;

    @FXML
    private Button btnClose;

    @FXML
    private StackPane placeholder;

    public void initialize() {
        
        draggablePane.setOnMousePressed(this::handleMousePressed);
        draggablePane.setOnMouseDragged(this::handleMouseDragged);

        btnClose.setOnAction(event -> {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
        placeholder.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            placeholder.setClip(new javafx.scene.shape.Rectangle(
                    newBounds.getMinX(),
                    newBounds.getMinY(),
                    newBounds.getWidth(),
                    newBounds.getHeight()
            ));
        });

        imageView.setOnScroll((ScrollEvent event) -> {
            double delta = event.getDeltaY();
            scaleFactor = Math.max(0.5, Math.min(scaleFactor * (delta > 0 ? 1.1 : 0.9), 5.0));
            imageView.setScaleX(scaleFactor);
            imageView.setScaleY(scaleFactor);
        });

        imageView.setOnMousePressed((MouseEvent event) -> {
            mouseAnchorX = event.getSceneX() - imageView.getTranslateX();
            mouseAnchorY = event.getSceneY() - imageView.getTranslateY();
        });

        imageView.setOnMouseDragged((MouseEvent event) -> {
            double translateX = event.getSceneX() - mouseAnchorX;
            double translateY = event.getSceneY() - mouseAnchorY;
            imageView.setTranslateX(translateX);
            imageView.setTranslateY(translateY);
        });
    }

    @FXML
    void closeDialog(MouseEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void handleMousePressed(MouseEvent event) {
        // Capture the initial offset between the mouse and the stage
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        // Get the current stage and update its position
        Stage stage = (Stage) draggablePane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    public void setImage(String filePath) {
        Image image = new Image(filePath);
        imageView.setImage(image);
    }
}
