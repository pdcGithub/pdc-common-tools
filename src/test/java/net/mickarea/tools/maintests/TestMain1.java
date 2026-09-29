package net.mickarea.tools.maintests;

import java.util.Arrays;
import java.util.List;

import net.mickarea.tools.utils.Stdout;

public class TestMain1 {

	public static void main(String[] args) {
		
		Stdout.pl("测试");
		
		List<String> test = Arrays.asList("a", "b", "c", "d");
		
		test.forEach(Stdout::pl);
		
		test.stream().forEach(Stdout::pl);
		
		test.stream().forEach(name->{
			Stdout.pl(name);
		});
	}

}
