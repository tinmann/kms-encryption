package com.example.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.kms.v1.CryptoKeyName;
import com.google.cloud.kms.v1.DecryptResponse;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.cloud.kms.v1.KeyManagementServiceSettings;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class KmsEncryptionService {

    private final String projectId = "test-encrypt-424804";
    private final String locationId = "us"; // or specify your location
    private final String keyRingId = "key-ring-1";
    private final String keyId = "key-1";
    private final String serviceAccountKeyPath = "C:\\Users\\sarig\\Downloads\\test-encrypt-424804-d4f0885719cf.json";

    public String encrypt(String plaintext) {
        try (KeyManagementServiceClient client = createKmsClient()) {
            CryptoKeyName keyName = CryptoKeyName.of(projectId, locationId, keyRingId, keyId);
            ByteString plaintextBytes = ByteString.copyFromUtf8(plaintext);
            ByteString ciphertext = client.encrypt(keyName, plaintextBytes).getCiphertext();
            return Base64.getEncoder().encodeToString(ciphertext.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String ciphertext) {
        try (KeyManagementServiceClient client = createKmsClient()) {
            CryptoKeyName keyName = CryptoKeyName.of(projectId, locationId, keyRingId, keyId);
            ByteString ciphertextBytes = ByteString.copyFrom(Base64.getDecoder().decode(ciphertext));
            DecryptResponse response = client.decrypt(keyName, ciphertextBytes);
            ByteString plaintext = response.getPlaintext();
            return plaintext.toStringUtf8();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private KeyManagementServiceClient createKmsClient() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountKeyPath));
        KeyManagementServiceSettings settings = KeyManagementServiceSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();
        return KeyManagementServiceClient.create(settings);
    }
}