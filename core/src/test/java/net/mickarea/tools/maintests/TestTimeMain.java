package net.mickarea.tools.maintests;

import java.util.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.TimeUtil;

public class TestTimeMain {

	public static void main(String[] args) {
		
		Stdout.pl("=====================getDefaultValue =========================");
		Stdout.pl(TimeUtil.getDefaultValue());
		Stdout.pl(LocalDateTime.now().format(TimeUtil.FMT_DEFAULT));
		Stdout.pl("=====================getDefaultValue Date=========================");
		Stdout.pl(TimeUtil.getDefaultValue(new Date()));
		Stdout.pl(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(TimeUtil.FMT_DEFAULT));
		Stdout.pl("=====================getDefaultValue Long=========================");
		Stdout.pl(TimeUtil.getDefaultValue(System.currentTimeMillis()));
		Stdout.pl(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()).format(TimeUtil.FMT_DEFAULT));
		Stdout.pl("===================== more tests =========================");
		Stdout.pl(Instant.now());
		Stdout.pl(LocalDateTime.now());
		Stdout.pl(LocalDateTime.now().atZone(ZoneId.systemDefault()).format(TimeUtil.FMT_DEFAULT_MILISECOND));
		
		// Timestamp
		Stdout.pl(Timestamp.valueOf("2012-12-21 00:00:00"));
		Stdout.pl(LocalDateTime.now());
		Stdout.pl(LocalDateTime.now().atZone(ZoneId.of("UTC+8")));
		Stdout.pl(LocalDateTime.now().atZone(ZoneId.of("UTC+8")).toInstant());
		Stdout.pl("===================== Instant tests =========================");
		Stdout.pl(Instant.now());
		Stdout.pl(Instant.now().atZone(ZoneId.of("UTC+8")));
		
		Stdout.pl("===================== other tests =========================");
		Stdout.pl(LocalDateTime.parse("2025-01-01 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		
		// 测试自己的工具包
		Stdout.pl(TimeUtil.getDefaultValueWithMiliseconds(Instant.now().toEpochMilli()));
		Stdout.pl(TimeUtil.getDefaultValueWithMiliseconds(System.currentTimeMillis()));
		
		//Stdout.pl(DateTimeFormatter.ofPattern(null)); // java.lang.NullPointerException: pattern
		Stdout.pl(DateTimeFormatter.ofPattern("?::::::::?"));
		
		Stdout.pl(new Date(Long.MAX_VALUE));
		Stdout.pl(new Date(-1L));
		Stdout.pl(new Date(Long.MIN_VALUE));
		Stdout.pl(new Date(-Long.MAX_VALUE));
		Stdout.pl(TimeUtil.getCustomValue(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss.SSSSSSSSS", "无"));
		Stdout.pl(ZoneId.systemDefault());
	}

}
