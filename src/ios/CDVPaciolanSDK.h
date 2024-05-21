#import <Cordova/CDV.h>

@interface CDVPaciolanSDK : CDVPlugin

- (void)show:(CDVInvokedUrlCommand*)command;
- (void)onTokenChanged:(CDVInvokedUrlCommand*)command;
- (void)navAwayFromPac;
- (void)appLaunched;

@end
