package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static volatile XuLy xuLy;
	// Biến cục bộ
	private ServerSocket serverSocket = null;
	
	private ThreadNhapXuatServer threadNhapXuat;
	// Khởi dựng
	public Server(int port) throws IOException{
		xuLy = new XuLy();
		try {
			serverSocket = new ServerSocket(port);//port > 1023
	
		} catch (IOException e) {
			System.out.println("Lỗi rồi: (1)" + e.getMessage());
		}
	}
	
	//phuơng thức
		public void chay() {
			int userID = 1;
			Socket incomingSocket;
			try {
				while (true) {
				
					//Máy chủ mở cổng và chờ máy trạm kết nối vào
					System.out.println("Máy chủ: " + "Đang chờ máy trạm đăng nhập ... ");
					incomingSocket = serverSocket.accept();
					System.out.println("Máy chủ: Đã có máy trạm" + incomingSocket.getRemoteSocketAddress() + "Kết nối vào máy chủ" + incomingSocket.getLocalSocketAddress());
					//tạo therad nhập xuất để quản lý
					//thêm thread
					//tạo thread nhập xuất để quản lý từng thread
					threadNhapXuat = new ThreadNhapXuatServer(incomingSocket, userID++);
					threadNhapXuat.start();
					
					//thêm thread nhập xuất của từng cline vào xử lý để quản lý theo list
					xuLy.themVao(threadNhapXuat);
					System.out.println("May chu: so thread dang chay la: " + xuLy.getKichThuoc());
					}
				} catch (IOException e) {
						System.out.println("Lỗi rồi (2): " + e.getMessage());
					}
					finally {
						try {
						serverSocket.close();
				}	catch (IOException e) {
					System.out.println("Lỗi rồi (3): " + e.getMessage());
				}
			}
		}
	
}
