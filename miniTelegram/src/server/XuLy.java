package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XuLy {
private List<ThreadNhapXuatServer> danhSachThreadNhapXuat;
	
	public XuLy() {
		danhSachThreadNhapXuat = new ArrayList<>();
		
	}
	
	public int getKichThuoc() {
		return danhSachThreadNhapXuat.size();
		}
	
	public void themVao(ThreadNhapXuatServer threadNhapXuat) {
		danhSachThreadNhapXuat.add(threadNhapXuat);
	}
	
	public void loaiRa(int userID) {
		for(ThreadNhapXuatServer threadNhapXuat : danhSachThreadNhapXuat) {
			if(threadNhapXuat.getUserID() == userID){
				danhSachThreadNhapXuat.remove(threadNhapXuat);
				break;
			}
		}
	}
	
	public void guiUserIDChoClient(int userID) {
		guiMotNguoi("userID" + "#~" + "server" + "#~" + userID + "#~" + userID,userID);
	}
	
	public void guiDanhSachUserDangOnLine() {
		String st = "";
		for(ThreadNhapXuatServer threadNhapXuat: danhSachThreadNhapXuat){
			st += threadNhapXuat.getUserID() + "-";
			guiMoiNguoi("capNhatDSOnLine" + "#~" + "sever" + "#~" + st);
		}
	}
	
	public void guiMoiNguoi(String thongDiep) {
		for(ThreadNhapXuatServer threadNhapXuat : danhSachThreadNhapXuat) {
			try {
				threadNhapXuat.xuat(thongDiep);
			}catch (IOException e) {
				System.out.println("Loi roi: "+ e.getMessage());
			}
		}
	}
	
	public void guiMoiNguoi(String thongDiep, int userID) {
		for(ThreadNhapXuatServer threadNhapXuat : danhSachThreadNhapXuat) {
			if(threadNhapXuat.getUserID() != userID)
				try {
				threadNhapXuat.xuat(thongDiep);
				}catch (IOException e) {
				System.out.println("Loi roi: "+ e.getMessage());
			}
		}
	}
	
	
	public void guiMotNguoi(String thongDiep, int userID) {
		for(ThreadNhapXuatServer threadNhapXuat : danhSachThreadNhapXuat)
			if(threadNhapXuat.getUserID() == userID) {
				try {
					threadNhapXuat.xuat(thongDiep);
					break;
				}catch(IOException e) {
					System.out.println("Loi roi: " + e.getMessage());
				}
			}
	}
	
	public void chuyenTiepThongDiep(String thongDiep, int userID) {
		String[] tachThongDiep = thongDiep.split("#~");
		if(tachThongDiep[0].equals("guiMoiNguoi")) {
			guiMoiNguoi("guiMoiNguoi" + "#~" + tachThongDiep[1] + "#~" + tachThongDiep[2]);
		}
		if(tachThongDiep[0].equals("guiMotNguoi")) {
			guiMotNguoi("guiMotNguoi" + "#~" + tachThongDiep[1] + "#~" + tachThongDiep[2] + "#~" + tachThongDiep[3],Integer.parseInt(tachThongDiep[3]));
		}
	}
}
