package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

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
  
  private static final JCloudsNova jcloudsNova = new JCloudsNova();
  
  public static Result listFlavourForZone(String zoneId){
	  List<FlavorView> flavourList = jcloudsNova.listFlavorsForZone(zoneId);
	  Gson gson = new Gson();
	  String jsonResponse = gson.toJson(flavourList);
	  return ok(jsonResponse);
  }
  
  public static Result listZones(){
	  Set<String> zones = jcloudsNova.listZones();
	  Gson gson = new Gson();
	  String jsonResponse = gson.toJson(zones);
	  return ok(jsonResponse);
  }
  
  //createVm(zoneId:String,name:String,imageId:String,flavorId:String)
  public static Result createVm(String zoneId,String name,String imageId,String flavorId){
	try{
		List<VMProperties> vmPropertiesList = jcloudsNova.createNewInstance(zoneId, name, imageId, flavorId);
		String jsonResponse = getVmPropertiesListAsJson(vmPropertiesList);
		return ok(jsonResponse);
	}catch(Exception e){
		e.printStackTrace();
		return internalServerError();
	}
  }
  
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
	  List<ImageView> imageList = jcloudsNova.listImagesForZone(zoneId);
	  Gson gson = new Gson();
	  String jsonResponse = gson.toJson(imageList);
	  return ok(jsonResponse);
  }
  
  public static Result jcloudTest()
  {
	  
	  List<VMProperties> vmPropertyList = new ArrayList<VMProperties>();
	  String jsonResponse = "";
	  
      try {
          vmPropertyList = jcloudsNova.listServers();
          jsonResponse = getVmPropertiesListAsJson(vmPropertyList);
          jcloudsNova.close();
      } catch (Exception e) {
          e.printStackTrace();
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
  
  public static Result deleteVm(String zoneId,String vmId){
	  try{
		  List <VMProperties> vmPropertiesList = jcloudsNova.deleteVM(zoneId, vmId);
		  String jsonResponse = getVmPropertiesListAsJson(vmPropertiesList);
		 return ok(jsonResponse);  
	  }catch(Exception e){
		  e.printStackTrace();
		  return internalServerError();
	  } 
  }
  
    
  
}
