package com.example.csc325_firebase_webview_auth.view;


import com.example.csc325_firebase_webview_auth.model.FirestoreContext;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.firebase.cloud.StorageClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Firestore fstore;
    public static FirebaseAuth fauth;
    public static Scene scene;
    private final FirestoreContext contxtFirebase = new FirestoreContext();

    @Override
    public void start(Stage primaryStage) throws Exception {
        fstore = contxtFirebase.firebase();
        fauth = FirebaseAuth.getInstance();
        //scene = new Scene(loadFXML("/files/AccessFBView.fxml"));
        scene = new Scene(loadFXML("/files/SplashScreen.fxml"));
        primaryStage.setScene(scene);
        try {
            scene.getStylesheets().add(getClass().getResource("/files/main.css").toExternalForm());
        } catch (NullPointerException e) {
            System.out.println("Failed to load css.\n" + e.getMessage());
        }

        primaryStage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml ));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

}
