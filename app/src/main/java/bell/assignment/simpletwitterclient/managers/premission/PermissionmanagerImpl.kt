package bell.assignment.simpletwitterclient.managers.premission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import bell.assignment.simpletwitterclient.R

object PermissionmanagerImpl: PermissionManager {

    private var requestCode = PermissionManager.PERMISSION_REQUEST_CODE
    private var onPermissionResult: ((premissionResult: Map<String, Int>) -> Unit)? = null

    override fun checkPermissions(
        activity: Activity,
        permissions: Array<out String>,
        onPermissionResult: ((premissionResult: Map<String, Int>) -> Unit)?,
        requestCode: Int?
    ): Boolean {
        // For API >= 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            this.onPermissionResult = onPermissionResult
            this.requestCode = requestCode ?: PermissionManager.PERMISSION_REQUEST_CODE

            val notGrantedPermissionList = mutableListOf<String>()
            permissions.forEach { permission ->
                if (ContextCompat.checkSelfPermission(
                        activity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    notGrantedPermissionList.add(permission)
                }
            }

            if (notGrantedPermissionList.isNotEmpty()) {

                var showDialog = false
                notGrantedPermissionList.forEach {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {
                        showDialog = true
                    }
                }

                if (showDialog) {

                    showPermissionDialog(
                        activity,
                        notGrantedPermissionList.toTypedArray(),
                        this.requestCode
                    )

                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(activity, permissions,
                        this.requestCode
                    )

                }

                return false
            } else {
                // all permissions has already been granted
                val result = mutableMapOf<String, Int>()
                permissions.forEachIndexed { _, permission ->
                    result[permission] = PackageManager.PERMISSION_GRANTED
                }
                onPermissionResult?.invoke(result)
                return true
            }
        } else {

            // the sdk is lower then API 23 so no permission handling needs to be used
            val result = mutableMapOf<String, Int>()
            permissions.forEach { permission ->
                result[permission] = PackageManager.PERMISSION_GRANTED
            }
            onPermissionResult?.invoke(result)
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean? {
        if (requestCode == requestCode) {
            if (grantResults.isNotEmpty()) {
                val result = mutableMapOf<String, Int>()
                permissions.forEachIndexed { index, permission ->
                    result[permission] = grantResults[index]
                }
                onPermissionResult?.invoke(result)

            } else {
                val result = mutableMapOf<String, Int>()
                permissions.forEachIndexed { index, permission ->
                    result[permission] = grantResults[index]
                }
                onPermissionResult?.invoke(result)
            }
            return true
        }
        return false
    }

    override fun areAllPermissionGranted(result: Map<String, Int>): Boolean {
        var allGranted = true
        result.values.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                allGranted = false
            }
        }
        return allGranted
    }

    private fun showPermissionDialog(
        activity: Activity,
        permissions: Array<String>,
        requestCode: Int
    ) {
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.permission_dialog_title))
            .setMessage(activity.getString(R.string.permission_dialog_message))

        alertDialog.setPositiveButton(activity.getString(R.string.allow)) { dialog, _ ->
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
            dialog.dismiss()
        }
        alertDialog.setNegativeButton(activity.getString(R.string.deny), null)

        alertDialog.create().show()
    }
}