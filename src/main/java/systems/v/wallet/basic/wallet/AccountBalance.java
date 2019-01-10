package systems.v.wallet.basic.wallet;

public interface AccountBalance {

    long getRegular();

    long getMintingAverage();

    long getAvailable();

    long getEffective();

    int getHeight();
}
