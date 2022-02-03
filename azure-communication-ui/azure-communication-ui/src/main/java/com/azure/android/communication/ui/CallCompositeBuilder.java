// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.communication.ui;

import com.azure.android.communication.ui.configuration.LocaleConfiguration;
import com.azure.android.communication.ui.configuration.ThemeConfiguration;
import com.azure.android.communication.ui.configuration.CallCompositeConfiguration;

/**
 * Builder for creating {@link CallComposite}.
 */
public final class CallCompositeBuilder {

    private ThemeConfiguration themeConfig = null;
    private LocaleConfiguration localeConfiguration = null;

    /**
     * Sets an optional theme for call-composite to use by {@link CallComposite}.
     *
     * @param theme {@link ThemeConfiguration}.
     * @return {@link CallCompositeBuilder}
     */
    public CallCompositeBuilder theme(final ThemeConfiguration theme) {
        this.themeConfig = theme;
        return this;
    }

    public  CallCompositeBuilder locale(final LocaleConfiguration locale) {
        this.localeConfiguration = locale;
        return this;
    }

    /**
     * Creates {@link CallComposite}.
     *
     * @return {@link CallComposite}
     */
    public CallComposite build() {
        final CallCompositeConfiguration config = new CallCompositeConfiguration();
        config.setThemeConfig(themeConfig);
        config.setLocaleConfiguration(localeConfiguration);

        return new CallComposite(config);
    }
}
