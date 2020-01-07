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
    if let path = Bundle.main.path(forResource: "iengine", ofType: "lic") {
      do {
        let license = try License(path: path)
        DOT.initialize(with: license)
        resolve(DOT.licenseId)
      } catch {
        reject("Error", "license error", error)
      }
    }
  }
  
  private let strongDelegate = GeneralDelegate()
  @objc
  func startDocumentCaptureActivity(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
    DispatchQueue.main.async {
      let rootVC = UIApplication.shared.delegate!.window!!.rootViewController
      let dvc = DocumentCaptureViewController()
      self.strongDelegate.documentcaptureHandler = { photo in
        dvc.dismiss(animated: true) {
          resolve(photo.pngData()?.base64EncodedString())
        }
      }
      dvc.delegate = self.strongDelegate
      rootVC?.present(dvc, animated: false, completion: nil)
    }
  }
  
  @objc
  func startLivenessCheckActivity(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
    DispatchQueue.main.async {
      let rootVC = UIApplication.shared.delegate!.window!!.rootViewController
      let lvc = LivenessCheck2Controller.create(with: Liveness2Configuration(transitionType: .move), style: .init())
      self.strongDelegate.livenessCheckHandler = { score, faceImage in
        lvc.dismiss(animated: true) {
          if let imageBase64String = faceImage.pngData()?.base64EncodedString().prefix(20) {
            resolve(["score": score, "photoUri": imageBase64String])
          }
        }
      }
      lvc.delegate = self.strongDelegate
      rootVC?.present(lvc, animated: false, completion: nil)
    }
    
  }
  
  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
}

class GeneralDelegate {
  
  var score: Float?
  var faceImage: UIImage?
  
  var documentcaptureHandler: ((UIImage) -> Void)?
  var livenessCheckHandler: ((Float, UIImage) -> Void)?
}

extension GeneralDelegate: DocumentCaptureViewControllerDelegate {
  func didTakePhoto(_ photo: UIImage) {
    documentcaptureHandler?(photo)
  }
  
  func didPressRightBarButton(for viewController: DocumentCaptureViewController) {
    
  }
}

extension GeneralDelegate: LivenessCheck2ControllerDelegate {
  func livenessCheck2InitialStart(_ controller: LivenessCheck2Controller) -> Bool {
    return true
  }
  
  func livenessCheck2CameraInitFailed(_ controller: LivenessCheck2Controller) {
    
  }
  
  func livenessCheck2(_ controller: LivenessCheck2Controller, livenessStateChanged state: LivenessContextState) {
    
  }
  
  func livenessCheck2(_ controller: LivenessCheck2Controller, checkDoneWith score: Float, capturedSegmentImages segmentImagesList: [SegmentImage]) {
    self.score = score
    if let faceImage = faceImage {
      livenessCheckHandler?(score, faceImage)
    }
  }
  
  func livenessCheck2FaceCaptureFailed(_ controller: LivenessCheck2Controller) {
    
  }
  
  func livenessCheck2NoMoreSegments(_ controller: LivenessCheck2Controller) {
    
  }
  
  func livenessCheck2NoEyesDetected(_ controller: LivenessCheck2Controller) {
    
  }
  
  func livenessCheck2(_ controller: LivenessCheck2Controller, captureStateChanged captureState: FaceCaptureState, with image: DOTImage?) {
    
  }
  
  func livenessCheck2(_ controller: LivenessCheck2Controller, didSuccess detectedFace: DetectedFace) {
    faceImage = detectedFace.cropedFace
    if let score = score, let faceImage = faceImage {
      livenessCheckHandler?(score, faceImage)
    }
  }
  
  func livenessCheck2DidAppear(_ controller: LivenessCheck2Controller) {
    controller.startLivenessCheck()
  }
}

