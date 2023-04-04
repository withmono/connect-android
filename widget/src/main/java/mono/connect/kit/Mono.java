package mono.connect.kit;

public class Mono {

    public static ConnectKit create(MonoConfiguration config){

        MonoWebInterface.getInstance().reset();

        return new ConnectKit(config);

    }

    public static ConnectKit reauthorise(MonoConfiguration config){

        MonoWebInterface.getInstance().reset();

        return new ConnectKit(config);

    }

}
