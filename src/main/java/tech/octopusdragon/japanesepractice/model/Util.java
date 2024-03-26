package tech.octopusdragon.japanesepractice.model;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

import com.moji4j.MojiConverter;
import com.moji4j.MojiDetector;

import javafx.application.Platform;
import javafx.scene.control.TextField;

/**
 * Contains utility functionality
 */
public class Util {
	
	/**
	 * Converts roman characters in a JavaFX text field to hiragana
	 * @param textField The TextField object
	 */
	public static void convertRomajiToHiragana(TextField textField) {
		Platform.runLater(() -> {	// Run on application thread so changes are accurate
			String romajiStr = textField.getText();
			String hiraganaStr = new MojiConverter().convertRomajiToHiragana(romajiStr);
			
			// If there is no convertible romaji, do nothing
			if (romajiStr.equals(hiraganaStr)) return;
			
			// If there is convertible romaji, get the different part
			hiraganaStr = new String(hiraganaStr.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
			String romajiDiff = StringUtils.difference(hiraganaStr, romajiStr);
			String hiraganaDiff = StringUtils.difference(romajiStr, hiraganaStr);
			int diffIndex = StringUtils.indexOfDifference(romajiStr, hiraganaStr);
			
			// Do nothing if just small っ
			if (hiraganaDiff.startsWith("っ") && new MojiDetector().hasRomaji(hiraganaDiff)) return;
			// Do nothing if ん without double n
			if (hiraganaDiff.startsWith("ん") && (romajiDiff.equals("n") || romajiDiff.equals("m") || romajiDiff.equals("nm"))) return;
			// Do nothing if ん with ny
			if (hiraganaDiff.startsWith("ん") && (romajiDiff.equals("ny"))) return;
			
			// Replace double n with ん
			hiraganaDiff = hiraganaDiff.replace("っん", "ん");
			
			int caretDistanceFromEnd = textField.getText().length() - textField.getCaretPosition();
			textField.replaceText(diffIndex, diffIndex + romajiDiff.length(), hiraganaDiff);
			textField.positionCaret(textField.getText().length() - caretDistanceFromEnd);
		});
	}
}
