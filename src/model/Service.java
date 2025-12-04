package model;

public class Service {
	private int serviceId;
    private String serviceName;
    private String serviceDesc;
    private int servicePrice;
    private int serviceDuration;
    private boolean isDeleted;

    public Service(int serviceId, String serviceName, String serviceDesc, int servicePrice, int serviceDuration, boolean isDeleted) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDesc = serviceDesc;
        this.servicePrice = servicePrice;
        this.serviceDuration = serviceDuration;
        this.isDeleted = isDeleted;
    }

	public int getServiceId() {
		return serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public int getServicePrice() {
		return servicePrice;
	}

	public int getServiceDuration() {
		return serviceDuration;
	}
	
	public boolean isDeleted() {
		return isDeleted; 
	}
    
	// Override for easy display in ComboBoxes
    @Override
    public String toString() {
        return serviceName; 
    }
    
    
}
