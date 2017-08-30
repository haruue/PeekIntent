# Peek Intent
A tool to peek intent from Notification.contentIntent

## Overview
From [Rikka's issue](https://github.com/RikkaW/FCM-for-Mojo/issues/14)

> Calling `pendingIntent.getIntent();` will invoke `ActivityManagerNative.getDefault().getIntentForIntentSender(pendingIntent.mTarget);` eventually. (Need a signature protected level permission on Android 7.+) 
>
> So we use a indirect method, start a process use `app_process` with root permission and add a system service by `ServiceManager.addService`, then provide `getIntentForIntentSender` in this service, which can be used to call API by other applications who originally don't have permission to call. 

This application is a demo implemention of this method. 

## Usage
1. [Download](https://github.com/haruue/PeekIntent/releases/latest) and install the apk. 
2. Connect your device with adb, and execute following commands: 

```shell
adb shell

su

# workaround of [Transaction failed](https://cfp.vim-cn.com/cbd1s)
sepolicy-inject --live "allow untrusted_app su binder transfer"
sepolicy-inject --live "allow untrusted_app su binder call"

# start service by app_process
CLASSPATH=$(echo /data/app/moe.haruue.peekintent-*/base.apk) \
nohup app_process /system/bin --nice-name=higis moe.haruue.server.Main > /dev/null 2>&1 &
```

3. Grant the notification listener permission to this application. 
4. Start the application, click the notification items in MainActivity to see or copy the intent information. 

## Troubleshooting
#### Why does it just tell me `notification.intent == null` when I click the notification item? 
In addition to some notification do not have a contentIntent, if the service which provides the `getIntentForIntentSender` does not start normally, service will not be able to get the intent of the notification. Check whether you have started the service successfully. 

Please note we only try once to get the intent of the notification. If you want to try again, you need retrigger the notification. 

You can use [Trigger test notification] to trigger a notification for a test. 

## License
Apache License 2.0

```License
Copyright 2017 Haruue Icymoon

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
