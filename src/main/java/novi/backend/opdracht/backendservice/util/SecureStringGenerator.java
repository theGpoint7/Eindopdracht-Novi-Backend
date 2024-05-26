package novi.backend.opdracht.backendservice.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureStringGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getEncoder(); // Use standard Base64 encoder

    public static String generateSecureKey(int length) {
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static void main(String[] args) {
        String secretKey = generateSecureKey(32); // 32 bytes = 256 bits
        System.out.println("Generated Secret Key: " + secretKey);
    }
}
