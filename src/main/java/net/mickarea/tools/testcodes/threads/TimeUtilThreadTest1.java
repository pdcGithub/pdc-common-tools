package net.mickarea.tools.testcodes.threads;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import net.mickarea.tools.utils.Stdout;
import net.mickarea.tools.utils.TimeUtil;

/**
 * 关于时间处理类的线程安全问题测试。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年11月29日
 */
public class TimeUtilThreadTest1 {

	/**
	 * 总执行次数
	 */
	private static final int EXECUTE_COUNT = 1000;
	
	/**
	 * 同时运行的线程数
	 */
	private static final int THREAD_COUNT = 20;
	
	public static void main(String[] args) throws Exception {
		
		final Semaphore semaphore = new Semaphore(THREAD_COUNT);

		final CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		for(int i=0;i<EXECUTE_COUNT;i++) {
			executorService.execute(() -> {
				try {
					semaphore.acquire();
					
					//Stdout.pl("时间1："+TimeUtil.getDefaultValue());
					//Stdout.pl("时间2："+TimeUtil.getDefaultValue(new Date()));
					//Stdout.pl("时间3："+TimeUtil.getDefaultValue(System.currentTimeMillis()));
					//Stdout.pl("时间4："+TimeUtil.getDefaultValueWithMiliseconds());
					//Stdout.pl("时间5："+TimeUtil.getDefaultValueWithMiliseconds(new Date()));
					//Stdout.pl("时间6："+TimeUtil.getDefaultValueWithMiliseconds(System.currentTimeMillis()));
					//Stdout.pl("时间7："+TimeUtil.getCustomValue(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
					//Stdout.pl("时间8："+TimeUtil.getCustomValue(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));
					//Stdout.pl("时间9："+TimeUtil.getCustomValue(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss.SSSSSS", "无..."));
					//Stdout.pl("时间10："+TimeUtil.getTimestampByLocalDateTime(LocalDateTime.now()));
					Stdout.pl("时间11："+TimeUtil.getTimestampByLocalDateTime(LocalDateTime.now(), ZoneId.systemDefault()));
					
					semaphore.release();
					
				} catch (InterruptedException e) {
					Stdout.pl("信号发生错误，"+e.getMessage());
					Stdout.pl("中止执行。");
					System.exit(-1);
				} catch (Exception e1) {
					Stdout.pl("发送其它异常，"+e1.getMessage());
				}
				countDownLatch.countDown();
			});
		}
		
		countDownLatch.await();
		executorService.shutdown();
		Stdout.pl("所有线程日期格式化成功");

	}

}
