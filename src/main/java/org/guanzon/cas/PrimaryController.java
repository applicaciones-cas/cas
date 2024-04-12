package org.guanzon.cas;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
        System.out.println("com.rmj.guanzongroup.cas.maven.PrimaryController.switchToSecondary()");
    }
    
    
}
