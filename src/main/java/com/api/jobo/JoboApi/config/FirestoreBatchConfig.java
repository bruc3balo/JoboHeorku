package com.api.jobo.JoboApi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.common.base.Function;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.io.InputStream;

import static com.api.jobo.JoboApi.globals.GlobalVariables.MESSAGES;

@Configuration
public class FirestoreBatchConfig {

    private final Logger logger = LoggerFactory.getLogger(FirestoreBatchConfig.class);
    public static Firestore firestore;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseDatabase firebaseDatabase;
   // public static boolean val = false;


    @Bean
    @Primary
    public Firestore getFireStore() {

        //InputStream serviceAccountStream = this.getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");
        InputStream serviceAccountStream = this.getClass().getClassLoader().getResourceAsStream("serviceAccountKeyApp.json");

        GoogleCredentials credentials;
        FirestoreOptions firestoreOptions = null;
        FirebaseOptions options;
        try {
            if (serviceAccountStream != null) {
                credentials = GoogleCredentials.fromStream(serviceAccountStream);
                options = FirebaseOptions.builder().setCredentials(credentials).build();

                logger.info("CONFIGURING FIRESTORE");

                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
                logger.info("Firebase Initialized");

                firestoreOptions = FirestoreOptions.newBuilder().setCredentials(credentials).build();

                firestore = FirestoreClient.getFirestore();
                logger.info("Firestore Initialized");

                firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp,"https://jobo-98e96-default-rtdb.europe-west1.firebasedatabase.app");
                logger.info("Realtime database Initialized");

                firebaseAuth = FirebaseAuth.getInstance();
                logger.info("Firebase Auth Initialized");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return firestoreOptions != null ? firestoreOptions.getService() : null;
    }



}
