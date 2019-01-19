package systems.v.wallet.basic.wallet;

import static systems.v.wallet.basic.wallet.Wallet.MAIN_NET;
import static systems.v.wallet.basic.wallet.Wallet.TEST_NET;

public class Agent {

    private static final String WALLET_SPECIFICATION_VERSION = "1.0";

    private String name;
    private String version;
    private String network;

    public Agent(String name, String version, String network) {
        this.name = name;
        this.version = version;
        this.network = network;
    }

    @Override
    public String toString() {
        String net = "";
        switch (network) {
            case MAIN_NET:
                net = "mainnet";
                break;
            case TEST_NET:
                net = "testnet";
        }
        return "V Systems Wallet Specification:" + WALLET_SPECIFICATION_VERSION + "/" + name + ":" + version + "/" + net;
    }
}
