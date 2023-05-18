package com.jooany.letsdeal.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init(){
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket("letsdeal-bucket")
//                .key("letsdeal-fd6ec-firebase-adminsdk-yev0p-967561ecfa.json")
//                .build();
//
//        ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(getObjectRequest);

        InputStream serviceAccount = FirebaseConfig.class.getClassLoader().getResourceAsStream("letsdeal-fd6ec-firebase-adminsdk-yev0p-967561ecfa.json");

        try{
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
