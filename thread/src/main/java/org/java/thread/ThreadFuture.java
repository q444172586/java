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
		ExecutorService service = Executors.newFixedThreadPool(10);
		List<Future<Person>> futureList=new ArrayList<Future<Person>>();
		
		for (int i = 0; i <100; i++) {
			CallableTask task=new CallableTask(i);
			futureList.add(service.submit(task));//按顺序放
		}
		
		for (int j = 0; j < futureList.size(); j++) {
			Person person = futureList.get(j).get();
			System.out.println("按顺序取结果："+person.getName());
		}
		System.out.println("计算结束了！");
		service.shutdown();
	}
	
	private class CallableTask implements Callable<Person>{
		
		Integer temp;
		
		public CallableTask(Integer d){
			temp=d;
		}
		
		public Person call() throws Exception {
			System.out.println("执行第"+temp+"个任务");
			Thread.sleep(3000);
			Person person = new Person();
			person.setName("第"+temp+"个任务："+Thread.currentThread().getName());
			return person;
		}
		
	}
	
	private class Person{
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		
	}


}
