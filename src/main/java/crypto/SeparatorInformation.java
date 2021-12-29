package crypto;

public class SeparatorInformation {

    private final String  extension;
    private final  byte[] array;

    public SeparatorInformation(String extension, byte[] array) {
        this.extension = extension;
        this.array = array;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getArray() {
        return array;
    }

}
