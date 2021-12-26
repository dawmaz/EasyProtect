package sample;


import crypto.CryptoUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    public Label passwordLabel;
    public Label fileLabel;
    public Label outputLabel;
    public PasswordField passwordField;
    public TextField passwordTextField;
    public TextField fileTextField;
    public TextField outputTextField;
    public CheckBox passwordCheckBox;
    public CheckBox outputCheckBox;

    private File lastDirectoryUsed ;

    public void selectFile(){
        if(fileTextField.getText().isEmpty()){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a file...");

            if (lastDirectoryUsed!=null)
                fileChooser.setInitialDirectory(lastDirectoryUsed);

            File file = fileChooser.showOpenDialog(new Stage());
            if(file!=null){
                fileTextField.setText(file.getPath());
                lastDirectoryUsed = file.getParentFile();
            }
        }
    }

    public void selectPath(){
        if(outputTextField.getText().isEmpty()){
            DirectoryChooser directory = new DirectoryChooser();
            directory.setTitle("Choose a path...");

            if (lastDirectoryUsed!=null)
                directory.setInitialDirectory(lastDirectoryUsed);

            File file = directory.showDialog(new Stage());
            if(file!=null){
                outputTextField.setText(file.getPath());
                lastDirectoryUsed =file;
            }
        }
    }

    public void encrypt() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        process(CryptoUtils.Mode.ENCODE);
    }

    public void decrypt() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        process(CryptoUtils.Mode.DECODE);
    }

    private void process(CryptoUtils.Mode mode) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        CryptoUtils cu = new CryptoUtils();
        String outputPath = prepareOutputPath();

        switch(mode){
            case DECODE:{
                cu.decrypt(passwordField.getText(),new File(fileTextField.getText()),new File(outputPath));
            }
            case ENCODE:{
                cu.encrypt(passwordField.getText(),new File(fileTextField.getText()),new File(outputPath));
            }
        }
    }

    private String prepareOutputPath() {
        if(outputTextField.getText().isEmpty()||!outputCheckBox.isSelected()){
            File tempFile = new File(fileTextField.getText());
            return tempFile.getParent();
        }
        return outputTextField.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        outputTextField.disableProperty().bind(outputCheckBox.selectedProperty().not());
        outputLabel.disableProperty().bind(outputCheckBox.selectedProperty().not());

        passwordField.visibleProperty().bind(passwordCheckBox.selectedProperty().not());
        passwordTextField.visibleProperty().bind(passwordCheckBox.selectedProperty());
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());

    }
}
