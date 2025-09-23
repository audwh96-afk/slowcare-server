package com.slow.care.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.slow.care.data.Person;

@Service
public class LoginService {
private final Firestore firestore;
    
    public LoginService() {
        this.firestore = FirestoreClient.getFirestore();
    }
    
    public void saveData(String collection, String Id, Map<String, Object> dataMap) {
        try {
            firestore.collection(collection).document(Id).set(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Map<String, Object> getData(String collection, String documentId) {
        try {
            DocumentSnapshot document = firestore.collection(collection).document(documentId).get().get();
            return document.getData();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Map<String, Object> getPerson(String collection, String id) {
        try {
        	System.out.println("getperson 시작 : ");
        	
        	Query query = firestore.collection(collection)
                    .whereEqualTo("id", id)  // id 필드로 검색
                    .limit(1);
        	QuerySnapshot querySnapshot = query.get().get();
        	DocumentSnapshot document = querySnapshot.getDocuments().get(0);
            Map<String, Object> data = document.getData();
            
            System.out.println("✅ Person 검색 성공!");
            System.out.println("문서 ID: " + document.getId());
            System.out.println("데이터: " + data);
        	
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean deletePerson(String collection, String id) {
        try {
            firestore.collection(collection).document(id).delete().get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean savePerson(Person person) {
        try {
            ApiFuture<WriteResult> future =
                firestore.collection("person").document(person.getId()).set(person);
            future.get(); // 저장 완료 대기
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // LoginService
    public boolean saveImage(String collection, String id, String url) {
        try {
            System.out.println("[FIRESTORE] update start: collection=" + collection + ", docId=" + id + ", url=" + url);

            Map<String, Object> data = new HashMap<>();
            data.put("avatarUrl", url);

            ApiFuture<WriteResult> future = firestore
                    .collection(collection)
                    .document(id)              // ← 문서 ID가 'test3'인지 확인
                    .set(data, SetOptions.merge());

            WriteResult wr = future.get();
            System.out.println("[FIRESTORE] update ok: " + wr.getUpdateTime());
            return true;
        } catch (Exception e) {
            System.out.println("[FIRESTORE] update failed:");
            e.printStackTrace();
            return false;
        }
    }


    
    
}
