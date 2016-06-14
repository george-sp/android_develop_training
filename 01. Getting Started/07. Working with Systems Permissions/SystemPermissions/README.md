#### Check For Permissions

If your app needs a **dangerous permission**, you must check whether you have that permission **every time** you perform an operation that requires that permission. 

The user is always free to revoke the permission, so even if the app used the camera yesterday, it can't assume it still has that permission today.

> To check if you have a permission, call the **`ContextCompat.checkSelfPermission()`** method.

 
For example, this snippet shows how to check if the activity has permission to write to the calendar:

```
// Assume thisActivity is the current activity
int permissionCheck = ContextCompat.checkSelfPermission(thisActivity,
        Manifest.permission.WRITE_CALENDAR);
```

If the app has the permission, the method returns 

> **_PackageManager.PERMISSION_GRANTED_**

, and the app can proceed with the operation. If the app does not have the permission, the method returns 

> **_PackageManager.PERMISSION_DENIED_**

, and the app has to explicitly ask the user for permission.

