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
import com.copilot.core.facade.impl.manage.auth.CopilotTokenProvider;

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
        CopilotTokenProvider tokenProvider = new DartCopilotTokenProvider();
        Copilot.getInstance().Manage.YourOwn.Auth.setCopilotTokenProvider(tokenProvider);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

        String screenName, flowId, thingId, consumableType;

        switch (call.method) {

            case "sessionStarted":
                String userId = call.argument("userId");
                boolean isCopilotAnalysisConsentApproved = call.argument("isCopilotAnalysisConsentApproved");
                if (userId == null) {
                    result.error("NULL", "User id is null", null);
                    return;
                }
                Copilot.getInstance().Manage.YourOwn.sessionStarted(userId, isCopilotAnalysisConsentApproved);
                break;

            case "sessionEnded":
                Copilot.getInstance().Manage.YourOwn.sessionEnded();
                break;

            case "setCopilotAnalysisConsent":
                boolean isConsentApproved = call.argument("isConsentApproved");
                Copilot.getInstance().Manage.YourOwn.setCopilotAnalysisConsent(isConsentApproved);
                break;

            case "logSignupEvent":
                Copilot.getInstance().Report.logEvent(new SignupAnalyticsEvent());
                break;

            case "logLoginEvent":
                Copilot.getInstance().Report.logEvent(new LoggedInAnalyticsEvent());
                break;

            case "logLogoutEvent":
                Copilot.getInstance().Report.logEvent(new LoggedOutAnalyticsEvent());
                break;

            case "logSuccessfulElevateAnonymousEvent":
                Copilot.getInstance().Report.logEvent(new SuccessfulElevateAnonymousAnalyticsEvent());
                break;

            case "logAcceptTermsOfUseEvent":
                String version = call.argument("version");
                if (version == null) {
                    result.error("NULL", "Version is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new AcceptTermsAnalyticsEvent(version));
                break;

            case "logContactSupportEvent":
                String supportCase = call.argument("supportCase");
                thingId = call.argument("thingId");
                screenName = call.argument("screenName");
                if (supportCase == null) {
                    result.error("NULL", "Support case is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new ContactSupportAnalyticsEvent(supportCase, thingId, screenName));
                break;

            case "logOnBoardingStartedEvent":
                flowId = call.argument("flowId");
                screenName = call.argument("screenName");
                if (flowId == null) {
                    result.error("NULL", "Flow id is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new OnBoardingStartedAnalyticsEvent(flowId, screenName));
                break;

            case "logOnBoardingEndedEvent":
                flowId = call.argument("flowId");
                screenName = call.argument("screenName");
                if (flowId == null) {
                    result.error("NULL", "Flow id is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new OnBoardingEndedAnalyticsEvent(flowId, screenName));
                break;

            case "logTapConnectDeviceEvent":
                Copilot.getInstance().Report.logEvent(new TapConnectDeviceAnalyticsEvent());
                break;

            case "logThingConnectedEvent":
                thingId = call.argument("thingId");
                screenName = call.argument("screenName");
                Copilot.getInstance().Report.logEvent(new ThingConnectedAnalyticsEvent(thingId, screenName));
                break;

            case "logThingDiscoveredEvent":
                thingId = call.argument("thingId");
                Copilot.getInstance().Report.logEvent(new ThingDiscoveredAnalyticsEvent(thingId));
                break;

            case "logThingInfoEvent":
                String thingFirmware = call.argument("thingFirmware");
                String thingModel = call.argument("thingModel");
                thingId = call.argument("thingId");
                Copilot.getInstance().Report.logEvent(new ThingInfoAnalyticsEvent(thingFirmware, thingModel, thingId));
                break;

            case "logThingConnectionFailedEvent":
                String failureReason = call.argument("failureReason");
                Copilot.getInstance().Report.logEvent(new ThingConnectionFailedAnalyticsEvent(failureReason));
                break;

            case "logConsumableUsageEvent":
                screenName = call.argument("screenName");
                consumableType = call.argument("consumableType");
                thingId = call.argument("thingId");
                Copilot.getInstance().Report.logEvent(new ConsumableUsageAnalyticsEvent(screenName, consumableType, thingId));
                break;

            case "logConsumableDepletedEvent":
                screenName = call.argument("screenName");
                consumableType = call.argument("consumableType");
                thingId = call.argument("thingId");
                Copilot.getInstance().Report.logEvent(new ConsumableDepletedAnalyticsEvent(screenName, consumableType, thingId));
                break;

            case "logScreenLoadEvent":
                screenName = call.argument("screenName");
                if (screenName == null) {
                    result.error("NULL", "Screen name is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new ScreenLoadAnalyticsEvent(screenName));
                break;

            case "logTapMenuEvent":
                screenName = call.argument("screenName");
                if (screenName == null) {
                    result.error("NULL", "Screen name is null", null);
                    return;
                }
                Copilot.getInstance().Report.logEvent(new TapMenuAnalyticsEvent(screenName));
                break;

            case "logTapMenuItemEvent":
                String menuItem = call.argument("menuItem");
                screenName = call.argument("screenName");
                Copilot.getInstance().Report.logEvent(new TapMenuItemAnalyticsEvent(menuItem, screenName));
                break;

            case "logFirmwareUpgradeStartedEvent":
                Copilot.getInstance().Report.logEvent(new FirmwareUpgradeStartedAnalyticsEvent());
                break;

            case "logFirmwareUpgradeCompletedEvent":
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
                break;

            case "logErrorReportEvent":
                String error = call.argument("error");
                screenName = call.argument("screenName");
                Copilot.getInstance().Report.logEvent(new ErrorAnalyticsEvent(error, screenName));
                break;

            case "enableInAppMessages":
                Copilot.getInstance().InAppMessages.enable();
                break;

            case "disableInAppMessages":
                Copilot.getInstance().InAppMessages.disable();
                break;

            default:
                result.notImplemented();
        }

        result.success(null);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
