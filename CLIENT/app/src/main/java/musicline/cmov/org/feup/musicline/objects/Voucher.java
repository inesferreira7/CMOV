package musicline.cmov.org.feup.musicline.objects;

import java.io.Serializable;

public class Voucher implements Serializable {
    String id, customerId, type;
    boolean isUsed;

    public Voucher(String id, String customerId, String type, boolean isUsed) {
        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.isUsed = isUsed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}
