// RNLinkedinLogin.m
//
// Copyright (c) 2015 Jody Brewster
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

#import "RNLinkedinLogin.h"
#import <UIKit/UIKit.h>

#import <React/RCTEventDispatcher.h>
#import "LISDK.h"

@implementation RNLinkedinLogin

+ (BOOL)shouldHandleUrl:(NSURL *)url {
    
    return [LISDKCallbackHandler shouldHandleUrl:url];
    
}

+ (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
    
    return [LISDKCallbackHandler application:application openURL:url sourceApplication:sourceApplication annotation:annotation];
    
}


RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

@synthesize scopes = _scopes;



RCT_EXPORT_METHOD(getRequest:(NSString *)url)
{
  
  [[LISDKAPIHelper sharedInstance] getRequest:url
                                    success:^(LISDKAPIResponse *response)
  {
    
    
    return [self.bridge.eventDispatcher sendAppEventWithName:@"linkedinGetRequest" body:@{ @"data": [NSString stringWithFormat:@"%@", response.data] }];

  } error:^(LISDKAPIError *apiError)
  {
    NSLog(@"Error : %@", apiError);
    
    return [self.bridge.eventDispatcher sendAppEventWithName:@"linkedinGetRequestError" body:@{
                                                                                        @"message": apiError
                                                                                        }];

  }];
   

}

RCT_EXPORT_METHOD(login:(NSArray *)scopes)
{

  
  self.scopes = scopes;
//self.state = state;

NSArray *permissions = [NSArray arrayWithObjects:LISDK_BASIC_PROFILE_PERMISSION, LISDK_EMAILADDRESS_PERMISSION, nil];
  
  [LISDKSessionManager
       createSessionWithAuth:permissions
                       state:nil
      showGoToAppStoreDialog:YES
                successBlock:^(NSString *returnState) {
                  NSLog(@"%s","success called!");
                  LISDKSession *session = [[LISDKSessionManager sharedInstance] session];
                  LISDKAccessToken *token = [session accessToken];
                  NSString *accessToken = token.accessTokenValue;
                  NSDate *expiresOn = token.expiration;
                  
                  [NSDateFormatter setDefaultFormatterBehavior:NSDateFormatterBehavior10_4];
                  NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
                  [dateFormatter setDateFormat:@"yyyy-MM-dd'T'HH:mm:ssz"];
                  [dateFormatter setTimeZone:[NSTimeZone timeZoneForSecondsFromGMT:0]];
                  [dateFormatter setCalendar:[[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar]];
                  
                  
                  NSString *expiresStr = [dateFormatter stringFromDate:expiresOn];
                  
                
                  NSDictionary *body = @{@"accessToken": accessToken, @"expiresOn": expiresStr};
                  return [self.bridge.eventDispatcher sendDeviceEventWithName:@"linkedinLogin"
                                                                         body:body];
                }
                errorBlock:^(NSError *error) {
                  return [self.bridge.eventDispatcher
                          sendDeviceEventWithName:@"linkedinLoginError"
                                  body:@{@"error": error.description}];

                }
  ];
  

}




@end
