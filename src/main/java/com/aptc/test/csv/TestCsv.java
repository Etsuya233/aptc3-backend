package com.aptc.test.csv;

import com.aptc.pojo.Score;
import com.aptc.utils.ArcaeaUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest
public class TestCsv {
	@Test
	public void testLoadCsv() throws FileNotFoundException {
		//初始化CSV格式
		CSVFormat csvFormat = CSVFormat.EXCEL.builder()
				.setHeader("sid", "sgid", "sname",
						"pst", "pst_score", "pst_ptt",
						"prs", "prs_score", "prs_ptt",
						"ftr", "ftr_score", "ftr_ptt",
						"byd", "byd_score", "byd_ptt",
						"etr", "etr_score", "etr_ptt")
				.build();
		File file = new File("/home/etsuya/Downloads/testcsv.csv");

		//解析CSV
		try(FileInputStream fis = new FileInputStream(file)){
			CSVParser parser = CSVParser.parse(fis, StandardCharsets.UTF_8, csvFormat);
			List<CSVRecord> records = parser.getRecords();
			List<Score> scoreList = records.stream().filter(r -> r != null && r.getRecordNumber() != 1
							&& (!r.get(4).isEmpty() || !r.get(7).isEmpty() || !r.get(10).isEmpty() || !r.get(13).isEmpty() || !r.get(16).isEmpty()))
					.map(r -> {
						Score score = new Score();
						if (!r.get((4)).isEmpty()) {
							int s = Integer.parseInt(r.get(4));
							double d = Double.parseDouble(r.get(3));
							score.setPstScore(s);
							score.setPstPtt(ArcaeaUtils.pptCalc(s, d));
						}
						if (!r.get(7).isEmpty()) {
							int s = Integer.parseInt(r.get(7));
							double d = Double.parseDouble(r.get(6));
							score.setPrsScore(s);
							score.setPrsPtt(ArcaeaUtils.pptCalc(s, d));
						}
						if (!r.get(10).isEmpty()) {
							int s = Integer.parseInt(r.get(10));
							double d = Double.parseDouble(r.get(9));
							score.setFtrScore(s);
							score.setFtrPtt(ArcaeaUtils.pptCalc(s, d));
						}
						if (!r.get(13).isEmpty()) {
							int s = Integer.parseInt(r.get(13));
							double d = Double.parseDouble(r.get(12));
							score.setFtrScore(s);
							score.setFtrPtt(ArcaeaUtils.pptCalc(s, d));
						}
						if (!r.get(16).isEmpty()) {
							int s = Integer.parseInt(r.get(16));
							double d = Double.parseDouble(r.get(15));
							score.setFtrScore(s);
							score.setFtrPtt(ArcaeaUtils.pptCalc(s, d));
						}
						return score;
					}).toList();
			scoreList.forEach(System.out::println);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
