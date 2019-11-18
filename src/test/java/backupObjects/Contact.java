package backupObjects;

import java.util.HashMap;

public class Contact extends BackupObject {
    private String name;
    private String email;
    private String phone;
    private String updated;
    private String address;

    public Contact(String name, String email, String phone, String updated, String address,
                   String categoryName, String calendar, String account) {
        super(categoryName, calendar, account);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.updated = updated;
        this.address = address;
        mainDetails = new HashMap<>();
        mainDetails.put("fullName", this.name);
        mainDetails.put("primaryEmail", this.email);
        //mainDetails.put("jobTitle", this.jobTitle);
        //mainDetails.put("deletedAt", this.deletedAt);
        detView = new HashMap<>();
        detView.put("addresses", this.address);
        detView.put("emails", this.email);
        detView.put("phones", this.phone);
    }

    public String getAddress() {
        return address;
    }

    public String getUpdated() {
        return updated;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

}
