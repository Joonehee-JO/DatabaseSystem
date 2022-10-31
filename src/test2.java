import java.sql.*;
import java.util.Scanner;

public class test2 {
	public static void main(String args[]){
		Scanner scanner = new Scanner(System.in);	
		int input;
		
		while(true) {
			System.out.println("원하시는 메뉴얼을 선택하세요 : \n");		//메뉴얼 노출
			System.out.println("1. 삽입\t2. 삭제\t3. 검색\t4. 전체보기\t0. 종료");
			String bookname = null;		//사용자로부터 입력받을 책이름
			String publisher = null;	//사용자로부터 입력받을 출판사
			int bookid = 0;				//사용자로부터 입력받을 책번호
			int price = 0;				//사용자로부터 입력받을 책가격
			input = scanner.nextInt();	//사용자 메뉴얼번호 선택
			
			if(input == 1) {		//1번 선택 시 이름,출판사,가격을 입력받고 해당 정보의 책 데이터베이스에 삽입
				System.out.println("추가하고자 하는 책 이름을 입력해주세요\n");
				bookname = scanner.next();
				System.out.println("추가하고자 하는 책의 출판사를 입력해주세요\n");
				publisher = scanner.next();
				System.out.println("추가하고자 하는 책의 가격을 입력해주세요\n");
				price = scanner.nextInt();
				insert(bookname, publisher, price);
			}
			else if(input == 2) {	//2번 선택 시 책번호를 입력받고 해당 번호의 책 데이터베이스에서 삭제
				System.out.println("삭제하고자 하는 책의 번호를 입력해주세요\n");
				bookid = scanner.nextInt();
				delete(bookid);
			}
			else if(input == 3) {	//3번 선택 시 책이름을 입력받고 데이터베이스에서 해당 이름의 책 추출
				System.out.println("찾고자하는 책 이름을 입력해주세요\n");
				bookname = scanner.next();
				if(bookname != null) {
					research(bookname);
				}
			}
			else if(input == 4) {	//4번 선택 시 데이터베이스의 모든 데이터 추출
				try{
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://192.168.142.3:4567/madang","root",
							"1234");
					Statement stmt=con.createStatement();
					ResultSet rs=stmt.executeQuery("SELECT * FROM Book");
					while(rs.next())
						System.out.println(rs.getInt(1)+" "+rs.getString(2)+
								" "+rs.getString(3)+ " "+rs.getInt(4) + "원");
					con.close();
				}catch(Exception e){ System.out.println(e);}
			}
			else if(input == 0) {	//0번 입력 시 프로그램 종료
				System.out.println("프로그램을 종료합니다.");
				scanner.close();
				break;
			}
			else {
				System.out.println("똑바로 입력해주세요.");
			}
		}		
	}
	
	//삽입 메소드
	public static void insert(String bookname, String publisher, int price) {
		int bookid = getfinalID();		//마지막 순서의 책 번호를 리턴받아옴
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.142.3:4567/madang","root",	//포트설정 및 디비연결
					"1234");
			String sql = "INSERT INTO Book VALUES(?,?,?,?)";	//쿼리준비
			PreparedStatement pstmt = con.prepareStatement(sql);	//쿼리실행객체선언
			pstmt.setInt(1, bookid+1);		//?에 값 매핑(리턴받은 책번호에 1더해서 매핑)
			pstmt.setString(2, bookname);	
			pstmt.setString(3, publisher);
			pstmt.setInt(4, price);
			pstmt.executeUpdate();			//쿼리실행
			con.close();
		}catch (Exception e) {
			System.out.println("오류");
		}
	}
	
	//삭제 메소드
	public static void delete(int bookid) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.142.3:4567/madang","root",	//포트설정 및 디비연결
					"1234");
			String sql = "DELETE FROM Book WHERE bookid = ?";		//쿼리준비
			PreparedStatement pstmt = con.prepareStatement(sql);	//쿼리실행객체 선언

			pstmt.setInt(1, bookid);								//?에 값 매핑
			pstmt.executeUpdate();									//쿼리실행
			
			System.out.println("해당 번호의 도서 삭제완료");	
			con.close();
		}catch (Exception e) {
			System.out.println("오류");
		}
	}
	
	//검색 메소드
	public static void research(String bookname) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.142.3:4567/madang","root",
					"1234");
			String sql = "SELECT * FROM Book WHERE bookname LIKE ?";	//쿼리준비
			PreparedStatement pstmt = con.prepareStatement(sql);		//쿼리실행객체 선언

			pstmt.setString(1, "%" + bookname + "%");					//?에 값매핑(해당 이름을 가진 튜플들 존재할 시 출력)

			ResultSet rs = pstmt.executeQuery();						//쿼리실행하여 rs에 저장된 데이터를 추출해놓음

			while(rs.next()) {											//추출된 데이터만큼 돌면서
				System.out.println(rs.getInt(1)+" "+rs.getString(2)+	//튜플들 출력
						" "+rs.getString(3) + " "+rs.getInt(4) + "원");
			}
			con.close();
		}catch (Exception e) {
			System.out.println("오류");
		}
		
		System.out.println("검색완료");
	}
	
	//마지막번호 추출 메소드
	public static int getfinalID() {
		int bookid = 0;
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://192.168.142.3:4567/madang","root",
					"1234");
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT bookid FROM Book ORDER BY bookid DESC limit 1");	//마지막번호를 받아오는 쿼리
			if(rs.next())
				bookid = rs.getInt(1);			
			con.close();
		}catch(Exception e){ System.out.println(e);}
		
		return bookid;		//받아온 책 번호 리턴
	}
}
