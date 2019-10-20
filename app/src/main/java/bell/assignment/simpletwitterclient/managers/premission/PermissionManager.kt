package bell.assignment.simpletwitterclient.managers.premission

import android.app.Activity

interface PermissionManager {

    companion object {
        const val PERMISSION_REQUEST_CODE = 1001
    }

    /**
     * check requried permission
     */
    fun checkPermissions(
        activity: Activity,
        permissions: Array<out String>,
        onPermissionResult: ((premissionResult: Map<String, Int>) -> Unit)?,
        requestCode: Int? = null
    ): Boolean

    /**
     * handle permission result
     */
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean?

    /**
     * return ture if all permissions granted
     */
    fun areAllPermissionGranted(
        result: Map<String, Int>
    ): Boolean
}