package org.java.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

/**
 * 多线程-按顺序放(按顺序取执行结果)
 * @author brave wang
 *
 */
public class ThreadFuture {
	
	public static void main(String[] args) throws Exception {
		new ThreadFuture().js();
	}
	
	@Test
	public void js() throws Exception{
		ExecutorService service = Executors.newFixedThreadPool(3);
		List<Future<String>> futureList=new ArrayList<Future<String>>();
		
		for (int i = 0; i <10; i++) {
			CallableTask task=new CallableTask(i);
			futureList.add(service.submit(task));//按顺序放
		}
		
		for (int j = 0; j < futureList.size(); j++) {
			System.out.println("按顺序取结果："+futureList.get(j).get());
		}
		System.out.println("计算结束了！");
		service.shutdown();
	}
	
	private class CallableTask implements Callable<String>{
		
		Integer temp;
		
		public CallableTask(Integer d){
			temp=d;
		}
		
		public String call() throws Exception {
			System.out.println("执行第"+temp+"个任务");
			Thread.sleep(3000);
			return "第"+temp+"个任务："+Thread.currentThread().getName();
		}
		
	}


}
