package com.azure.android.communication.ui.configuration

internal class LocaleConfiguration {
    private var language: String? = ""
    private var isRTL: Boolean? = null
    private var customString: MutableMap<String,String>? = mutableMapOf()

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

    constructor(customString: HashMap<String, String>) {
        this.customString = customString
    }
}