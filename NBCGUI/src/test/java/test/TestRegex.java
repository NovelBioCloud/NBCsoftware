package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {
	
	public static void main(String[] args) {
		
		String regex = "\\$\\{text\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher("${text}");
		System.out.println(matcher.matches());
		
	}

}
