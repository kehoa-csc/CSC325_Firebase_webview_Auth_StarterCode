package com.example.csc325_firebase_webview_auth.model;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;

/**
 *
 * @author MoaathAlrajab
 */
public class FirestoreContext {
    public static FirebaseApp fireApp;
    public Firestore firebase() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(getClass().getResourceAsStream("/files/key.json")))
                    .setStorageBucket("csc325-78793.appspot.com")
                    .build();
            fireApp = FirebaseApp.initializeApp(options);
            System.out.println("Firebase is initialized");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return FirestoreClient.getFirestore();
    }


}
