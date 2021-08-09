import 'dart:async';

import 'package:firebase_analytics/firebase_analytics.dart';
import 'package:flutter/services.dart';

class CopilotDart {
  static const MethodChannel _channel = const MethodChannel('copilot_dart');
  static late final FirebaseAnalytics _analytics;

  static void initialize(FirebaseAnalytics analytics) {
    _analytics = analytics;
    _channel.setMethodCallHandler((call) {
      switch (call.method) {
        case 'setUserId':
          return setUserId(call.arguments as String);
        case 'logEvent':
          final arguments = call.arguments as Map<String, dynamic>;
          final eventName = arguments['eventName'] as String;
          final transformedParams = arguments['transformedParams'] as Map<String, String>;
          return logEvent(eventName, transformedParams);
        case 'generateUserAuthKey':
          final arguments = call.arguments as Map<String, dynamic>;
          final userId = arguments['userId'] as String;
          return generateUserAuthKey(userId);
        default:
          throw MissingPluginException();
      }
    });
  }

  static Future<void> setUserId(String userId) async {
    await _analytics.setUserId(userId);
  }

  static Future<void> logEvent(
    String eventName,
    Map<String, String> transformedParams,
  ) async {
    await _analytics.logEvent(name: eventName, parameters: transformedParams);
  }

  static Future<String> generateUserAuthKey(String userId) async {
    // TODO: Implement backend call
    return '';
  }

  static Future<void> sessionStarted({
    required String userId,
    required bool isCopilotAnalysisConsentApproved,
  }) async {
    await _channel.invokeMethod(
      'sessionStarted',
      {
        'userId': userId,
        'isCopilotAnalysisConsentApproved': isCopilotAnalysisConsentApproved,
      },
    );
  }

  static Future<void> sessionEnded() async {
    await _channel.invokeMethod('sessionEnded');
  }

  static Future<void> setCopilotAnalysisConsent({
    required bool isConsentApproved,
  }) async {
    await _channel.invokeMethod(
      'setCopilotAnalysisConsent',
      {
        'isConsentApproved': isConsentApproved,
      },
    );
  }

  static Future<void> logSignupEvent() async {
    await _channel.invokeMethod('logSignupEvent');
  }

  static Future<void> logLoginEvent() async {
    await _channel.invokeMethod('logLoginEvent');
  }

  static Future<void> logLogoutEvent() async {
    await _channel.invokeMethod('logLogoutEvent');
  }

  static Future<void> logSuccessfulElevateAnonymousEvent() async {
    await _channel.invokeMethod('logSuccessfulElevateAnonymousEvent');
  }

  static Future<void> logAcceptTermsOfUseEvent({required String version}) async {
    await _channel.invokeMethod(
      'logAcceptTermsOfUseEvent',
      {
        'version': version,
      },
    );
  }

  static Future<void> logContactSupportEvent({
    required String supportCase,
    String? thingId,
    String? screenName,
  }) async {
    await _channel.invokeMethod(
      'logContactSupportEvent',
      {
        'supportCase': supportCase,
        'thingId': thingId,
        'screenName': screenName,
      },
    );
  }

  static Future<void> logOnBoardingStartedEvent({
    required String flowId,
    String? screenName,
  }) async {
    await _channel.invokeMethod(
      'logOnBoardingStartedEvent',
      {
        'flowId': flowId,
        'screenName': screenName,
      },
    );
  }

  static Future<void> logOnBoardingEndedEvent({
    required String flowId,
    String? screenName,
  }) async {
    await _channel.invokeMethod(
      'logOnBoardingEndedEvent',
      {
        'flowId': flowId,
        'screenName': screenName,
      },
    );
  }

  static Future<void> logTapConnectDeviceEvent() async {
    await _channel.invokeMethod('logTapConnectDeviceEvent');
  }

  static Future<void> logThingConnectedEvent({
    required String thingId,
    String? screenName,
  }) async {
    await _channel.invokeMethod(
      'logOnBoardingStartedEvent',
      {
        'thingId': thingId,
        'screenName': screenName,
      },
    );
  }

  static Future<void> logThingDiscoveredEvent({required String thingId}) async {
    await _channel.invokeMethod(
      'logThingDiscoveredEvent',
      {
        'thingId': thingId,
      },
    );
  }

  static Future<void> logThingInfoEvent({
    required String thingFirmware,
    required String thingModel,
    required String thingId,
  }) async {
    await _channel.invokeMethod(
      'logThingInfoEvent',
      {
        'thingFirmware': thingFirmware,
        'thingModel': thingModel,
        'thingId': thingId,
      },
    );
  }

  static Future<void> logThingConnectionFailedEvent({
    required String failureReason,
  }) async {
    await _channel.invokeMethod(
      'logThingConnectionFailedEvent',
      {
        'failureReason': failureReason,
      },
    );
  }

  static Future<void> logConsumableUsageEvent({
    String? screenName,
    required String consumableType,
    required String thingId,
  }) async {
    await _channel.invokeMethod(
      'logConsumableUsageEvent',
      {
        'screenName': screenName,
        'consumableType': consumableType,
        'thingId': thingId,
      },
    );
  }

  static Future<void> logConsumableDepletedEvent({
    String? screenName,
    String? consumableType,
    String? thingId,
  }) async {
    await _channel.invokeMethod(
      'logConsumableDepletedEvent',
      {
        'screenName': screenName,
        'consumableType': consumableType,
        'thingId': thingId,
      },
    );
  }

  static Future<void> logScreenLoadEvent({
    required String screenName,
  }) async {
    await _channel.invokeMethod(
      'logScreenLoadEvent',
      {
        'screenName': screenName,
      },
    );
  }

  static Future<void> logTapMenuEvent({
    required String screenName,
  }) async {
    await _channel.invokeMethod(
      'logTapMenuEvent',
      {
        'screenName': screenName,
      },
    );
  }

  static Future<void> logTapMenuItemEvent({
    required String menuItem,
    String? screenName,
  }) async {
    await _channel.invokeMethod(
      'logTapMenuItemEvent',
      {
        'menuItem': menuItem,
        'screenName': screenName,
      },
    );
  }

  static Future<void> logFirmwareUpgradeStartedEvent() async {
    await _channel.invokeMethod('logFirmwareUpgradeStartedEvent');
  }

  static Future<void> logFirmwareUpgradeCompletedEvent({
    required FirmwareUpgradeStatus firmwareUpgradeStatus,
  }) async {
    await _channel.invokeMethod(
      'logFirmwareUpgradeCompletedEvent',
      {
        'firmwareUpgradeStatus': firmwareUpgradeStatus == FirmwareUpgradeStatus.success ? 'success' : 'failure',
      },
    );
  }

  static Future<void> logErrorReportEvent({
    required String error,
    String? screenName,
  }) async {
    await _channel.invokeMethod(
      'logErrorReportEvent',
      {
        'error': error,
        'screenName': screenName,
      },
    );
  }

  static Future<void> enableInAppMessages() async {
    await _channel.invokeMethod('enableInAppMessages');
  }

  static Future<void> disableInAppMessages() async {
    await _channel.invokeMethod('disableInAppMessages');
  }
}

enum FirmwareUpgradeStatus {
  success,
  failure,
}
