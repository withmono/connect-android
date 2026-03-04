package mono.connect.kit;

public class MonoInstitution {

    private String id;
    private String auth_method;
    private String account_number;

    public MonoInstitution(String id, String auth_method, String account_number){
        this.id = id;
        this.auth_method = auth_method;
        this.account_number = account_number;
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
    public String getAccountNumber(){
        return this.account_number;
    }
    public void setAccountNumber(String account_number){
        this.account_number = account_number;
    }
}
