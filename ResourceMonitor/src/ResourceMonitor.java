import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

/*** 
 * 
 * @author smita
 *	ResourceMonitor to continuously monitor
 *  status of application
 */

public class ResourceMonitor {
	
	// defining number of resources (assumed here to be 2) 
	private final static int NUM_RESOURCES = 4;
	
	private static ConcurrentHashMap<String,Boolean> resourcesStatus = new ConcurrentHashMap<String,Boolean>(); 
	
	private static class Resource implements Runnable  {
		 
		 private String name;    //resource name
		 private boolean status; //health status 
		 
		 Resource(String name,boolean status){
			 
			 this.name = name;
			 this.status = status;
		 }
		 
		 @Override
		 public void run() {
			 
			 try {
				  while(true) {
					
					   int  num = ThreadLocalRandom.current().nextInt(0,2);
						switch(num) {
				   			   case 1: status = true; 
				   	 		   		break;
				   			   case 0: status = false;         
				   			        break;
				   		   
				   }
				 
				 //monitoring resource health status		
				 setResource(name,status);
				 
				 //defining random intervals for resource thread
				 int microInterval = ThreadLocalRandom.current().nextInt(50,100);  
				 
				 Thread.sleep(microInterval);
			 }
			}catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		 }
		 
		 	public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public boolean isStatus() {
				return status;
			}

			public void setStatus(boolean status) {
				this.status = status;
			}

	
		
		 synchronized void setResource(String resourceName, boolean isHealth) {
				
			 resourcesStatus.put(resourceName,isHealth);
		}
	}	
		static boolean isHealthy() {
	
			for(Map.Entry<String, Boolean> entry : resourcesStatus.entrySet())	
				if (entry.getValue() == Boolean.TRUE)
					continue;
				else return false;
			return true;	
		  }
	
		
	
		public static void main (String args[]) {
	
			Resource[] resources = new Resource[NUM_RESOURCES];
			
			for(int i = 0; i< NUM_RESOURCES;i++) 
				resources[i] = new Resource("Resource" + i,false);

			Thread[] t = new Thread[NUM_RESOURCES];
		
			for (int i=0; i<NUM_RESOURCES; i++) {
			
				t[i] = new Thread(resources[i],"" + i + "");
	       		t[i].start();
			}
			
		 Request req = new Request();
		 
		 //generating continuous requests
		 Thread requestThread = new Thread(req,"requests");
		 requestThread.start();	

		}
}	

	class Request implements Runnable {
	
		@Override
		public void run() {
		
			while(true) {
	    
				if (ResourceMonitor.isHealthy())
						System.out.println("200");
				else 
			System.out.println("500");
			}
	}
}
 	

	 
 
