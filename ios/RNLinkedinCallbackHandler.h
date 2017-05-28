//
//  RNLInkedinCallbackHandler.h
//  RNLinkedinLogin
//
//  Created by Jody Brewster on 5/28/17.
//  Copyright © 2017 lcj. All rights reserved.
//

#ifndef RNLInkedinCallbackHandler_h
#define RNLInkedinCallbackHandler_h

#import <UIKit/UIKit.h>
#import "RNLinkedinCallbackHandler.h"


@interface RNLinkedinCallbackHandler : NSObject


+ (BOOL)shouldHandleUrl:(NSURL *)url;

+ (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation;


@end


#endif /* RNLInkedinCallbackHandler_h */
