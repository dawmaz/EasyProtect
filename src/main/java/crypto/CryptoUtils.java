package crypto;

import crypto.base.Coder;
import crypto.base.ExtensionDecoder;
import crypto.base.ExtensionEncoder;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;


public class CryptoUtils {

     private static final String TRANSFORMATION = "AES";
     private static final String ALGORITHM = "AES";
     private static final String SALT = "0;pMz$31Xa€yV";

     private static final String ENCRYPTED_EXTENSION= ".DEF";
     private static final String EXTENSION_SEPARATOR = " ";
     
    public void encrypt(String password, File inputFile, File outputFile) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        Coder.CoderBuilder builder = Coder.getBuilder();
        Coder tempEncoder = builder.withAlgorithm(ALGORITHM)
                .withTransformation(TRANSFORMATION)
                .withInputPath(inputFile.getPath())
                .withOutputPath(outputFile.getPath())
                .withKey(new SecretKeySpec(CryptoUtils.createKey(password), ALGORITHM))
                .withMode(Cipher.ENCRYPT_MODE)
                .build();

        ExtensionEncoder encoder = new ExtensionEncoder(tempEncoder);
        encoder.encodeFirstThenProcess(prepareExtensionInfo(inputFile),ENCRYPTED_EXTENSION);
    }

    public void decrypt(String password, File inputFile, File outputFile) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Coder.CoderBuilder builder = Coder.getBuilder();
        Coder tempEncoder = builder.withAlgorithm(ALGORITHM)
                .withTransformation(TRANSFORMATION)
                .withInputPath(inputFile.getPath())
                .withOutputPath(outputFile.getPath())
                .withKey(new SecretKeySpec(CryptoUtils.createKey(password), ALGORITHM))
                .withMode(Cipher.DECRYPT_MODE)
                .build();

        ExtensionDecoder decoder = new ExtensionDecoder(tempEncoder);
        decoder.decodeAfterFirstAppearance(EXTENSION_SEPARATOR.getBytes()[0]);
    }

    private byte[] prepareExtensionInfo(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        String ext = fileName.substring(index) + EXTENSION_SEPARATOR;
        return ext.getBytes();
    }

    public static byte[] createKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), 65536, 256);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return f.generateSecret(spec).getEncoded();
    }
}
