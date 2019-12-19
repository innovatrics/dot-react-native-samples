//
//  Dot.swift
//  DotReactNative
//
//  Created by Pavol Porubský on 18/12/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation

@objc(ActivityStarter)
class ActivityStarter: NSObject {
  @objc
  func initDot(resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
    resolve(true)
  }
  
  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
}

