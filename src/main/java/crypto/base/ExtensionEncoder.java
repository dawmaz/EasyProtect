package crypto.base;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class ExtensionEncoder extends Coder{

    protected ExtensionEncoder(File inputFile, File outputFile, String transformation, String algorithm, Key secretKey, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super(inputFile, outputFile, transformation, algorithm, secretKey, mode);
    }

    public ExtensionEncoder(Coder coder) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super(coder.inputFile, coder.outputFile, coder.transformation, coder.algorithm, coder.secretKey, coder.mode);
    }

    @Override
    public void process() throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        super.process();
    }
}
