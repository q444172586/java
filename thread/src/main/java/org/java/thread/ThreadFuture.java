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
		ExecutorService service = Executors.newFixedThreadPool(3);
		List<Future<String>> futureList=new ArrayList<Future<String>>();
		
		for (int i = 0; i <10; i++) {
			CallableTask task=new CallableTask(i);
			futureList.add(service.submit(task));//��˳���
		}
		
		for (int j = 0; j < futureList.size(); j++) {
			System.out.println("��˳��ȡ�����"+futureList.get(j).get());
		}
		System.out.println("��������ˣ�");
		service.shutdown();
	}
	
	private class CallableTask implements Callable<String>{
		
		Integer temp;
		
		public CallableTask(Integer d){
			temp=d;
		}
		
		public String call() throws Exception {
			System.out.println("ִ�е�"+temp+"������");
			Thread.sleep(3000);
			return "��"+temp+"������"+Thread.currentThread().getName();
		}
		
	}


}
