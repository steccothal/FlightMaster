package com.flight.manager.utils;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import com.flight.manager.config.JPAUtil;

public class TestWatcher implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        JPAUtil.setTestMode();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        JPAUtil.close();
    }
}
