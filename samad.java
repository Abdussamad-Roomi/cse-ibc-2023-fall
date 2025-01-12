import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

class CryptoHashCalculator {
    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        BigInteger privateKey = Keys.createEcKeyPair().getPrivateKey();
        BigInteger publicKey = Sign.publicKeyFromPrivate(privateKey);
        String compressedPublicKey = compressPublicKey(publicKey);

        String sha256Hash = calculateSHA256Hash(compressedPublicKey);
        System.out.println("SHA-256 Hash: " + sha256Hash);

        String ripemd160Hash = calculateRIPEMD160Hash(sha256Hash);
        System.out.println("RIPEMD160 Hash: " + ripemd160Hash);
    }

    public static String compressPublicKey(BigInteger publicKey) {
        String publicKeyPrefix = publicKey.testBit(0) ? "03" : "02";
        String publicKeyHex = publicKey.toString(16);
        String publicKeyX = publicKeyHex.substring(0, 64);
        return publicKeyPrefix + publicKeyX;
    }

    public static String calculateSHA256Hash(String input) {
        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            byte[] inputBytes = hexStringToByteArray(input);
            byte[] sha256HashBytes = sha256Digest.digest(inputBytes);
            return bytesToHex(sha256HashBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String calculateRIPEMD160Hash(String input) {
        try {
            RIPEMD160Digest ripemd = new RIPEMD160Digest();
            byte[] inputBytes = hexStringToByteArray(input);
            byte[] ripemdHashBytes = new byte[ripemd.getDigestSize()];
            ripemd.update(inputBytes, 0, inputBytes.length);
            ripemd.doFinal(ripemdHashBytes, 0);
            return bytesToHex(ripemdHashBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] hexStringToByteArray(String input) {
        int len = input.length();
        byte[] output = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            output[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                    + Character.digit(input.charAt(i + 1), 16));
        }
        return output;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}