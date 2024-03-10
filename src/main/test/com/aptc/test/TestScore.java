package com.aptc.test;

import cn.hutool.core.util.StrUtil;
import com.aptc.mapper.ScoreMapper;
import com.aptc.mapper.SongMapper;
import com.aptc.pojo.vo.UserScoreVO;
import com.aptc.utils.BaseContext;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@SpringBootTest
public class TestScore {

	@Autowired
	private ScoreMapper scoreMapper;

	@Autowired
	private SongMapper songMapper;

	@Test
	public void test(){
		List<UserScoreVO> allScore = scoreMapper.getAllScore(2);
		UserScoreVO userScoreVO = allScore.get(1);
		System.out.println(userScoreVO);
	}

	@Test
	public void testCsv(){
		Integer userId = 2;
		//读取成绩
		List<UserScoreVO> scores = scoreMapper.getAllScore(userId);
		//导出CSV
		StringWriter sw = new StringWriter();
		CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
//				.setDelimiter("|")
//				.setEscape('\"')
				.setHeader("sid", "sname",
						"pst", "pst_score", "pst_ptt",
						"prs", "prs_score", "prs_ptt",
						"ftr", "ftr_score", "ftr_ptt",
						"byd", "byd_score", "byd_ptt",
						"etr", "etr_score", "etr_ptt")
				.build();
		try {
			CSVPrinter csvPrinter = new CSVPrinter(sw, csvFormat);
			for (UserScoreVO score : scores) {
				csvPrinter.printRecord(
						score.getSid(), score.getSname(),
						score.getPst(),
						score.getPstScore(), score.getPstPtt(),
						score.getPrs(), score.getPrsScore(), score.getPrsPtt(),
						score.getFtr(), score.getFtrScore(), score.getFtrPtt(),
						score.getByd(), score.getBydScore(), score.getBydPtt(),
						score.getEtr(), score.getEtrScore(), score.getEtrPtt()
				);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println(sw);
	}

	@Test
	public void updatePtt(){
		String data = "LIVHT MY WΔY||9.8||Lawless Point||9||Flashback||8.9||Journey||9.1||Coastal Highway||9||Lights of Muse||8.9||Grimheart||8.7||cry of viyella||8.7||Chelsea||8.9||Cosmica||8.8||enchanted love||8.6||Tsuki ni Murakumo, Hana ni Kaze||8.7||April showers||8.6||Altair (feat. *spiLa*)||8.5||Dialnote||8.8||Rise||7.8||Dement ~after legend~||7.8||Infinity Heaven||7.8||Brand new world||7.8||Paradise||7.8||Clotho and the stargazer||7.8||Moonlight of Sand Castle||7.8||Romance Wars||7.8||Dream goes on||7.8||inkar-usi||7.8||Suomi||7.8||First Snow||7.8";
		List<String> split = StrUtil.split(data, "||");
		for(int i = 0; i < split.size(); i+=2){
			String sname = split.get(i);
			Double difficulty = Double.parseDouble(split.get(i + 1));
			int influence = songMapper.testChangeFtrDiff(sname, difficulty);
			System.out.println(sname + " " + difficulty + " " + influence + "\n");
		}
	}
}
