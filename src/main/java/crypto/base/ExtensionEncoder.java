package crypto.base;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ExtensionEncoder extends Coder{

    protected ExtensionEncoder(File inputFile, File outputFile, String transformation, String algorithm, Key secretKey, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        super(inputFile, outputFile, transformation, algorithm, secretKey, mode);
    }

    public ExtensionEncoder(Coder coder) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        super(coder.inputFile, coder.outputFile, coder.transformation, coder.algorithm, coder.secretKey, coder.mode);
    }

    public void encodeFirstThenProcess(byte[] firstPart, String extension) throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(mode, secretKey);

        byte[] buffer = new byte[2048];

        String fileName = inputFile.getName().substring(0,inputFile.getName().lastIndexOf("."));
        String newOutputPath = outputFile.getPath()+File.separator+fileName+extension;

        try(InputStream input = new FileInputStream(inputFile);
            OutputStream output = new FileOutputStream(newOutputPath)){

            byte [] first = cipher.update(firstPart,0,firstPart.length);
            output.write(first);

            int len;

            while((len=input.read(buffer))>0){
                byte[] enc = cipher.update(buffer,0,len);
                output.write(enc);
            }
            byte[] finalBytes = cipher.doFinal();
            output.write(finalBytes);
        }
    }
}
