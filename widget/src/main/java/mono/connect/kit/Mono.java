package mono.connect.kit;

public class Mono {

    public static ConnectKit create(MonoConfiguration config){

        return new ConnectKit(config);

    }

    public static ConnectKit reauthorise(MonoConfiguration config){

        return new ConnectKit(config);

    }

}
