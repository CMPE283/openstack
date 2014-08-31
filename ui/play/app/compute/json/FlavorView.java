package compute.json;

public class FlavorView {

	private String id;
	private String name;
	private String vCpus;
	private String ram;
	private String disk;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getvCpus() {
		return vCpus;
	}
	public void setvCpus(String vCpus) {
		this.vCpus = vCpus;
	}
	public String getRam() {
		return ram;
	}
	public void setRam(String ram) {
		this.ram = ram;
	}
	public String getDisk() {
		return disk;
	}
	public void setDisk(String disk) {
		this.disk = disk;
	}
	
	
}
