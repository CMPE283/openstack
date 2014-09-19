package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import org.jclouds.openstack.swift.v1.domain.Container;

import compute.json.FlavorView;
import compute.json.ImageView;
import compute.json.LimitView;
import compute.json.VMProperties;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
	
  
   
  public static Result index() {
    return ok(index.render("Your new application is ready."));
  }
  
  public static Result test()
  {
	  return ok("bye");
  }
    
  /*public static Result listFlavourForZone(String zoneId){
	  List<FlavorView> flavourList = jcloudsNova.listFlavorsForZone(zoneId);
	  Gson gson = new Gson();
	  String jsonResponse = gson.toJson(flavourList);
	  return ok(jsonResponse);
  }
  
  public static Result listZones(){
      response().setHeader("Content-Type", "application/json");
      response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
      response().setHeader("Access-Control-Allow-Methods", "GET");   // Only allow POST
      response().setHeader("Access-Control-Max-Age", "300");          // Cache response for 5 minutes
      response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");         // Ensure this header is also allowed!  
	  Set<String> zones = jcloudsNova.listZones();
	  Gson gson = new Gson();
	  String jsonResponse = gson.toJson(zones);
	  return ok(jsonResponse);
  }
  
  //createVm(zoneId:String,name:String,imageId:String,flavorId:String)
  
  public static Result usage(String zone,String tenantId){
	  try{
		  List<LimitView> limitView = jcloudsNova.getLimitViewForTenant(zone, tenantId);
		  Gson gson = new Gson();
		  String jsonResponse = gson.toJson(limitView);
		  return ok(jsonResponse);
	  }catch(Exception e){
		  e.printStackTrace();
		  return internalServerError();
	  }
  }
  
  public static Result listImagesForZone(String zoneId){
      response().setHeader("Content-Type", "application/json");
      response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
      response().setHeader("Access-Control-Allow-Methods", "GET");   // Only allow POST
      response().setHeader("Access-Control-Max-Age", "300");          // Cache response for 5 minutes
      response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");         // Ensure this header is also allowed!  
	  System.out.println("Listing images");
	  List<ImageView> imageList = jcloudsNova.listImagesForZone(zoneId);
	  Gson gson = new Gson();
	  String jsonResponse = gson.toJson(imageList);
	  return ok(jsonResponse);
  }
  
 public static Result saveImage(String zone,String serverId, String newImageName){
	 String imageId= jcloudsNova.saveImage(zone, serverId, newImageName);
	 Gson gson = new Gson();
	 String jsonResponse = gson.toJson(imageId);
	 return ok(jsonResponse);
 }*/
  public static Result createVm(String zoneId,String name,String imageId,String flavorId,String username,String password){
	  JCloudsNova jcloudsNova = new JCloudsNova(username,password);
	  String jsonResponse = "";
	  try{
			System.out.println("Creating VM");
			
			//List<VMProperties> vmPropertiesList = jcloudsNova.createNewInstance(zoneId, name, imageId, flavorId);
			Boolean createvalue=jcloudsNova.createNewInstance(zoneId, name, imageId, flavorId);
			System.out.println("Boolean value"+ createvalue);
			if(createvalue==true)
			{
			List<VMProperties> vmPropertyList1 = new ArrayList<VMProperties>();
			
			vmPropertyList1 = jcloudsNova.listServers();
			jsonResponse = getVmPropertiesListAsJson(vmPropertyList1);
			}
			jcloudsNova.close();
      } catch (Exception e) {
          e.printStackTrace();
          return internalServerError();
      } finally {
          //jcloudsNova.close();
      }
      response().setHeader("Content-Type", "application/json");
      response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
      response().setHeader("Access-Control-Allow-Methods", "GET");   // Only allow POST
      response().setHeader("Access-Control-Max-Age", "300");          // Cache response for 5 minutes
      response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");         // Ensure this header is also allowed!  
      return ok(jsonResponse);
	  }
	  
  public static Result jcloudTest(String username, String password)
  {
	  JCloudsNova jcloudsNova = new JCloudsNova(username,password);
	  List<VMProperties> vmPropertyList = new ArrayList<VMProperties>();
	  String jsonResponse = "";
	  
      try {
          vmPropertyList = jcloudsNova.listServers();
          jsonResponse = getVmPropertiesListAsJson(vmPropertyList);
          jcloudsNova.close();
      } catch (Exception e) {
          e.printStackTrace();
          return internalServerError();
      } finally {
          //jcloudsNova.close();
      }
      response().setHeader("Content-Type", "application/json");
      response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
      response().setHeader("Access-Control-Allow-Methods", "GET");   // Only allow POST
      response().setHeader("Access-Control-Max-Age", "300");          // Cache response for 5 minutes
      response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");         // Ensure this header is also allowed!  
      return ok(jsonResponse);
  }
  
  private static String getVmPropertiesListAsJson(List<VMProperties> vmPropertiesList){
	    Gson gson = new Gson();
        String jsonResponse = gson.toJson(vmPropertiesList);
        return jsonResponse;
  }
  
 /* public static Result uploadObjectsToContainer(String zoneId, String containerId, String objectToUpload){
	  System.out.println("uploading file to containers: "+ objectToUpload);
      response().setHeader("Content-Type", "application/json");
      response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
      response().setHeader("Access-Control-Allow-Methods", "GET");   // Only allow POST
      response().setHeader("Access-Control-Max-Age", "300");          // Cache response for 5 minutes
      response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");         // Ensure this header is also allowed!  
	  jcloudsNova.uploadObjectToContainer(zoneId, containerId, objectToUpload);
	  Gson gson = new Gson();
	  //String jsonResponse = gson.toJson(imageId);
	  return ok();

  }
  
  public static Result createContainer(String zoneId,String containerName){
      response().setHeader("Content-Type", "application/json");
      response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
      response().setHeader("Access-Control-Allow-Methods", "GET");   // Only allow POST
      response().setHeader("Access-Control-Max-Age", "300");          // Cache response for 5 minutes
      response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");         // Ensure this header is also allowed!  
      jcloudsNova.createContainer(zoneId, containerName);
	  return ok();
  }
  
  
  public static Result listContainers(){
      response().setHeader("Content-Type", "application/json");
      response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
      response().setHeader("Access-Control-Allow-Methods", "GET");   // Only allow POST
      response().setHeader("Access-Control-Max-Age", "300");          // Cache response for 5 minutes
      response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");         // Ensure this header is also allowed!  
	  Set<Container> containers = jcloudsNova.listContainers("RegionOne");
	  Gson gson = new Gson();
	  String jsonResponse = gson.toJson(containers);
	  return ok(jsonResponse);
  }*/
    
 /* public static Result deleteVm(String zoneId,String vmId){
	  JCloudsNova jcloudsNova = new JCloudsNova(username,password);
	  try{
		  List <VMProperties> vmPropertiesList = jcloudsNova.deleteVM(zoneId, vmId);
		  String jsonResponse = getVmPropertiesListAsJson(vmPropertiesList);
		 return ok(jsonResponse);  
	  }catch(Exception e){
		  e.printStackTrace();
		  return internalServerError();
	 
	  } 
  }*/
   
}
