//
//  RNLInkedinCallbackHandler.m
//  RNLinkedinLogin
//
//  Created by Jody Brewster on 5/28/17.
//  Copyright © 2017 lcj. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNLInkedinCallbackHandler.h"
#import <linkedin-sdk/LISDK.h>

@implementation RNLinkedinCallbackHandler : NSObject

+ (BOOL)shouldHandleUrl:(NSURL *)url {
    
    return NO;
    // return [LISDKCallbackHandler shouldHandleUrl:url];
    
}

+ (BOOL)application:(UIApplication *)application
openURL:(NSURL *)url
sourceApplication:(NSString *)sourceApplication
annotation:(id)annotation {
    
    return NO;
    //return [LISDKCallbackHandler application:application openURL:url sourceApplication:sourceApplication annotation:annotation];
    
}


@end

