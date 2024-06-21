package com.example.csc325_firebase_webview_auth.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SplashScreen {
    @FXML
    private void gotoStart(ActionEvent e){
        try {
            App.setRoot("/files/AccessFBView.fxml");
        } catch (IOException ex) {
            Logger.getLogger(WebContainerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
