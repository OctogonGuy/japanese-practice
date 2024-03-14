package tech.octopusdragon.japanesepractice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import com.moji4j.MojiConverter;

public class test {

	public static void main(String[] args) throws IOException {
		File verbFile = new File("src/main/java/tech/octopusdragon/japanesepractice/verb_list.csv");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(verbFile), StandardCharsets.UTF_8));
		String str = "";
		String line;
		MojiConverter converter = new MojiConverter();
		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split(",", 3);
			tokens[1] = converter.convertRomajiToHiragana(converter.convertKanaToRomaji(tokens[1]));
			str += tokens[0] + "," + tokens[1] + "," + tokens[2] + "\n";
		}
		System.out.println(str);
		reader.close();
				
		
		PrintWriter outputFile = new PrintWriter("new.csv", "UTF-8");
		outputFile.println(str);
		outputFile.close();
	}

}
