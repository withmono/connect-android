package mono.connect.kit;

public class MonoInstitution {

    private String id;
    private String auth_method;

    public MonoInstitution(String id, String auth_method){
        this.id = id;
        this.auth_method = auth_method;
    }

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getAuthMethod(){
        return this.auth_method;
    }
    public void setAuthMethod(String auth_method){
        this.auth_method = auth_method;
    }
}
