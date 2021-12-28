package crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws Exception {

        encrypt(new File("C:\\Users\\Dawid\\Desktop\\CVs\\Test\\Dawid_CV.pdf"),"pass".getBytes());
        decrypt(new File("C:\\Users\\Dawid\\Desktop\\CVs\\Test\\decrypt.dec"),"pass".getBytes());
    }

    public static  void  encrypt(File f, byte[] key) throws Exception
    {
        System.out.println("Starting Encryption");
        Key secretKey = new SecretKeySpec(new CryptoUtils().createKey("pass"),"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        System.out.println(cipher);

        String outPath = "C:\\Users\\Dawid\\Desktop\\CVs\\Test\\decrypt.dec";
        byte[] plainBuf = new byte[1];

        try (InputStream in = new FileInputStream(f.getPath());
             OutputStream out = new FileOutputStream(outPath)) {

            while ((in.read(plainBuf)) > 0) {
                byte[] enc = cipher.update(plainBuf);
                out.write(enc);
            }
           // byte[] enc = cipher.doFinal();
           // out.write(enc);
        }
    }

    public static  void  decrypt(File f, byte[] key) throws Exception
    {
        System.out.println("Starting Encryption");
        Key secretKey = new SecretKeySpec(new CryptoUtils().createKey("123"),"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        System.out.println(cipher);
        String outPath = "C:\\Users\\Dawid\\Desktop\\CVs\\Test\\decrypt.pdf";
        byte[] plainBuf = new byte[1];

        try (InputStream in = new FileInputStream(f.getPath());
             OutputStream out = new FileOutputStream(outPath)) {

            while ((in.read(plainBuf)) > 0) {
                byte[] enc = cipher.update(plainBuf);
                out.write(enc);
            }
        }
    }

}


