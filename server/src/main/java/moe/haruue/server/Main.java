package moe.haruue.server;

import android.os.Looper;
import android.os.ServiceManager;
import android.support.annotation.Keep;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

@Keep
public class Main {

    public static void main(String[] args) throws Exception {
        Looper.prepare();
        ServiceManager.addService(HaruueGetIntentService.NAME, new HaruueGetIntentService());
        Looper.loop();
    }

}
