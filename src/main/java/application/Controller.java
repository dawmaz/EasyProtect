package application;


import crypto.CryptoUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
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

    public void encrypt()  {
        process(Cipher.ENCRYPT_MODE);
    }

    public void decrypt()  {
        process(Cipher.DECRYPT_MODE);
    }

    private void process(int mode)  {
        CryptoUtils cu = new CryptoUtils();
        String outputPath = prepareOutputPath();

        try {
            switch(mode){
                case Cipher.DECRYPT_MODE: {
                    cu.decrypt(passwordField.getText(), new File(fileTextField.getText()), new File(outputPath));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success!");
                    alert.setHeaderText("Decoding operation performed correctly");
                    alert.show();
                    break;
                }
                case Cipher.ENCRYPT_MODE:{
                    cu.encrypt(passwordField.getText(),new File(fileTextField.getText()),new File(outputPath));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success!");
                    alert.setHeaderText("Encoding operation performed correctly");
                    alert.show();
                    break;
                }
            }
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | IOException | IllegalBlockSizeException | BadPaddingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Process cannot be finished!");
            alert.setHeaderText(e.getMessage());
            alert.show();
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
