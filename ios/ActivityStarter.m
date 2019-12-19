//
//  Dot.m
//  DotReactNative
//
//  Created by Pavol Porubský on 18/12/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"

@interface RCT_EXTERN_MODULE(ActivityStarter, NSObject)
RCT_EXTERN_METHOD(initDot:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
@end
