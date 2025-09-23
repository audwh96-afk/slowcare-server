package com.slow.care.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.slow.care.data.Person;
import com.slow.care.service.LoginService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/slow")
@CrossOrigin("*")
public class MainController {
	@Autowired
    private LoginService firebaseService;
	
	@GetMapping("/person/{id}")
    public ResponseEntity<Map<String, Object>> getPerson(@PathVariable String id) {
        Map<String, Object> data = firebaseService.getPerson("person", id);
        
        if (data != null) {
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@DeleteMapping("/person/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        boolean deleted = firebaseService.deletePerson("person", id); 
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
	
	@PostMapping("/signup")
	public ResponseEntity<Void> signup(@RequestBody Person person) {
	    // Firebase 또는 DB에 사용자 저장
	    firebaseService.savePerson(person);
	    return ResponseEntity.ok().build();
	}
	
	private final Path uploadBase = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
	
	// MainController
	@PostMapping("/upload/{id}")
	public ResponseEntity<Map<String,String>> uploadImage(
	        @PathVariable String id,
	        @RequestParam("image") MultipartFile file) {
	    try {
	        String bucket = System.getenv("GCS_BUCKET"); // 예: slowcare-d48ff.appspot.com
	        String objectName = "avatars/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

	        Storage storage = StorageOptions.getDefaultInstance().getService();

	        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, objectName))
	                .setContentType(file.getContentType())
	                .build();

	        // 🎯 공개 읽기(퍼블릭)로 업로드
	        storage.create(
	            blobInfo,
	            file.getBytes(),
	            Storage.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ)
	        );

	        String url = String.format("https://storage.googleapis.com/%s/%s", bucket, objectName);

	        boolean ok = firebaseService.saveImage("person", id, url);
	        if (!ok) return ResponseEntity.status(500).body(Map.of("error", "firestore update failed"));

	        return ResponseEntity.ok(Map.of("url", url));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
	    }
	}
	
	
}
