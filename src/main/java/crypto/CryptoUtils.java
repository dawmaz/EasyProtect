package crypto;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;


public class CryptoUtils {

     private static final String TRANSFORMATION = "AES";
     private static final String ALGORITHM = "AES";
     private static final String SALT = "0;pMz$31Xa€yV";

     private static final String SEPARATOR = String.valueOf(File.separatorChar);
     private static final String ENCRYPTED_EXTENSION= ".DEF";
     private static final String EXTENSION_SEPARATOR = " ";

     private Cipher cipher;

     public enum Mode {
         ENCODE,DECODE;
     }

    public void encrypt(String password, File inputFile, File outputFile) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        initCipher(password,Cipher.ENCRYPT_MODE);

        byte [] inputBytes = createBytesFromFile(inputFile,Cipher.ENCRYPT_MODE);
        byte [] outputBytes = cipher.doFinal(inputBytes);

        String fileName = inputFile.getName().substring(0,inputFile.getName().lastIndexOf("."));
        File finalOutFile = new File(outputFile.getPath()+SEPARATOR+fileName+ENCRYPTED_EXTENSION);

        writeToFile(finalOutFile,outputBytes);
    }

    public void decrypt(String password, File inputFile, File outputFile) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        initCipher(password,Cipher.DECRYPT_MODE);

        byte [] inputBytes = createBytesFromFile(inputFile,Cipher.DECRYPT_MODE);
        byte [] outputBytes = cipher.doFinal(inputBytes);

        int separatorIndex = findExtensionSeparator(outputBytes);
        String extension = new String(Arrays.copyOfRange(outputBytes,0,separatorIndex));
        byte [] finalOutputBytes = Arrays.copyOfRange(outputBytes,separatorIndex+1,outputBytes.length);

        String fileName = inputFile.getName().substring(0,inputFile.getName().lastIndexOf("."));
        File finalOutputFile = new File(outputFile.getPath()+SEPARATOR+fileName+extension);

        writeToFile(finalOutputFile,finalOutputBytes);
    }

    private int findExtensionSeparator(byte[] array) {
        int index = 0;
        byte separator = EXTENSION_SEPARATOR.getBytes()[0];

        for (int i =0; i<array.length;i++){
            if(array[i]==separator){
                index=i;
                break;
            }
        }
        return index;
    }

    private byte[] createBytesFromFile(File inputFile, int mode) throws IOException {
        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int)inputFile.length()];
        inputStream.read(inputBytes);
        inputStream.close();
        byte[] result=null;
        if(mode==Cipher.ENCRYPT_MODE){
            byte[] addInfo = prepareExtensionInfo(inputFile);
            result = Arrays.copyOf(addInfo, addInfo.length+inputBytes.length);
            System.arraycopy(inputBytes,0,result,addInfo.length,inputBytes.length);
        }

        if(result!=null) return result;
        return inputBytes;
    }

    private byte[] prepareExtensionInfo(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        String ext = fileName.substring(index) + EXTENSION_SEPARATOR;
        return ext.getBytes();
    }

    private void writeToFile(File outputFile, byte[] outputBytes) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);
        outputStream.close();
    }

    private void initCipher(String password, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        cipher = Cipher.getInstance(TRANSFORMATION);
        Key secretKey = new SecretKeySpec(createKey(password),ALGORITHM);
        cipher.init(mode,secretKey);
    }

    public static byte[] createKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), 65536, 256);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return f.generateSecret(spec).getEncoded();
    }
}
