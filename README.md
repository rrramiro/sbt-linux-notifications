# sbt-linux-notifications
Notify sbt result tests on NotifyOSD

## Configuring Icons

By default the plugin looks for icons in `~/.sbt/notifications/icons/`.  Specifically, it looks for:

* `passed.png` - used when tests pass
* `failed.png` - used when tests fail
* `error.png` - used for catastrophic failures

The directory in which the plugin looks for icons can be configured by adding this to your `build.sbt` file:

    notificationsDefaultImagePath := "/my/better/path"

You can configure images individually.  e.g.

    notificationsErrorImagePath := /better/error/icon.png

    notificationsPassedImagePath := /better/pass/icon.png

    notificationsFailedImagePath := /better/fail/icon.png

