package fr.thedarven.utils.texts;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextInterpreter {

	public static String textInterpretation(String format, Map<String, String> variables) {
		Pattern pattern = Pattern.compile("\\{[a-zA-Z0-9_]*\\}");
		StringBuilder finalString = new StringBuilder();
		Matcher matcher = pattern.matcher(format);
		int last = 0;
		String indexS;
		while(matcher.find()) {
			finalString.append(format.subSequence(last, matcher.start()));
			indexS = format.subSequence(matcher.start()+1, matcher.end()-1).toString();
			finalString.append(variables.containsKey(indexS) ? variables.get(indexS) : "{"+indexS.toUpperCase()+"}");
			last = matcher.end();
		}
		finalString.append(format.subSequence(last, format.length()));
		return finalString.toString();
	}
	
}
