package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientModel extends Thread {
	private String serverName;
	private int port;
	
	
	Label labelBan;
	TextArea textAreaTrucTuyen;
	TextArea textAreaNoiDung;
	TextField textFieldSoanThao;
	ComboBox<String> comboBoxChonNguoiNhan;
	
	//biến cục bộ
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private int userID;

	
	


	public ClientModel(String serverName, int port, Label labelBan, TextArea textAreaTrucTuyen,
			TextArea textAreaNoiDung, TextField textFieldSoanThao, ComboBox<String> comboBoxChonNguoiNhan) {
		super();
		this.serverName = serverName;
		this.port = port;
		this.labelBan = labelBan;
		this.textAreaTrucTuyen = textAreaTrucTuyen;
		this.textAreaNoiDung = textAreaNoiDung;
		this.textFieldSoanThao = textFieldSoanThao;
		this.comboBoxChonNguoiNhan = comboBoxChonNguoiNhan;
	}

	
	@Override
	public void run() {
		try {
			socket = new Socket(serverName, port);
			System.out.println("Kết nối thành công!");
			
			//mở kênh nhập dữ liệu
			InputStream in = socket.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			bufferedReader = new BufferedReader(reader);
			
			//Mở kênh xuất dữ liệu
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(out);
			bufferedWriter = new BufferedWriter(writer);
			
			   System.out.println("Máy trạm: Mở kênh nhập xuất dữ liệu với máy chủ thành công!");
			
			//Liên tục nhập dữ liệu từ kênh
			// sau đó phân tích cú pháp của thông diệp để hiển thị lên
			nhan();
			
			socket.close();
		}catch (IOException e) {
			System.out.println("Lỗi rồi: " + e.getMessage());
			return;
		}
	}
	
	/*
	 * Nhập thông điệp từ lênh, sau đó phân tích cú pháp của thông điệp 
	 * lên màn hình máy trạm*/
	public void nhan() {
		String thongDiepNhan;
		while(true) {
			try {
				thongDiepNhan = bufferedReader.readLine();
				
				if(thongDiepNhan != null) {
					
					String[] phanTachThongDiep = thongDiepNhan.split("#~");
					
					//Phân tích "Loại" thông điệp để hiển thị lên client đúng vị trí
					//Hiển thị UserID lên labelBan
					if(phanTachThongDiep[0].equals("userID")) {
					userID = Integer.parseInt(phanTachThongDiep[2]);
					Platform.runLater(() -> {
                        labelBan.setText("Bạn: userName " + userID);
                    });
					}
					else if (phanTachThongDiep[0].equals("capNhatDSOnLine")) {
	                    capNhatDSOnline(phanTachThongDiep[2]); // Update the online user list
	                }
					//Hiển thị ra màn hình 1 userID đăng nhập hoặc đăng xuất
					else if (phanTachThongDiep[0].equals("capNhatDangNhapDangXuat")) {
						textAreaNoiDung.setText(textAreaNoiDung.getText()+ phanTachThongDiep[2] + "\n");
				
						//Hiển thị nội dung tin nhắn của một người tới cho riêng mình
					}else if (phanTachThongDiep[0].equals("guiMotNguoi")) {
						textAreaNoiDung.setText(textAreaNoiDung.getText() + "userName" + phanTachThongDiep[1] + ": " + phanTachThongDiep[2] + "\n");
						
						//Hiển thị nội dung tin nhắn của một người tới cho nhiều người
					}else if (phanTachThongDiep[0].equals("guiMoiNguoi")) {
						textAreaNoiDung.setText(textAreaNoiDung.getText() + "userName" + phanTachThongDiep[1] + " (gửi mọi người): " + phanTachThongDiep[2] + "\n");
					}
				}
			} catch (IOException e) {
				System.out.println("Lỗi rồi: " + e.getMessage());
				return;
			}
		}
	}
	
	
	/*
	 * Cập nhậ danh sach dang tực tuyến, đồng thời cập nhật ComboBox Chọn người nhân
	 * @param danhSachOnline Danh sách trực tuyến, do server gửi đến*/
	
	public void capNhatDSOnline(String danhSachOnline) {
	    String[] tachDanhSachOnline = danhSachOnline.split("-");
	    List<String> dsOnline = new ArrayList<>();
	    StringBuilder userNameOnline = new StringBuilder();
	    for (String i : tachDanhSachOnline) {
	        dsOnline.add(i);
	        userNameOnline.append("userName ").append(i).append("\n");
	    }
	    Platform.runLater(() -> {
	        textAreaTrucTuyen.setText(userNameOnline.toString());
	      
	    });
	    // Update the UI components on the JavaFX Application Thread
	    Platform.runLater(() -> {
	        comboBoxChonNguoiNhan.getItems().clear();
	        comboBoxChonNguoiNhan.setPromptText("Chọn người nhận");
	        comboBoxChonNguoiNhan.getItems().addAll("Mọi người");
	        for (String i : dsOnline) {
	            if (!i.equals("" + userID)) { // Exclude self
	                comboBoxChonNguoiNhan.getItems().add("username " + i);
	            }
	        }
	    });
	}
	
	public void gui() {
		String thongDiep = textFieldSoanThao.getText();
		String diaChiDich = comboBoxChonNguoiNhan.getValue();
		
		if(thongDiep.isBlank() || diaChiDich == null) {
			thongBao("Bạn chưa nhập thông điệp hoặc chưa chọn người nhận");
		}else {
			if(diaChiDich.equals("Mọi người")) {
				//gửi cho mọi người
				xuat("guiMoiNguoi" + "#~" + userID + "#~" + thongDiep);
				
				//Hiển thị nội dung vừa gửi lên cho chính mình
				textAreaNoiDung.setText(textAreaNoiDung.getText() + "Bạn(gửi mọi người): " + thongDiep + "\n");
				
			}else {
				//Gửi một người
				 String [] diaChiDichSplit = diaChiDich.split(" ");xuat("guiMotNguoi" + "#~" + userID + "#~" + thongDiep + "#~" + diaChiDichSplit[1]);
				 
				 //HIển thị nội dung vừa gửi lên cho chính mình
				 textAreaNoiDung.setText(textAreaNoiDung.getText() + "Bạn (gửi username " 
				 + diaChiDichSplit[1] + "):" + thongDiep + "\n");
			}
			textFieldSoanThao.setText("");
		}
	}
	
	/*Xuaast ra kên hhieen hành : Gửi đi */
	
public void xuat(String thongDiep) {
		try {
			bufferedWriter.write(thongDiep);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			System.out.println("Lỗi rồi: " + e.getMessage());
			return;
		}
	}


	
	
	
	public void thongBao(String tb) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thông báo");
		alert.setHeaderText(null);
		alert.setContentText(tb);
		alert.showAndWait();
	}
}
