package com.example.csc325_firebase_webview_auth.view;//package modelview;

import com.example.csc325_firebase_webview_auth.model.Person;
import com.example.csc325_firebase_webview_auth.viewmodel.AccessDataViewModel;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.firebase.cloud.StorageClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import static com.example.csc325_firebase_webview_auth.model.FirestoreContext.fireApp;
import static com.example.csc325_firebase_webview_auth.view.App.fstore;

public class AccessFBView {


     @FXML
    private TextField nameField;
    @FXML
    private TextField majorField;
    @FXML
    private TextField ageField;
    @FXML
    private Button writeButton;
    @FXML
    private Button readButton;
    @FXML
    private TextArea outputField;
    @FXML
    private ImageView profileImageView;
    @FXML
    private TableColumn<Person,Integer> colAge;
    @FXML
    private TableColumn<Person,String> colMajor;
    @FXML
    private TableColumn<Person,String> colName;
    @FXML
    public TableView<Person> dbTable;

    private boolean key;
    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;
    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }


    private FileChooser fc = new FileChooser();

    @FXML
    void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        colMajor.setCellValueFactory(new PropertyValueFactory<Person, String>("major"));
        colAge.setCellValueFactory(new PropertyValueFactory<Person, Integer>("age"));

        dbTable.setItems(listOfUsers);

        AccessDataViewModel accessDataViewModel = new AccessDataViewModel();
        nameField.textProperty().bindBidirectional(accessDataViewModel.userNameProperty());
        majorField.textProperty().bindBidirectional(accessDataViewModel.userMajorProperty());
        writeButton.disableProperty().bind(accessDataViewModel.isWritePossibleProperty().not());
    }

    @FXML
    private void addRecord(ActionEvent event) {
        addData();
    }

        @FXML
    private void readRecord(ActionEvent event) {
        readFirebase();
    }

            @FXML
    private void regRecord(ActionEvent event) {
        registerUser();
    }

     @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("/files/WebContainer.fxml");
    }

    public void addData() {

        DocumentReference docRef = fstore.collection("References").document(UUID.randomUUID().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameField.getText());
        data.put("Major", majorField.getText());
        data.put("Age", Integer.parseInt(ageField.getText()));
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }

    public boolean readFirebase(){
        key = false;
        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future =  fstore.collection("References").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                System.out.println("Outing....");
                for (QueryDocumentSnapshot document : documents)
                {
                    outputField.setText(outputField.getText()+ document.getData().get("Name")+ " , Major: "+
                            document.getData().get("Major")+ " , Age: "+
                            document.getData().get("Age")+ " \n");
                    System.out.println(document.getId() + " => " + document.getData().get("Name"));
                    person  = new Person(String.valueOf(document.getData().get("Name")),
                            document.getData().get("Major").toString(),
                            Integer.parseInt(document.getData().get("Age").toString()));
                    listOfUsers.add(person);

                    dbTable.setItems(listOfUsers);
                    //System.out.println(dbTable.getItems().size());
                    //dbTable.getItems().add(person);
                }
            }
            else
            {
               System.out.println("No data");
            }
            key=true;

        }
        catch (InterruptedException | ExecutionException ex)
        {
            outputField.setText("Could not read from database.");
            ex.printStackTrace();
        }
        return key;
    }

    public void sendVerificationEmail() {
        try {
            UserRecord user = App.fauth.getUser("name");
            //String url = user.getPassword();

        } catch (Exception e) {
        }
    }

    public boolean registerUser() {
        System.out.println("Attempting to register new user.");
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail("user@example.com")
                .setEmailVerified(false)
                .setPassword("secretPassword")
                .setPhoneNumber("+11234567890")
                .setDisplayName("John Doe")
                .setDisabled(false);

        UserRecord userRecord;
        try {
            userRecord = App.fauth.createUser(request);
            outputField.setText("Successfully created new user: " + userRecord.getUid());
            return true;

        } catch (FirebaseAuthException ex) {
            // Logger.getLogger(FirestoreContext.class.getName()).log(Level.SEVERE, null, ex);
            outputField.setText("Could not register user.");
            return false;
        }
    }

    public void aboutButton() {
        outputField.setText(outputField.getText()+"About\nWeek 06 Assignment \n(c) 2024 Andrew Kehoe & Moaath Alrajab. All rights reserved.\n");
    }

    public void closeButton() {
        System.exit(0);
    }

    public void imageSelect() throws IOException {
        FileChooser.ExtensionFilter imgFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fc.getExtensionFilters().add(imgFilter);

        File currentDir = null;
        currentDir = new File(new File(".").getCanonicalPath());
        fc.setInitialDirectory(currentDir);
        File currentFile = fc.showOpenDialog(null);

        Image profileImage = new Image(currentFile.toURI().toString());
        System.out.println(String.valueOf(currentFile));
        profileImageView.setImage(profileImage);
    }

    public void deleteButton() {
        listOfUsers.clear();
        outputField.setText("");
        dbTable.setItems(listOfUsers);
    }

    public void uploadPicButton() throws IOException {
        if(nameField.getText() == null) {
            outputField.setText(outputField.getText()+"\nPlease put in name for image upload.");
        } else {


        StorageClient storageClient = StorageClient.getInstance(fireApp);
        InputStream testFile  = new FileInputStream(profileImageView.getImage().getUrl().substring(6));
        String blobString = (nameField.getText() + ".png");

        storageClient.bucket().create(blobString, testFile, "image/png", Bucket.BlobWriteOption.userProject("csc325-78793"));
        outputField.setText(outputField.getText()+"\nYour image has been sent to Firebase.");

        }
    }

}
