package mono.connect.kit;

public class MonoCustomer {
    private String id;
    private String name;
    private String email;
    private MonoIdentity identity;

    public MonoCustomer(String id) {
        this.id = id;
    }

    public MonoCustomer(String name, String email, MonoIdentity identity) {
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

