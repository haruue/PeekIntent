package android.app;

import android.os.Binder;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */


public abstract class ActivityManagerNative extends Binder implements IActivityManager {

    static public IActivityManager getDefault() {
        throw new RuntimeException("Stub!");
    }

}
