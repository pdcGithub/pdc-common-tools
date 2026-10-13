/******************************************************************************************************

This file "ConcurrencyTestUtil.java" is part of project "myservice" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2026 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.utils.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import net.mickarea.tools.utils.Stdout;

/**
 * 这里是一个大并发测试的封装类，用于测试在并发执行代码时的问题。具体需要看 task 任务
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2026年1月9日-2026年1月9日
 */
public class ConcurrencyTestUtil {
	
	/**
	 * 这里是一个大并发测试的封装类，用于测试在并发执行代码时的问题。具体需要看 task 任务
	 * <p>注意：如果并发线程很少，可能测不出问题，建议大于50。在task代码里面，建议增加 Thread.sleep 函数调用，以手动产生时间差和线程切换</p>
	 * @param threadNum 并发的线程总数，默认是负数。运行时，它必须是一个大于等于1的数值
	 * @param timeout 这是全部任务执行完成时，等待的时长。超时后会直接停止
	 * @param timeoutUnit 这是 timeout 参数的单位信息，比如：时、分、秒
	 * @param runnableTask 这是要执行的任务
	 * @throws IllegalArgumentException 参数校验时，发生的一些异常。一般情况下就是入参有问题
	 * @throws Exception 可能的一些异常
	 */
	public static void test(int threadNum, long timeout, TimeUnit timeoutUnit, Runnable runnableTask)
			throws IllegalArgumentException, Exception {
		
		Stdout.mylogger.debug("high concurrency testing : begining!");
		
		// 首先是参数检测
		if(threadNum<1 || timeout<1 || timeoutUnit==null || runnableTask==null) {
			//输出异常信息
			String output = String.format("some parameters of HighConcurrencyTesting may be wrong, please check. "
					+ "threadNum=%s, timeout=%s, timeoutUnit=%s, task=%s", 
					threadNum, timeout, timeoutUnit, runnableTask);
			throw new IllegalArgumentException(output);
		}
		
		Stdout.mylogger.debug("high concurrency testing : parameters ok!");
		
		// 线程池初始化
		ExecutorService pool = Executors.newFixedThreadPool(threadNum);
		
		Stdout.mylogger.debug("high concurrency testing : thread pool ok!");
		
		// 启动计数器 和 累计计数器 初始化
		CountDownLatch begin = new CountDownLatch(1);          // 发令枪
		CountDownLatch end = new CountDownLatch(threadNum);    // 线程同步计数器
		
		Stdout.mylogger.debug("high concurrency testing : count down latch ok!");
		
		// 循环生成 threadNum 个任务，交给任务池执行
		IntStream.range(0, threadNum).forEach(t->{
			pool.submit(()->{
				try {
					// 等待发令枪信号，暂时阻塞
					begin.await();
					
					// 停顿一下，易产生线程切换
					Thread.sleep(1);
					
					//具体的处理
					runnableTask.run();
					
					// 停顿一下，易产生线程切换
					Thread.sleep(1);
					
				}catch(Exception e) {
					// 如果线程已经被中断，则标记一下
					if(e instanceof InterruptedException) Thread.currentThread().interrupt();
					// 打印异常信息
					Stdout.mylogger.error("There was an errro when task running. erro:"+e);
				}finally {
					end.countDown();
				}
			});
		});
		
		Stdout.mylogger.debug("high concurrency testing : task submit ok!");
		
		// 发令枪，启动
		begin.countDown();
		Stdout.mylogger.debug("high concurrency testing : start!");
		try {
			// 等待全部任务执行结束
			end.await(timeout, timeoutUnit);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			Stdout.mylogger.error("high concurrency testing : something was wrong when waiting! info : "+e);
		}
		
		// 关闭线程池
		pool.shutdown();
		Stdout.mylogger.debug("high concurrency testing : shutdown the pool!");
		try {
            // 等待所有任务完成
            if (!pool.awaitTermination(timeout, timeoutUnit)) {
                Stdout.mylogger.debug("high concurrency testing : timeout! There are still tasks not completed; force close!");
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
        	Thread.currentThread().interrupt();
        	Stdout.mylogger.error("high concurrency testing : error! An exception occurred while waiting for the pool to close. force close!");
            pool.shutdownNow();
        }
		
		Stdout.mylogger.debug("high concurrency testing : end!");
	}
}
