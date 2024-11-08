package client;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientController implements Initializable {
    @FXML
    private Label labelBan;
    @FXML
    private TextArea textAreaTrucTuyen;
    @FXML
    private TextArea textAreaNoiDung;
    @FXML
    private TextField textFieldSoanThao; // Chuyển từ TextArea sang TextField cho đúng
    @FXML
    private ComboBox<String> comboBoxChonNguoiNhan;

    private String serverName = "localhost";
    private int port = 3333;
    private ClientModel t;

    @Override
    public void initialize(URL url, ResourceBundle res) {
        ketNoiMayChu();
    }

    public void ketNoiMayChu() {
        t = new ClientModel(serverName, port, labelBan, textAreaTrucTuyen, textAreaNoiDung, textFieldSoanThao, comboBoxChonNguoiNhan);
        t.start();
    }

    @FXML
    public void hanhDongGui(ActionEvent event) {
        t.gui();
    }
}
