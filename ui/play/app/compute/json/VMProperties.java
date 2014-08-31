package compute.json;

import java.util.List;

/**
 * 
 * @author Sowmya
 *Contains the properties of VM.
 *This class is used for listing all VM's a particular user has created.
 */
public class VMProperties {
	private String project;
	private String host;
	private String vmName;
	private String imageName;
	private String ipAddress;
	private String size;
	private String status;
	private String task;
	private String powerState;
	private String upTime;
	private String zone;
	
	public String getZone() {
		return zone;
	}
	
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String string) {
		this.ipAddress = string;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getPowerState() {
		return powerState;
	}
	public void setPowerState(String powerState) {
		this.powerState = powerState;
	}
	public String getUpTime() {
		return upTime;
	}
	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}
	
}
