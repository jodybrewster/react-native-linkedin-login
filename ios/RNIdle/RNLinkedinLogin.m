//
//  RNIdle.m
//  RNIdle
//
//  Created by lcj on 9/17/16.
//  Copyright © 2016 lcj. All rights reserved.
//

#import "RNIdle.h"

@implementation RNIdle

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(disableIdleTimer)
{
    //  DON'T let the device go to sleep during our sync
    [[UIApplication sharedApplication] setIdleTimerDisabled:NO];
    [[UIApplication sharedApplication] setIdleTimerDisabled:YES];
}

RCT_EXPORT_METHOD(enableIdleTimer)
{
    //  DON'T let the device go to sleep during our sync
    [[UIApplication sharedApplication] setIdleTimerDisabled:NO];
}

@end
