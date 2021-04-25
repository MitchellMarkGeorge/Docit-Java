package services;
import java.security.MessageDigest;

import services.interfaces.IHashService;

public class HashService implements IHashService {
    
    @Override
    public String hashBuffer(byte[] buffer) {
      
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
             byte[] result = messageDigest.digest(buffer);
             return bytesToHex(result);
        } catch (Exception e) {
            //TODO: handle exception
            return "failed";
        }

        
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
