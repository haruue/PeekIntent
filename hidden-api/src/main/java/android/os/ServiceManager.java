package android.os;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

@SuppressWarnings({"unused", "SameParameterValue"})
public final class ServiceManager {
    public static IBinder getService(String name) {
        throw new RuntimeException("Stub!");
    }

    public static IBinder getServiceOrThrow(String name) throws ServiceNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public static void addService(String name, IBinder service) {
        throw new RuntimeException("Stub!");
    }

    public static void addService(String name, IBinder service, boolean allowIsolated) {
        throw new RuntimeException("Stub!");
    }

    @SuppressWarnings("WeakerAccess")
    public static class ServiceNotFoundException extends Exception {

    }
}
