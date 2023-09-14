package org.example;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class WordCRUD implements ICRUD{

	final String WORD_SELECTALL = "select * from Dictionary";
	final String WORD_SELECT = "select * from Dictionary where word like ? ";
	final String WORD_INSERT = "insert into dictionary (level, word, meaning, regdate) "
			+ "values (?,?,?,?) ";
	final String WORD_UPDATE = "update dictionary set meaning=? where id=? ";
	final String WORD_DELETE = "delete from dictionary where id=? ";
	ArrayList<Word> list;
	Scanner s;
	final String fname = "Dictionary.txt";
	Connection conn;
	/*
	 * => 난이도(1,2,3) & 새 단어 입력 : 1 driveway
	 * 뜻 입력 : 차고 진입로
	 * 새 단어가 단어장에 추가되었습니다.
	 */
	
	WordCRUD(Scanner s) {
		list = new ArrayList<>();
		this.s = s;
		conn = DBConnection.getConnection();
	}

	public void loadData(String keyword) {
		list.clear();
		try {
			PreparedStatement stmt;
			ResultSet rs;

			if(keyword.equals("")) {
				stmt = conn.prepareStatement(WORD_SELECTALL);
				rs = stmt.executeQuery();
			} else {
				stmt = conn.prepareStatement(WORD_SELECT);
				stmt.setString(1, "%" + keyword + "%");
				rs = stmt.executeQuery();
			}

			int count = 0;
			while (true) {
				if (!rs.next()) break;
				int id = rs.getInt("id");
				int level = rs.getInt("level");
				String word = rs.getString("word");
				String meaning = rs.getString("meaning");
				list.add(new Word(id, level, word, meaning));
				count++;
			}
			rs.close();
			stmt.close();
			System.out.println("==> " + count + "개 로딩 완료!!!");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public String getCurrentDate() {
		LocalDate now = LocalDate.now();
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return f.format(now);
	}

	@Override
	public int add(Word one){
		int retval = 0;
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(WORD_INSERT);
			pstmt.setInt(1, one.getLevel());
			pstmt.setString(2, one.getWord());
			pstmt.setString(3, one.getMeaning());
			pstmt.setString(4, getCurrentDate());
			retval = pstmt.executeUpdate();
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retval;
	}

	@Override
	public int update(Word one) {
		int retval = 0;
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(WORD_UPDATE);
			pstmt.setString(1, one.getMeaning());
			pstmt.setInt(2, one.getId());
			retval = pstmt.executeUpdate();
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retval;
	}

	@Override
	public int delete(Word one) {
		int retval = 0;
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(WORD_DELETE);
			pstmt.setInt(1, one.getId());
			retval = pstmt.executeUpdate();
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retval;
	}

	public void addItem() {
		System.out.print("=> 난이도(1,2,3) & 새 단어 입력 : ");
		int level = s.nextInt();
		String word = s.nextLine();

		System.out.print("뜻 입력 : ");
		String meaning = s.nextLine();

		Word one = new Word(0, level, word, meaning);
		int retval = add(one);

		if(retval > 0) System.out.println("새 단어가 단어장에 추가되었습니다.");
		else System.out.println("새 단어 추가중 에러가 발생되었습니다.");
	}

	public void listAll(String keyword) {
		loadData(keyword);
		System.out.println("---------------------------");
		for(int i = 0; i < list.size(); i++) {
			System.out.print((i+1) + " ");
			System.out.println(list.get(i).toString());
		}
		System.out.println("---------------------------");
	}

//	public ArrayList<Integer> listAll(String keyword) {
//		ArrayList<Integer> idlist = new ArrayList<>();
//		int j = 0;
//		System.out.println("---------------------------");
//		for(int i = 0; i < list.size(); i++) {
//			String word = list.get(i).getWord();
//			if(!word.contains(keyword)) continue;
//			System.out.print((j+1) + " ");
//			System.out.println(list.get(i).toString());
//			idlist.add(i);
//			j++;
//		}
//		System.out.println("---------------------------");
//		return idlist;
//	}

	public void listAll(int level) {
		int j = 0;
		System.out.println("---------------------------");
		for(int i = 0; i < list.size(); i++) {
			int ilevel = list.get(i).getLevel();
			if(ilevel != level) continue;
			System.out.print((j+1) + " ");
			System.out.println(list.get(i).toString());
			j++;
		}
		System.out.println("---------------------------");
	}

	public void updateItem() {
		System.out.print("=> 수정할 단어 검색 : ");
		String keyword = s.next();
		listAll(keyword);

		System.out.print("=> 수정할 번호 선택 : ");
		int id = s.nextInt();
		s.nextLine();

		System.out.print("=> 뜻 입력 : ");
		String meaning = s.nextLine();

		int val = update(new Word(list.get(id-1).getId(), 0, "", meaning));
		if(val > 0) System.out.println("단어가 수정되었습니다.");
		else System.out.println("[수정] 에러발생 !!!");
	}

	public void deleteItem() {
		System.out.print("=> 삭제할 단어 검색 : ");
		String keyword = s.next();
		listAll(keyword);

		System.out.print("=> 삭제할 번호 선택 : ");
		int id = s.nextInt();
		s.nextLine();

		System.out.print("=> 정말로 삭제하실래요?(Y/n) ");
		String ans = s.next();
		if (ans.equalsIgnoreCase("y")) {
			int val = delete(new Word(list.get(id-1).getId(), 0, "", ""));
			if(val > 0) System.out.println("단어가 삭제되었습니다. ");
			else System.out.println("[삭제] 에러발생 !!!");
		} else {
			System.out.println("취소되었습니다. ");
		}
	}

	public void loadFile() {
		list.clear();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fname));
			String line;
			int count = 0;

			while(true) {
				line = br.readLine();
				if(line == null) break;
				String[] data = line.split("\\|");
				int level = Integer.parseInt(data[0]);
				String word = data[1];
				String meaning = data[2];
				list.add(new Word(count, level, word, meaning));
				count++;
			}
			br.close();
			System.out.println("==> " + count + "개 로딩 완료!!!");
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	public void saveFile() {
		try {
			PrintWriter pr = new PrintWriter(new FileWriter(fname));
			for(Word one : list) {
				pr.write(one.toFileString() + "\n");
			}
			pr.close();
			System.out.println("==> 데이터 저장 완료 !!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void searchLevel() {
		System.out.print("=> 원하는 레벨은? ");
		int level = s.nextInt();
		listAll(level);
	}

	public void searchWord() {
		System.out.print("=> 원하는 단어는? ");
		String keyword = s.next();
		listAll(keyword);
	}
}
