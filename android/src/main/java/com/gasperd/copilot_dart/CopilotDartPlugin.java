package com.gasperd.copilot_dart;

import android.app.Application;

import androidx.annotation.NonNull;

import com.copilot.analytics.EventLogProvider;
import com.copilot.analytics.predifined.AcceptTermsAnalyticsEvent;
import com.copilot.analytics.predifined.ConsumableDepletedAnalyticsEvent;
import com.copilot.analytics.predifined.ConsumableUsageAnalyticsEvent;
import com.copilot.analytics.predifined.ContactSupportAnalyticsEvent;
import com.copilot.analytics.predifined.ErrorAnalyticsEvent;
import com.copilot.analytics.predifined.FirmwareUpgradeCompletedAnalyticsEvent;
import com.copilot.analytics.predifined.FirmwareUpgradeStartedAnalyticsEvent;
import com.copilot.analytics.predifined.LoggedInAnalyticsEvent;
import com.copilot.analytics.predifined.LoggedOutAnalyticsEvent;
import com.copilot.analytics.predifined.OnBoardingEndedAnalyticsEvent;
import com.copilot.analytics.predifined.OnBoardingStartedAnalyticsEvent;
import com.copilot.analytics.predifined.ScreenLoadAnalyticsEvent;
import com.copilot.analytics.predifined.SignupAnalyticsEvent;
import com.copilot.analytics.predifined.SuccessfulElevateAnonymousAnalyticsEvent;
import com.copilot.analytics.predifined.TapConnectDeviceAnalyticsEvent;
import com.copilot.analytics.predifined.TapMenuAnalyticsEvent;
import com.copilot.analytics.predifined.TapMenuItemAnalyticsEvent;
import com.copilot.analytics.predifined.ThingConnectedAnalyticsEvent;
import com.copilot.analytics.predifined.ThingConnectionFailedAnalyticsEvent;
import com.copilot.analytics.predifined.ThingDiscoveredAnalyticsEvent;
import com.copilot.analytics.predifined.ThingInfoAnalyticsEvent;
import com.copilot.core.Copilot;

import java.util.ArrayList;
import java.util.List;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * CopilotDartPlugin
 */
public class CopilotDartPlugin extends Application implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    public static MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "copilot_dart");
        channel.setMethodCallHandler(this);

        initCopilot();
    }

    private void initCopilot() {
        List<EventLogProvider> providers = new ArrayList<>();
        providers.add(new FirebaseAnalyticsEventLogProvider(this));
        Copilot.setup(this, providers);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

        switch (call.method) {
            case "sessionStarted" -> {
                String userId = call.argument("userId");
                boolean isCopilotAnalysisConsentApproved = call.argument("isCopilotAnalysisConsentApproved");
                if (userId == null) {
                    result.error("NULL", "User id is null", null);
                    return;
                }
                Copilot.getInstance().Manage.YourOwn.sessionStarted(userId, isCopilotAnalysisConsentApproved);
            }
            case "sessionEnded" -> Copilot.getInstance().Manage.YourOwn.sessionEnded();
            case "setCopilotAnalysisConsent" -> {
                boolean isConsentApproved = call.argument("isConsentApproved");
                Copilot.getInstance().Manage.YourOwn.setCopilotAnalysisConsent(isConsentApproved);
            }
            case "logSignupEvent" -> Copilot.getInstance().Report.logEvent(new SignupAnalyticsEvent());
            case "logLoginEvent" -> Copilot.getInstance().Report.logEvent(new LoggedInAnalyticsEvent());
            case "logLogoutEvent" -> Copilot.getInstance().Report.logEvent(new LoggedOutAnalyticsEvent());
            case "logSuccessfulElevateAnonymousEvent" -> Copilot.getInstance().Report.logEvent(new SuccessfulElevateAnonymousAnalyticsEvent());
            case "logAcceptTermsOfUseEvent" -> {
                String version = call.argument("version");
                if (version == null) {
                    result.error("NULL", "Version is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new AcceptTermsAnalyticsEvent(version));
            }
            case "logContactSupportEvent" -> {
                String supportCase = call.argument("supportCase");
                String thingId = call.argument("thingId");
                String screenName = call.argument("screenName");
                if (supportCase == null) {
                    result.error("NULL", "Support case is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new ContactSupportAnalyticsEvent(supportCase, thingId, screenName));
            }
            case "logOnBoardingStartedEvent" -> {
                String flowId = call.argument("flowId");
                String screenName = call.argument("screenName");
                if (flowId == null) {
                    result.error("NULL", "Flow id is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new OnBoardingStartedAnalyticsEvent(flowId, screenName));
            }
            case "logOnBoardingEndedEvent" -> {
                String flowId = call.argument("flowId");
                String screenName = call.argument("screenName");
                if (flowId == null) {
                    result.error("NULL", "Flow id is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new OnBoardingEndedAnalyticsEvent(flowId, screenName));
            }
            case "logTapConnectDeviceEvent" -> Copilot.getInstance().Report.logEvent(new TapConnectDeviceAnalyticsEvent());
            case "logThingConnectedEvent" -> {
                String thingId = call.argument("thingId");
                String screenName = call.argument("screenName");
                Copilot.getInstance().Report.logEvent(new ThingConnectedAnalyticsEvent(thingId, screenName));
            }
            case "logThingDiscoveredEvent" -> {
                String thingId = call.argument("thingId");
                Copilot.getInstance().Report.logEvent(new ThingDiscoveredAnalyticsEvent(thingId));
            }
            case "logThingInfoEvent" -> {
                String thingFirmware = call.argument("thingFirmware");
                String thingModel = call.argument("thingModel");
                String thingId = call.argument("thingId");
                Copilot.getInstance().Report.logEvent(new ThingInfoAnalyticsEvent(thingFirmware, thingModel, thingId));
            }
            case "logThingConnectionFailedEvent" -> {
                String failureReason = call.argument("failureReason");
                Copilot.getInstance().Report.logEvent(new ThingConnectionFailedAnalyticsEvent(failureReason));
            }
            case "logConsumableUsageEvent" -> {
                String screenName = call.argument("screenName");
                String consumableType = call.argument("consumableType");
                String thingId = call.argument("thingId");
                Copilot.getInstance().Report.logEvent(new ConsumableUsageAnalyticsEvent(screenName, consumableType, thingId));
            }
            case "logConsumableDepletedEvent" -> {
                String screenName = call.argument("screenName");
                String consumableType = call.argument("consumableType");
                String thingId = call.argument("thingId");
                Copilot.getInstance().Report.logEvent(new ConsumableDepletedAnalyticsEvent(screenName, consumableType, thingId));
            }
                case "logScreenLoadEvent" -> {
                String screenName = call.argument("screenName");
                if (screenName == null) {
                    result.error("NULL", "Screen name is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new ScreenLoadAnalyticsEvent(screenName));
            }
            case "logTapMenuEvent" -> {
                String screenName = call.argument("screenName");
                if (screenName == null) {
                    result.error("NULL", "Screen name is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new TapMenuAnalyticsEvent(screenName));
            }
            case "logTapMenuItemEvent" -> {
                String menuItem = call.argument("menuItem");
                String screenName = call.argument("screenName");
                Copilot.getInstance().Report.logEvent(new TapMenuItemAnalyticsEvent(menuItem, screenName));
            }
            case "logFirmwareUpgradeStartedEvent" -> Copilot.getInstance().Report.logEvent(new FirmwareUpgradeStartedAnalyticsEvent());
            case "logFirmwareUpgradeCompletedEvent" -> {
                String firmwareUpgradeStatusString = call.argument("firmwareUpgradeStatus");
                if (firmwareUpgradeStatusString == null) {
                    result.error("NULL", "Firmware upgrade status name is null", null);
                    return;
                }
                FirmwareUpgradeCompletedAnalyticsEvent.FirmwareUpgradeCompletedStatus firmwareUpgradeStatus;
                if (firmwareUpgradeStatusString.equals("success")) {
                    firmwareUpgradeStatus = FirmwareUpgradeCompletedAnalyticsEvent.FirmwareUpgradeCompletedStatus.Success;
                } else {
                    firmwareUpgradeStatus = FirmwareUpgradeCompletedAnalyticsEvent.FirmwareUpgradeCompletedStatus.Failure;
                }
                Copilot.getInstance().Report.logEvent(new FirmwareUpgradeCompletedAnalyticsEvent(firmwareUpgradeStatus));
            }
            case "logErrorReportEvent" -> {
                String error = call.argument("error");
                String screenName = call.argument("screenName");
                Copilot.getInstance().Report.logEvent(new ErrorAnalyticsEvent(error, screenName));
            }
            default -> result.notImplemented();
        }

        result.success(null);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
