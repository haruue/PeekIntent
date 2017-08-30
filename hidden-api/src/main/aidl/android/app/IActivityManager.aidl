package android.app;

import android.content.Intent;
import android.content.IIntentSender;

interface IActivityManager {

    Intent getIntentForIntentSender(in IIntentSender sender);

}
