package backupObjects;

import java.util.Map;

public abstract class BackupObject {
    protected String categoryName;
    protected String calendar;
    protected String account;
    protected Map<String, String> mainDetails;
    protected Map<String, String> detView;

    public BackupObject(String categoryName, String calendar, String account) {
        this.categoryName = categoryName;
        this.calendar = calendar;
        this.account = account;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCalendar() {
        return calendar;
    }

    public String getAccount() {
        return account;
    }

    public Map<String, String> mainDetails () {
        return mainDetails;
    }
    public Map<String, String> detView () {
        return detView;
    }
}
