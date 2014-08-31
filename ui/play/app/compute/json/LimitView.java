package compute.json;

public class LimitView {

	private String name;
	private long available;
	private long used;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getAvailable() {
		return available;
	}
	public void setAvailable(long available) {
		this.available = available;
	}
	public long getUsed() {
		return used;
	}
	public void setUsed(long used) {
		this.used = used;
	}
	
}
