package crypto.base;

import crypto.SeparatorInformation;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ExtensionDecoder extends Coder{

    private final int bufferSize = 2048;

    protected ExtensionDecoder(File inputFile, File outputFile, String transformation, String algorithm, Key secretKey, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        super(inputFile, outputFile, transformation, algorithm, secretKey, mode);
    }

    public ExtensionDecoder(Coder coder) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        super(coder.inputFile, coder.outputFile, coder.transformation, coder.algorithm, coder.secretKey, coder.mode);
    }

    public void decodeAfterFirstAppearance(byte separator) throws InvalidKeyException,IOException, IllegalBlockSizeException, BadPaddingException {
        SeparatorInformation separatorInformation = getInfo(separator);
        byte[] buffer = new byte[bufferSize];

        String fileName = inputFile.getName().substring(0,inputFile.getName().lastIndexOf("."));
        String newOutputPath = outputFile.getPath()+File.separator+fileName+separatorInformation.getExtension();

        try(InputStream input = new FileInputStream(inputFile);
            OutputStream output = new FileOutputStream(newOutputPath)){

            input.read(buffer);
            output.write(separatorInformation.getArray());

            int len;
            while((len=input.read(buffer))>0){
                byte []enc = cipher.update(buffer,0,len);
                output.write(enc);
            }

            byte[] finalBytes = cipher.doFinal();
            output.write(finalBytes);
        }

    }

    private SeparatorInformation getInfo(byte separator) throws IOException, IllegalBlockSizeException, BadPaddingException {
        byte[] buffer = new byte[bufferSize];

        try(InputStream input = new FileInputStream(inputFile)){

                int len = input.read(buffer);
                byte[] enc = cipher.update(buffer,0,len);

                if(enc.length==0)
                    enc = cipher.doFinal();

            return getSeparatorInformation(enc,separator);
        }
    }

    private SeparatorInformation getSeparatorInformation(byte[] array, byte separator) {
        int index=0;

        for (int i =0; i<array.length;i++){
            if(array[i]==separator){
                index=i;
                break;
            }
        }

        String fileExtension = new String(Arrays.copyOf(array,index));
        byte[] arrayToWrite = new byte[array.length-index-1];
        for(int i=0;i<arrayToWrite.length;i++){
            arrayToWrite[i]=array[++index];
        }

    return new SeparatorInformation(fileExtension,arrayToWrite);
    }


}
