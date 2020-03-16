package com.example.week11.repo;

import androidx.annotation.Nullable;

import com.example.week11.MapsActivity;
import com.example.week11.model.MyLocation;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseRepo {

    public static List<MyLocation> locations = new ArrayList<>();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static MapsActivity mapsActivity;
    private static String path = "mylocations";

    public static void setMapsActivity(MapsActivity activity) {
        mapsActivity = activity;
        startListener();
    }

    public static void addMarker(String lat, String lon) {
        DocumentReference ref = db.collection(path).document();
        Map<String, String> map = new HashMap<>();
        map.put("lat", lat);
        map.put("lon", lon);
        ref.set(map);
    }

    private static void startListener() {
        db.collection(path).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot data, @Nullable FirebaseFirestoreException e) {
                locations.clear();
                for (DocumentSnapshot snap : data.getDocuments()) {
                    MyLocation location = new MyLocation(snap.get("lat").toString(), snap.get("lon").toString());
                    locations.add(location);
                }
                mapsActivity.updateMarkers();
            }
        });
    }

}
