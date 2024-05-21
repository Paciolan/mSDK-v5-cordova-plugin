#import "CDVPaciolanSDK.h"
#import "InstantiateViewControllerError.h"
#import <UIKit/UIKit.h>
#import "PaciolanSDKViewController.h"


// We'll receive props as default, just in case. However they won't be used right now.
@interface CDVPaciolanSDK (hidden)

-(PaciolanSDKViewController*) tryInstantiatePaciolanViewWithProps: (NSString*) props;
@end

@implementation CDVPaciolanSDK
PaciolanSDKViewController *viewController = nil;
- (void)show:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    
    NSString* className = [command argumentAtIndex: 0];
    
    viewController = [self tryInstantiatePaciolanViewWithProps: className];
    
    CDVAppDelegate* appDelegate = [[UIApplication sharedApplication] delegate];
    [appDelegate.window.rootViewController presentViewController:viewController animated:YES completion:nil];
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId ];
}
- (void)onTokenChanged:(CDVInvokedUrlCommand*)command {
    if(viewController){
        [viewController setTokenListener:^(NSString *token) {
            CDVPluginResult* pluginResult = [CDVPluginResult
                                             resultWithStatus:CDVCommandStatus_OK
                                             messageAsString:token];
            [pluginResult setKeepCallbackAsBool:true];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId ];
        }];
    }
}

- (PaciolanSDKViewController*) tryInstantiatePaciolanViewWithProps:(NSString *)props {
    @try {
        
        return [[PaciolanSDKViewController alloc] init];
        // TODO: in the future, if we need to pass props to we can use the following:
        // return [[PaciolanSDKViewController alloc] initWithString:props];
    } @catch (InstantiateViewControllerError* e) {
        NSLog(@"%@", e.reason);
    }
    
    return nil;
}

- (void)navAwayFromPac {
       dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        // Call the navAwayFromPac method without providing the response parameter
        [[PaciolanSDKViewController alloc] navAwayFromPac:nil resolver:^(id result) {
            NSLog(@"Parent App Success: %@", result);
        } rejecter:^(NSString *code, NSString *message, NSError *error) {
            NSLog(@"Parent App Error: %@ - %@", code, message);
        }];
    });
}

- (void)appLaunched {
       dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        // Call the appLaunched method without providing the response parameter
        [[PaciolanSDKViewController alloc] appLaunched:nil resolver:nil rejecter:nil];
    });
}

@end
