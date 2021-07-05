#import "CopilotDartPlugin.h"
#if __has_include(<copilot_dart/copilot_dart-Swift.h>)
#import <copilot_dart/copilot_dart-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "copilot_dart-Swift.h"
#endif

@implementation CopilotDartPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftCopilotDartPlugin registerWithRegistrar:registrar];
}
@end
