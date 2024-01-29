package mono.connect.kit;

public class MonoCustomerIdentity {
    private String type;
    private String number;

    public MonoCustomerIdentity(String type, String number) {
        this.type = type;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
