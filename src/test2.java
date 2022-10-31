import java.sql.*;
import java.util.Scanner;

public class test2 {
	public static void main(String args[]){
		Scanner scanner = new Scanner(System.in);	
		int input;
		
		while(true) {
			System.out.println("���Ͻô� �޴����� �����ϼ��� : \n");		//�޴��� ����
			System.out.println("1. ����\t2. ����\t3. �˻�\t4. ��ü����\t0. ����");
			String bookname = null;		//����ڷκ��� �Է¹��� å�̸�
			String publisher = null;	//����ڷκ��� �Է¹��� ���ǻ�
			int bookid = 0;				//����ڷκ��� �Է¹��� å��ȣ
			int price = 0;				//����ڷκ��� �Է¹��� å����
			input = scanner.nextInt();	//����� �޴����ȣ ����
			
			if(input == 1) {		//1�� ���� �� �̸�,���ǻ�,������ �Է¹ް� �ش� ������ å �����ͺ��̽��� ����
				System.out.println("�߰��ϰ��� �ϴ� å �̸��� �Է����ּ���\n");
				bookname = scanner.next();
				System.out.println("�߰��ϰ��� �ϴ� å�� ���ǻ縦 �Է����ּ���\n");
				publisher = scanner.next();
				System.out.println("�߰��ϰ��� �ϴ� å�� ������ �Է����ּ���\n");
				price = scanner.nextInt();
				insert(bookname, publisher, price);
			}
			else if(input == 2) {	//2�� ���� �� å��ȣ�� �Է¹ް� �ش� ��ȣ�� å �����ͺ��̽����� ����
				System.out.println("�����ϰ��� �ϴ� å�� ��ȣ�� �Է����ּ���\n");
				bookid = scanner.nextInt();
				delete(bookid);
			}
			else if(input == 3) {	//3�� ���� �� å�̸��� �Է¹ް� �����ͺ��̽����� �ش� �̸��� å ����
				System.out.println("ã�����ϴ� å �̸��� �Է����ּ���\n");
				bookname = scanner.next();
				if(bookname != null) {
					research(bookname);
				}
			}
			else if(input == 4) {	//4�� ���� �� �����ͺ��̽��� ��� ������ ����
				try{
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://192.168.142.3:4567/madang","root",
							"1234");
					Statement stmt=con.createStatement();
					ResultSet rs=stmt.executeQuery("SELECT * FROM Book");
					while(rs.next())
						System.out.println(rs.getInt(1)+" "+rs.getString(2)+
								" "+rs.getString(3)+ " "+rs.getInt(4) + "��");
					con.close();
				}catch(Exception e){ System.out.println(e);}
			}
			else if(input == 0) {	//0�� �Է� �� ���α׷� ����
				System.out.println("���α׷��� �����մϴ�.");
				scanner.close();
				break;
			}
			else {
				System.out.println("�ȹٷ� �Է����ּ���.");
			}
		}		
	}
	
	//���� �޼ҵ�
	public static void insert(String bookname, String publisher, int price) {
		int bookid = getfinalID();		//������ ������ å ��ȣ�� ���Ϲ޾ƿ�
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.142.3:4567/madang","root",	//��Ʈ���� �� ��񿬰�
					"1234");
			String sql = "INSERT INTO Book VALUES(?,?,?,?)";	//�����غ�
			PreparedStatement pstmt = con.prepareStatement(sql);	//�������ఴü����
			pstmt.setInt(1, bookid+1);		//?�� �� ����(���Ϲ��� å��ȣ�� 1���ؼ� ����)
			pstmt.setString(2, bookname);	
			pstmt.setString(3, publisher);
			pstmt.setInt(4, price);
			pstmt.executeUpdate();			//��������
			con.close();
		}catch (Exception e) {
			System.out.println("����");
		}
	}
	
	//���� �޼ҵ�
	public static void delete(int bookid) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.142.3:4567/madang","root",	//��Ʈ���� �� ��񿬰�
					"1234");
			String sql = "DELETE FROM Book WHERE bookid = ?";		//�����غ�
			PreparedStatement pstmt = con.prepareStatement(sql);	//�������ఴü ����

			pstmt.setInt(1, bookid);								//?�� �� ����
			pstmt.executeUpdate();									//��������
			
			System.out.println("�ش� ��ȣ�� ���� �����Ϸ�");	
			con.close();
		}catch (Exception e) {
			System.out.println("����");
		}
	}
	
	//�˻� �޼ҵ�
	public static void research(String bookname) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.142.3:4567/madang","root",
					"1234");
			String sql = "SELECT * FROM Book WHERE bookname LIKE ?";	//�����غ�
			PreparedStatement pstmt = con.prepareStatement(sql);		//�������ఴü ����

			pstmt.setString(1, "%" + bookname + "%");					//?�� ������(�ش� �̸��� ���� Ʃ�õ� ������ �� ���)

			ResultSet rs = pstmt.executeQuery();						//���������Ͽ� rs�� ����� �����͸� �����س���

			while(rs.next()) {											//����� �����͸�ŭ ���鼭
				System.out.println(rs.getInt(1)+" "+rs.getString(2)+	//Ʃ�õ� ���
						" "+rs.getString(3) + " "+rs.getInt(4) + "��");
			}
			con.close();
		}catch (Exception e) {
			System.out.println("����");
		}
		
		System.out.println("�˻��Ϸ�");
	}
	
	//��������ȣ ���� �޼ҵ�
	public static int getfinalID() {
		int bookid = 0;
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.142.3:4567/madang","root",
					"1234");
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT bookid FROM Book ORDER BY bookid DESC limit 1");	//��������ȣ�� �޾ƿ��� ����
			if(rs.next())
				bookid = rs.getInt(1);			
			con.close();
		}catch(Exception e){ System.out.println(e);}
		
		return bookid;		//�޾ƿ� å ��ȣ ����
	}
}
