package io.github.sithengineer.motoqueiro

data class PermissionResponse(
    val permissionRequest: String,
    val requestCode: Int,
    val isGranted: Boolean
)
