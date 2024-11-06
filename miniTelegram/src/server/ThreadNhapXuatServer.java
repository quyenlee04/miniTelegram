package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ThreadNhapXuatServer extends Thread{
	private Socket socket;
    private int userID;
    
    // Biến cục bộ của lớp
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    
    public ThreadNhapXuatServer(Socket socket, int userID) {
        this.socket = socket;
        this.userID = userID;
    }
    
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    //co
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }
    
    
    @Override
    public void run() {
        try {
            // Mở kênh nhập dữ liệu - nhận
            InputStream in = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(reader);
            
            // Mở kênh xuất dữ liệu - gửi
            OutputStream out = socket.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            bufferedWriter = new BufferedWriter(writer);
            System.out.println("Máy chủ: Mở kênh nhập/xuất dữ liệu với userID: " + userID + " thành công");

            Server.xuLy.guiUserIDChoClient(userID);
            
            Server.xuLy.guiDanhSachUserDangOnLine();
            
            Server.xuLy.guiMoiNguoi("capNhatDangNhapDangXuat" + "#~" +"server" + "#~" + "***username" + userID + "da dang nhap***");
            
            String thongDiep;
            while(true) {
            	thongDiep = nhap();
            	if(thongDiep != null)
            		Server.xuLy.chuyenTiepThongDiep(thongDiep, getUserID());
            }
            
            
        } catch(IOException e) {
            Server.xuLy.loaiRa(userID);
            System.out.println("May chu: May tram " + userID + "đã thoát;" + "số thread dang chạy là: " + Server.xuLy.getKichThuoc());
            
            Server.xuLy.guiDanhSachUserDangOnLine();
            
            Server.xuLy.guiMoiNguoi("capNhatDangNhapDangXuat" + "#~" +"server" + "#~" + "***username" + userID + "đã thoát***");
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (bufferedWriter != null) bufferedWriter.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
               System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
            }
        }
    }
    
    public String nhap() throws IOException {
        return bufferedReader.readLine();
    }
    
    public void xuat(String thongDiep) throws IOException {
        bufferedWriter.write(thongDiep);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}
