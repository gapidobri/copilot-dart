package com.gasperd.copilot_dart;

import com.copilot.core.facade.impl.manage.auth.CopilotTokenProvider;
import com.copilot.core.facade.impl.manage.auth.exceptions.ConnectivityException;
import com.copilot.core.facade.impl.manage.auth.exceptions.GeneralErrorException;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import io.flutter.plugin.common.MethodChannel;

import static com.gasperd.copilot_dart.CopilotDartPlugin.channel;

public class DartCopilotTokenProvider implements CopilotTokenProvider {

    @Override
    public String generateUserAuthKey(String userId) throws GeneralErrorException, ConnectivityException {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("userId", userId);
        SimpleSettableFuture<String> future = new SimpleSettableFuture<>();
        channel.invokeMethod("generateUserAuthKey", arguments, new MethodChannel.Result() {
            @Override
            public void success(Object result) {
                String authKey = (String) ((HashMap<String, Object>) result).get("authKey");
                future.set(authKey);
            }

            @Override
            public void error(String errorCode, String errorMessage, Object errorDetails) {
                future.set(null);
            }

            @Override
            public void notImplemented() {
                future.set(null);
            }
        });

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

}
