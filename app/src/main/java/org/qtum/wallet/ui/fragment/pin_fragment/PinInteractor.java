package org.qtum.wallet.ui.fragment.pin_fragment;

import org.bitcoinj.wallet.Wallet;
import javax.crypto.Cipher;
import rx.Observable;

public interface PinInteractor {
    String getPassword();
    void savePassword(String password);
    void savePassphraseSaltWithPin(String pin, String passphrase);
    byte[] getSaltPassphrase();
    String getTouchIdPassword();
    void saveTouchIdPassword(String password);
    Observable<Wallet> loadWalletFromFile();
    Observable<String> createWallet();
    void setKeyGeneratedInstance(boolean isKeyGenerated);
    String decode(String encoded,Cipher cipher);
    Observable<String> encodeInBackground(String pin);
    String generateSHA256String(String pin);
    String getUnSaltPassphrase(String oldPin);
}
