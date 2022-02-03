package com.azure.android.communication.ui.configuration

import org.json.JSONObject

internal class LocaleConfiguration {
    private var language: String? = ""
    private var isRTL: Boolean? = null
    private var customString = HashMap<String, String>()

    constructor (
        language: String?,
        isRTL: Boolean?,
        customString: HashMap<String, String>,
    ) {
        this.language = language
        this.customString = customString
        this.isRTL = isRTL
    }

    constructor(language: String?, isRTL: Boolean?) {
        this.language = language
        this.isRTL = isRTL
    }
}