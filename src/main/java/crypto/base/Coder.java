package crypto.base;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Coder {
    private final File inputFile;
    private final File outputFile;
    private final String transformation;
    private final String algorithm;
    private final Key secretKey;
    private final int mode;

    private final Cipher cipher;

    private Coder(File inputFile,File outputFile,String transformation,String algorithm, Key secretKey, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.transformation = transformation;
        this.algorithm = algorithm;
        this.secretKey = secretKey;
        this.mode = mode;
        cipher = Cipher.getInstance(transformation);
    }

    public void process() throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] buffer = new byte[2048];

        try(InputStream input = new FileInputStream(inputFile);
            OutputStream output = new FileOutputStream(outputFile)){

            while(input.read(buffer)>0){
                byte[] enc = cipher.update(buffer);
                output.write(enc);
            }

            byte[] last = cipher.doFinal();
            output.write(last);
        }
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public String getTransformation() {
        return transformation;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getMode() {
        return mode;
    }

    public static CoderBuilder getBuilder(){
        return new CoderBuilder();
    }

    public static class CoderBuilder{

        private File inputFile;
        private File outputFile;
        private String transformation;
        private String algorithm;
        private Key secretKey;
        private int mode;


        public CoderBuilder withInputPath(String inputPath){
            inputFile = new File(inputPath);
            return this;
        }

        public CoderBuilder withOutputPath(String outputPath){
            outputFile = new File(outputPath);
            return this;
        }

        public CoderBuilder withTransformation(String transformation){
            this.transformation = transformation;
            return this;
        }

        public CoderBuilder withAlgorithm(String algorithm){
            this.algorithm = algorithm;
            return this;
        }

        public CoderBuilder withKey(Key secretKey){
            this.secretKey = secretKey;
            return this;
        }

        public CoderBuilder withMode(int mode){
            this.mode = mode;
            return this;
        }

        public Coder build() throws NoSuchPaddingException, NoSuchAlgorithmException {
            verify();
            return new Coder(inputFile,outputFile,transformation,algorithm,secretKey,mode);
        }

        private void verify() {
            if(inputFile==null) throw new IllegalStateException("Missing input file");
            if(outputFile==null) throw new IllegalStateException("Missing output file");
            if(transformation==null) throw new IllegalStateException("Missing transformation");
            if(algorithm==null) throw new IllegalStateException("Missing algorithm");
            if(secretKey==null) throw new IllegalStateException("Missing secret key");
            if(mode!= Cipher.ENCRYPT_MODE&&mode!=Cipher.DECRYPT_MODE) throw new IllegalStateException("Incorrect mode provided");
        }


    }




}
