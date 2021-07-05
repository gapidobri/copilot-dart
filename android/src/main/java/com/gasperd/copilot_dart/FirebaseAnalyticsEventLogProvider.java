package com.gasperd.copilot_dart;

import android.content.Context;
import android.os.Bundle;

import com.copilot.analytics.EventLogProvider;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.gasperd.copilot_dart.CopilotDartPlugin.channel;

public class FirebaseAnalyticsEventLogProvider implements EventLogProvider {

    private static final String TAG = FirebaseAnalyticsEventLogProvider.class.getSimpleName();

    @NotNull
    private final Context mContext;

    public FirebaseAnalyticsEventLogProvider(@NotNull Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public String getProviderName() {
        return TAG;
    }

    @Override
    public void activate() {
        // No implementation
    }

    @Override
    public void deactivate() {
        // No implementation
    }

    @Override
    public void setUserId(String userId) {
        // FirebaseAnalytics.getInstance(mContext).setUserId(userId);
        channel.invokeMethod("setUserId", userId);
    }

    @Override
    public Map<String, String> transformParameters(Map<String, String> parameters) {
        return parameters;
    }

    @Override
    public void logEvent(@NotNull String eventName, Map<String, String> transformedParams) {
        Bundle bundle = buildEventBundle(transformedParams);
        // FirebaseAnalytics.getInstance(mContext).logEvent(eventName, bundle);
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("eventName", eventName);
        arguments.put("transformedParams", transformedParams);
        channel.invokeMethod("logEvent", arguments);
    }

    private Bundle buildEventBundle(Map<String, String> parameters) {
        Bundle params = new Bundle();

        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            String key = parameter.getKey();
            String value = parameter.getValue();
            params.putString(key, value);
        }
        return params;
    }
}