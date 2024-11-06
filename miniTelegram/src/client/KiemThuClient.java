package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KiemThuClient extends Application {
	@Override
	public void start(Stage sanKhau) {
		try {
			// tải giao diện từ file fxml đã thiết kế
			Parent giaoDien = FXMLLoader.load(getClass().getResource("miniTelegram.fxml"));
			// trình diễn trên sân khấu
			Scene canhVat = new Scene(giaoDien);
			sanKhau.setTitle("Ứng Dụng - Thử Nghiệm");
			sanKhau.setScene(canhVat);
			sanKhau.show();
			sanKhau.setResizable(false);
			
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			sanKhau.setScene(scene);
//			sanKhau.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
