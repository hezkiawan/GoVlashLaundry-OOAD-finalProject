package controller;

import java.util.Vector;

import database.ServiceDAO;
import model.Service;

public class ServiceController {
	private ServiceDAO serviceDAO = new ServiceDAO();

    public Vector<Service> getAllServices() {
        return serviceDAO.getAllServices();
    }

    // Validate and Insert/Update 
    public String saveService(String idStr, String name, String desc, String priceStr, String durationStr, boolean isUpdate) {
        
        // 1. Validate Name (Cannot be empty, <= 50 chars)
        if (name.isEmpty()) return "Service Name cannot be empty.";
        if (name.length() > 50) return "Service Name must be <= 50 characters.";

        // 2. Validate Description (Cannot be empty, <= 250 chars)
        if (desc.isEmpty()) return "Description cannot be empty.";
        if (desc.length() > 250) return "Description must be <= 250 characters.";

        // 3. Validate Price (ust be > 0)
        int price = 0;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            return "Price must be a number.";
        }
        if (price <= 0) return "Price must be greater than 0.";

        // 4. Validate Duration (Between 1 and 30 days)
        int duration = 0;
        try {
            duration = Integer.parseInt(durationStr);
        } catch (NumberFormatException e) {
            return "Duration must be a number.";
        }
        if (duration < 1 || duration > 30) return "Duration must be between 1 and 30 days.";

        // EXECUTE
        boolean ok;
        if (isUpdate) {
            if (idStr == null || idStr.isEmpty()) {
                return "Service ID is missing.";
            }
            int id = Integer.parseInt(idStr);
            Service s = new Service(id, name, desc, price, duration, false);
            ok = serviceDAO.updateService(s);
        } else {
            Service s = new Service(0, name, desc, price, duration, false);
            ok = serviceDAO.insertService(s);
        }

        return ok ? "Success" : "Failed to save service.";
    }

    public String deleteService(int id) {
        boolean ok = serviceDAO.deleteService(id);
        return ok ? "Success" : "Failed to delete service.";
    }
}
