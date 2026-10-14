package net.mickarea.tools.maintests;

import net.mickarea.tools.utils.Stdout;

public class NumStrTest {

	public static void main(String[] args) {
		
		String s1 = "0.780000239109086";
		String s2 = "2.1956000809897347E-5";
		
		int start = s1.indexOf(".")+1;
		int end = s1.indexOf("E")<0?s1.length():s1.indexOf("E");

		int start2 = s2.indexOf(".")+1;
		int end2 = s2.indexOf("E")<0?s1.length():s2.indexOf("E");
		
		Stdout.pl(s1.substring(start, end));
		Stdout.pl(s2.substring(start2, end2));
	}

}
