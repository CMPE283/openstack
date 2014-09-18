package controllers;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.neutron.v2.NeutronApi;
import org.jclouds.openstack.neutron.v2.domain.Network;
import org.jclouds.openstack.neutron.v2.features.NetworkApi;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Address;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.Image;
import org.jclouds.openstack.nova.v2_0.domain.Quota;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.domain.SimpleTenantUsage;
import org.jclouds.openstack.nova.v2_0.extensions.HostAggregateApi;
import org.jclouds.openstack.nova.v2_0.extensions.QuotaApi;
import org.jclouds.openstack.nova.v2_0.extensions.SimpleTenantUsageApi;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.jclouds.openstack.nova.v2_0.options.CreateServerOptions;
import org.jclouds.openstack.v2_0.domain.Limits;

import scala.collection.mutable.HashMap;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.io.Closeables;
import com.google.inject.Module;

import compute.json.FlavorView;
import compute.json.ImageView;
import compute.json.LimitView;
import compute.json.VMProperties;

public class JCloudsNova implements Closeable {
    private final NovaApi novaApi;
    private final Set<String> zones;
    private final NeutronApi neutronApi;
    
    public static void main(String[] args) throws IOException {
        JCloudsNova jcloudsNova = new JCloudsNova();
        try {
            jcloudsNova.listServers();
            jcloudsNova.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jcloudsNova.close();
        }
    }

    public void getStatistics(String zone){
    	SimpleTenantUsageApi tenantUsageApi = novaApi.getSimpleTenantUsageExtensionForZone(zone).get();
    	SimpleTenantUsage tenantUsage = tenantUsageApi.get("");
    	HostAggregateApi hostAggregateApi = novaApi.getHostAggregateExtensionForZone(zone).get();
    	
    }
    
    
    public JCloudsNova() {
        Iterable<Module> modules = ImmutableSet.<Module>of(new SLF4JLoggingModule());
        
        String provider = "openstack-nova";
        String identity = "admin:admin"; // tenantName:userName
        String credential = "a28d36fd90074d0c";

        novaApi = ContextBuilder.newBuilder(provider)
                .endpoint("http://10.0.0.16:35357/v2.0/")
                .credentials(identity, credential)
                .modules(modules)
                .buildApi(NovaApi.class);
        
        
        
        String neutronProvider = "openstack-neutron";
        neutronApi = ContextBuilder.newBuilder(neutronProvider)
                .endpoint("http://10.0.0.16:35357/v2.0/")
                .credentials(identity, credential)
                .modules(modules)
                .buildApi(NeutronApi.class);
       
        
        zones = novaApi.getConfiguredZones();
        System.out.println("............................" + zones );
    }

    public Set<String> listZones(){
    	Set<String> zones = novaApi.getConfiguredZones();
    	return zones;
    }
    
    public List<FlavorView> listFlavorsForZone(String zoneId){
    	FlavorApi flavorApi = novaApi.getFlavorApiForZone(zoneId);
    	List<FlavorView> flavorViewList = new ArrayList<FlavorView>();
    	for(Flavor flavor : flavorApi.listInDetail().concat()){
    		FlavorView flavorView = new FlavorView();
    		flavorView.setName(flavor.getName());
    		flavorView.setId(flavor.getId());
    		flavorView.setvCpus(String.valueOf(flavor.getVcpus()));
    		flavorView.setRam(String.valueOf(flavor.getRam()));
    		flavorView.setDisk(String.valueOf(flavor.getDisk()));
    		flavorViewList.add(flavorView);
    	}
    	return flavorViewList;
    }

    public List<ImageView> listImagesForZone(String zoneId){
        System.out.println("In Jclouds nova file");
    	ImageApi imageApi = novaApi.getImageApiForZone(zoneId);
    	List<ImageView> imageViewList = new ArrayList<ImageView>();
    	for(Image image:imageApi.listInDetail().concat()){
    		ImageView imageView = new ImageView();
    		imageView.setName(image.getName());
    		imageView.setId(image.getId());
    		imageViewList.add(imageView);
    	}
    	return imageViewList;
    }
    
    public List<Network> getNetworksForZone(String zoneId){
    	NetworkApi networkApi = neutronApi.getNetworkApi(zoneId);
        return networkApi.list().concat().toList();
    }
    
    public List<VMProperties> createNewInstance(String zone,String name,String imageId,String flavorId) throws Exception{
    	ServerApi serverApi = novaApi.getServerApiForZone(zone);
    	if(serverApi !=null){
    		//TODO change this use ServerCreated.builder
    		List<Network> networks = getNetworksForZone(zone);
    		if(networks.size() > 1){
    			String networkId = networks.get(0).getId();
    			CreateServerOptions options = CreateServerOptions.Builder.networks(networkId);
    			ServerCreated serverCreated = serverApi.create(name, imageId, flavorId,options);
    		}else{
    			ServerCreated serverCreated = serverApi.create(name, imageId, flavorId);
    		}
    		return listServers();
    	}else{
    		throw new Exception("Not able to get serverApi for zone:"+zone);
    	}
    }
    
    public List<VMProperties> deleteVM(String zone,String vmId) throws Exception{
    	ServerApi serverApi = novaApi.getServerApiForZone(zone);
    	boolean isDeleted = serverApi.delete(vmId);
    	if(isDeleted == true){
    		return listServers();
    	}else{
    		throw new Exception("unable to delete vm");
    	}
    }
    
    public List<LimitView> getLimitViewForTenant(String zone,String tenantId) throws Exception{
    	QuotaApi quotaApi = novaApi.getQuotaExtensionForZone(zone).get();
    	Map<String,LimitView> limitsView = new java.util.HashMap<String,LimitView>();
    	if(quotaApi != null){
    		Quota quota = quotaApi.getByTenant(tenantId);
    		
    		//set cpu's
    		LimitView cpusView = new LimitView();
    		cpusView.setName("VCPUs");
    		cpusView.setAvailable(quota.getCores());
    		limitsView.put(cpusView.getName(), cpusView);
    		
    		//set ram
    		LimitView memoryView = new LimitView();
    		memoryView.setName("RAM");
    		memoryView.setAvailable(quota.getRam());
    		limitsView.put(memoryView.getName(), memoryView);
    		
    		//set instances
    		LimitView instanceView = new LimitView();
    		instanceView.setName("Instances");
    		instanceView.setAvailable(quota.getInstances());
    		limitsView.put(instanceView.getName(), instanceView);
    		
    		populateUsedResources(limitsView);
    		
    		List<LimitView> limitViewList = new ArrayList<LimitView>();
    		for(Map.Entry<String, LimitView> limitView:limitsView.entrySet()){
    			limitViewList.add(limitView.getValue());
    		}
    		return limitViewList;
    	}else{
    		throw new Exception("unable to get Quota Deatils");
    	}
    }
  
    //TODO refractor and use list servers
    private void populateUsedResources(Map<String, LimitView> limitsView) {
		for(String zone:zones){
			ServerApi serverApi = novaApi.getServerApiForZone(zone);
			for(Server server:serverApi.listInDetail().concat()){
				String flavourId = server.getFlavor().getId();
            	FlavorApi flavourApi = novaApi.getFlavorApiForZone(zone);
            	Flavor flavor = flavourApi.get(flavourId);
            	
         
            	//RAM
            	LimitView memory = limitsView.get("RAM");
            	memory.setUsed(memory.getUsed()+flavor.getRam());
            	limitsView.put(memory.getName(), memory);
            	
            	LimitView cpus = limitsView.get("VCPUs");
            	cpus.setUsed(cpus.getUsed()+flavor.getVcpus());
            	limitsView.put(cpus.getName(), cpus);
            	
            	LimitView instances = limitsView.get("Instances");
            	instances.setUsed(instances.getUsed()+1);
            	limitsView.put(instances.getName(), instances);
            	
			}
		}
	}

	public List<VMProperties> listServers() {
    	StringBuilder s = new StringBuilder();
    	List<VMProperties> vmPropertiesList = new ArrayList<VMProperties>();
        for (String zone : zones) {
            ServerApi serverApi = novaApi.getServerApiForZone(zone);
           // System.out.println("Servers in " + zone);
           
            ImageApi imageApi = novaApi.getImageApiForZone(zone);

            for (Server server : serverApi.listInDetail().concat()) {
            	
            	//TODO change this to builder pattern
            	VMProperties vmProperty = new VMProperties();	
            	vmProperty.setZone(zone);
            	//TODO userName
            	vmProperty.setProject(server.getTenantId());
            	vmProperty.setHost(server.getExtendedAttributes().get().getHostName());
            	vmProperty.setVmName(server.getName());
            	
            	Image image = imageApi.get(server.getImage().getId());
            	vmProperty.setImageName(image.getName());

            	Multimap<String, Address> addressMap=server.getAddresses();
            	StringBuilder ipAddressBuilder = new StringBuilder();
            	for(Map.Entry<String, Address> address:addressMap.entries()){
            		ipAddressBuilder.append(address.getValue().getAddr()).append(",");
            	}
            	
            	if(!ipAddressBuilder.toString().isEmpty()){
            		vmProperty.setIpAddress(ipAddressBuilder.toString().substring(0,ipAddressBuilder.toString().length()-1));
            	}
            	
            	String flavourId = server.getFlavor().getId();
            	FlavorApi flavourApi = novaApi.getFlavorApiForZone(zone);
            	Flavor flavor = flavourApi.get(flavourId);
            	StringBuilder flavourBuilder = new StringBuilder();
            	flavourBuilder.append(flavor.getName()).append("|")
            	.append(flavor.getRam()).append("MB").append("|").append(flavor.getVcpus()).append("vCpu(s)").append("|")
            	.append(flavor.getDisk()).append("GB");
            	vmProperty.setSize(flavourBuilder.toString());
            	
            	vmProperty.setStatus(server.getStatus().value());
            	String taskState = server.getExtendedStatus().get().getTaskState();
            	vmProperty.setTask(taskState);
            	//TODO write a function to map int to stauts(active)
            	vmProperty.setPowerState(String.valueOf(server.getExtendedStatus().get().getPowerState()));
            	if(!"deleting".equalsIgnoreCase(taskState)){
            		vmPropertiesList.add(vmProperty);
            	}
            	
            }
        }
        return vmPropertiesList;
    }

    public void close() throws IOException {
        Closeables.close(novaApi, true);
        Closeables.close(neutronApi,true);
    }
}
