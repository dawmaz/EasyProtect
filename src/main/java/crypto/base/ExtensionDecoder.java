package crypto.base;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class ExtensionDecoder extends Coder{

    protected ExtensionDecoder(File inputFile, File outputFile, String transformation, String algorithm, Key secretKey, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super(inputFile, outputFile, transformation, algorithm, secretKey, mode);
    }

    public ExtensionDecoder(Coder coder) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super(coder.inputFile, coder.outputFile, coder.transformation, coder.algorithm, coder.secretKey, coder.mode);
    }

    public void decodeAfterFirstAppearance(byte separator) throws InvalidKeyException,IOException, IllegalBlockSizeException, BadPaddingException {

        String fileExtension = decodeFileExtension(separator);
        byte[] buffer = new byte[2048];
        byte[] oneByte = new byte[1];

        String fileName = inputFile.getName().substring(0,inputFile.getName().lastIndexOf("."));
        String newOutputPath = outputFile.getPath()+File.pathSeparator+fileName+fileExtension;

        try(InputStream input = new FileInputStream(inputFile);
            OutputStream output = new FileOutputStream(newOutputPath)){

            while(input.read(oneByte)>0){
                byte[] enc = cipher.update(oneByte);
                if(enc[0]==separator)
                    break;
            }

            while(input.read(buffer)>0){
                byte[] enc = cipher.update(buffer);
                output.write(enc);
            }
        }

    }

    private String decodeFileExtension(byte separator) throws InvalidKeyException,IOException{
        cipher.init(mode, secretKey);
        List<Byte> list = new ArrayList<>();

        byte[] oneByte = new byte[1];
        String fileExtension;

        try(InputStream input = new FileInputStream(inputFile)){

            while(true){
                input.read(oneByte);
                byte[] enc = cipher.update(oneByte);
                if(enc[0]==separator)
                    break;
                list.add(enc[0]);
            }

             fileExtension = list.stream().map(String::valueOf).reduce("",(a,b)->a+b);
        }

      return fileExtension;
    }


}
