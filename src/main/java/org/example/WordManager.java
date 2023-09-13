package org.example;

import java.util.Scanner;

public class WordManager {
	Scanner s = new Scanner(System.in);
	WordCRUD wordCRUD;
	/*
	*** 영단어 마스터 ***
	*******************
	1. 모든 단어 보기
	2. 수준별 단어 보기
	3. 단어 검색
	4. 단어 추가
	5. 단어 수정
	6. 단어 삭제
	7. 파일 저장
	0. 나가기
	*******************
	=> 원하는 메뉴는?
	 */
	
	WordManager(){
		wordCRUD = new WordCRUD(s);
	}

	public void loadManager() {
		while(true) {
			int loadMethod = selectLoad();
			if(loadMethod == 1) {
				wordCRUD.loadFile();
				break;
			}
			else if(loadMethod == 2) {
				wordCRUD.loadData();
				break;
			}
			else {
				System.out.println("1과 2중에 선택하십시오.");
			}
		}
	}
	public int selectLoad() {
		System.out.print("*** 파일 로드 방법을 선택하세요 ***\n"
						+ " ******************************\n"
						+ " 1. txt 파일\n"
						+ " 2. DB\n"
						+ " ******************************\n"
						+ " => 원하는 메뉴는? ");
		return s.nextInt();
	}
	
	public int selectMenu() {
		System.out.print("*** 영단어 마스터 ***\n"
				+ "	*******************\n"
				+ "	1. 모든 단어 보기\n"
				+ "	2. 수준별 단어 보기\n"
				+ "	3. 단어 검색\n"
				+ "	4. 단어 추가\n"
				+ "	5. 단어 수정\n"
				+ "	6. 단어 삭제\n"
				+ "	7. 파일 저장\n"
				+ "    8. 파일 로드\n"
				+ "	0. 나가기\n"
				+ "	*******************\n"
				+ "	=> 원하는 메뉴는? ");
		
		return s.nextInt();
	}
	public void goSelectMenu() {
		s.nextLine();
		System.out.print("계속하려면 엔터를 누르세요.");
		s.nextLine();
		System.out.println();
	}
	public void start() {

		loadManager();

		while(true) {
			int menu = selectMenu();
			if(menu == 0) break;
			if(menu == 4) {
				wordCRUD.addItem();
			}
			else if(menu == 1) {
				wordCRUD.listAll();
			}
			else if(menu == 2) {
				wordCRUD.searchLevel();
			}
			else if(menu == 3) {
				wordCRUD.searchWord();
			}
			else if(menu == 5) {
				wordCRUD.updateItem();
			}
			else if(menu == 6) {
				wordCRUD.deleteItem();
			}
			else if(menu == 7) {
				wordCRUD.saveFile();
			}
			else if(menu == 8) {
				loadManager();
			}

			goSelectMenu();
		}

		System.out.println("");
		System.out.println("이용해 주셔서 감사합니다.");
	}
}
