package crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        encrypt(new File("E:\\Harry Potter The Complete Saga - 1080p - Yify\\HARRY-POTTER-07-1080p\\HARRY-POTTER-07-1080p\\Harry.Potter.and.the.Deathly.Hallows.Part.1.2010.MULTi.1080p.BluRay.x264.mkv"),"pass".getBytes());
    }

    public static  File encrypt(File f, byte[] key) throws Exception
    {
        System.out.println("Starting Encryption");
        Key secretKey = new SecretKeySpec(new CryptoUtils().createKey("pass"),"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);


        Path outPath = Paths.get("C:\\Users\\Dawid\\Desktop\\CVs\\Test\\decrypt.dec");
        byte[] plainBuf = new byte[2048];
        try (InputStream in = Files.newInputStream(f.toPath());
             OutputStream out = Files.newOutputStream(outPath)) {
            int nread;
            while ((nread = in.read(plainBuf)) > 0) {
                byte[] enc = cipher.update(plainBuf, 0, nread);
                out.write(enc);
            }
            byte[] enc = cipher.doFinal();
            out.write(enc);
        }
        return outPath.toFile();
    }

}


