package org.java.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

/**
 * ���߳�-��˳���(��˳��ȡִ�н��)
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
			futureList.add(service.submit(task));//��˳���
		}
		
		for (int j = 0; j < futureList.size(); j++) {
			Person person = futureList.get(j).get();
			System.out.println("��˳��ȡ�����"+person.getName());
		}
		System.out.println("��������ˣ�");
		service.shutdown();
	}
	
	private class CallableTask implements Callable<Person>{
		
		Integer temp;
		
		public CallableTask(Integer d){
			temp=d;
		}
		
		public Person call() throws Exception {
			System.out.println("ִ�е�"+temp+"������");
			Thread.sleep(3000);
			Person person = new Person();
			person.setName("��"+temp+"������"+Thread.currentThread().getName());
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
