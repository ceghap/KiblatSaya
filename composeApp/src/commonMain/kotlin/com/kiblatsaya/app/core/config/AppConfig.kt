package com.kiblatsaya.app.core.config

import com.kiblatsaya.app.core.AppBuildInfo

object AppConfig {
    const val APP_NAME = "KiblatSaya"
    val VERSION_NAME: String get() = AppBuildInfo.VERSION_NAME
    val VERSION_DISPLAY: String get() = AppBuildInfo.VERSION_DISPLAY
    val VERSION_CODE: Int get() = AppBuildInfo.VERSION_CODE
    
    const val SUBTITLE = "Arah ke Kaabah Suci, Makkah"
    const val COMPANY_NAME = "Ashraf Systems"
    const val COMPANY_LEGAL_NAME = "Ashraf Systems Enterprise"
    const val COPYRIGHT = "© 2026 Ashraf Systems Enterprise"
    const val WEBSITE_URL = "https://ashrafsystems.com"
    const val PRIVACY_URL = "https://ashrafsystems.com/privacy#kiblatsaya"
    
    // Cross-promotion: Wiridly
    const val WIRIDLY_TITLE = "Wiridly: Tasbih Digital"
    const val WIRIDLY_SUBTITLE = "Buku wirid, zikir & tasbih digital tanpa iklan"
    const val WIRIDLY_PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.wiridly.app"
}
