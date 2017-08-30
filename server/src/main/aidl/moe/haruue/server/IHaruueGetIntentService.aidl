package moe.haruue.server;

import 	android.app.PendingIntent;
import android.content.Intent;

interface IHaruueGetIntentService {

    Intent getIntentForPendingIntent(in PendingIntent p);

}
