package net.javajs.bdi.collection;

import lombok.Data;

@Data
public class PlayerDetail {
	private String player_no;
	private String team_code;
	private double player_game;
	private String player_min; //출장시간 time
	private double player_2p;//필드콜 성공 -시도
	private double player_2pa;
	private double player_3p;//3점슛 성공 -시도
	private double player_3pa;
	private double player_fgp; //야투성공률
	private double player_ft; //자유투 성공-시도
	private double player_fta;
	private double player_off;//공격리바운드
	private double player_def;//수비리바운드
	private double player_rpg; //경기당 리바운드
	private double player_ast; //어시스트
	private double player_apg;//경기당 어시스트
	private double player_wft;//파울-자유투 유
	private double player_woft;//파울-자유투 무
	private double player_stl;
	private double player_bs; //블록슛
	private double player_gd;//굳디펜스
	private double player_to;//턴오버
	private double player_dk; //덩크슛 성공 시도
	private double player_dka;
	private double player_pts; //득점
	private double player_ppg;//경기당 평균득점

}
