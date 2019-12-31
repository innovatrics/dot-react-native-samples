//
//  Dot.swift
//  DotReactNative
//
//  Created by Pavol Porubský on 18/12/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

import UIKit
import DOT
import AVKit

@objc(ActivityStarter)
class ActivityStarter: NSObject {
  @objc
  func initDot(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
    if let path = Bundle.main.path(forResource: "iengine.lic", ofType: "lic") {
      do {
        let license = try License(path: path)
        DOT.initialize(with: license)
      } catch {
        reject("Error", "license error", error)
      }
    }
    resolve(DOT.licenseId)
  }
  
  private var strongDelegate: DVCDelegate?
  @objc
  func startDocumentCaptureActivity(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
    DispatchQueue.main.async {
      let rootVC = UIApplication.shared.delegate!.window!!.rootViewController
      let dvc = DocumentCaptureViewController()
      self.strongDelegate = DVCDelegate()
      self.strongDelegate?.handler = { photo in
        dvc.dismiss(animated: true, completion: nil)
        resolve(photo.pngData()?.base64EncodedString())
      }
      dvc.delegate = self.strongDelegate
      rootVC?.present(dvc, animated: false, completion: nil)
    }
  }
  
  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
}

class DVCDelegate {
  
  var handler: ((UIImage) -> Void)?
}

extension DVCDelegate: DocumentCaptureViewControllerDelegate {
  func didTakePhoto(_ photo: UIImage) {
    handler?(photo)
  }
  
  func didPressRightBarButton(for viewController: DocumentCaptureViewController) {
    
  }
}

