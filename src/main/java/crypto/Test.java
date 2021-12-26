package crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        CryptoUtils utils= new CryptoUtils();
        //utils.encrypt("1234",new File("C:\\Users\\Dawid\\eclipse-workspace2\\EasyProtect\\Dawid_CV_U.docx"),new File("C:\\Users\\Dawid\\eclipse-workspace2\\EasyProtect\\Dawid_CV_U.enc"));
        //utils.decrypt("1234",new File("C:\\Users\\Dawid\\eclipse-workspace2\\EasyProtect\\Dawid_CV_U.enc"),new File("C:\\Users\\Dawid\\eclipse-workspace2\\EasyProtect\\src"));

      //  int[] array = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
       // int index = 3;

       // System.out.println(Arrays.toString( Arrays.copyOfRange(array,1,array.length)));
        File file =new File("C:\\Users\\Dawid\\eclipse-workspace2\\EasyProtect\\Dawid_CV_U.docx");
        System.out.println(file.getName());
        System.out.println(file.getPath());
        System.out.println(file.getAbsoluteFile());
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getCanonicalFile());
        System.out.println(file.getCanonicalPath());
        System.out.println(file.getParentFile());
    }

}
