package mono.connect.kit;

public class MonoCustomer {
    private String id;
    private String name;
    private String email;
    private MonoCustomerIdentity identity;

    public MonoCustomer(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Both name and email are required when id is not provided");
        }

        this.id = id;
    }

    public MonoCustomer(String name, String email, MonoCustomerIdentity identity) {
        this.name = name;
        this.email = email;
        this.identity = identity;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

