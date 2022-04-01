//
//  ActivityStarter.swift
//  dot_react_native_sample
//
//  Created by Oleksandr Lunevskyi on 22/03/2022.
//

import UIKit
import AVKit
import DotDocument
import DotFaceCore
import DotFaceEyeGazeLiveness
import DotFaceDetectionFast
import DotFacePassiveLiveness

// MARK: - ActivityStarter

@objc(ActivityStarter)
class ActivityStarter: NSObject {

  private let strongDelegate = GeneralDelegate()

  @objc
  func initDot(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
    if let url = Bundle.main.url(forResource: "iengine", withExtension: "lic") {
      do {
        let license = try Data(contentsOf: url)
        let configuration = try DotFaceConfiguration(
          license: license,
          modules: [
            DotFacePassiveLivenessModule.shared,
            DotFaceEyeGazeLivenessModule.shared,
            DotFaceDetectionFastModule.shared
          ],
          faceDetectionConfidenceThreshold: 0.06
        )
        DotFace.shared.initialize(configuration: configuration)
        resolve(DotFace.shared.licenseId)
      } catch {
        reject("Error", "license error", error)
      }
    }
  }

  @objc
  func startDocumentCaptureActivity(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
    DispatchQueue.main.async { [weak self] in
      guard let self = self else { return }

      let rootViewController = UIApplication.shared.delegate?.window??.rootViewController

      let configuration = try! DocumentAutoCaptureConfiguration(isDetectionLayerVisible: true)
      let documentAutoCaptureViewController = DocumentAutoCaptureViewController.create(configuration: configuration)
      documentAutoCaptureViewController.delegate = self.strongDelegate

      self.strongDelegate.documentAutoCaptureHandler = { photo in
        documentAutoCaptureViewController.dismiss(animated: true) {
          resolve(photo.pngData()?.base64EncodedString())
        }
      }

      self.strongDelegate.cameraAccessNotAuthorizedHandler = { [weak self] in
        self?.presentCameraAuthorizationStatusAlert(documentAutoCaptureViewController)
      }

      rootViewController?.present(documentAutoCaptureViewController, animated: true)
    }
  }
  
  @objc
  func startLivenessCheckActivity(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
    DispatchQueue.main.async { [weak self] in
      guard let self = self else { return }

      let rootViewController = UIApplication.shared.delegate?.window??.rootViewController

      let segments = RandomSegmentsGenerator().generate(segmentCount: 5, segmentDurationMillis: 1000)
      let configuration = try! EyeGazeLivenessConfiguration(
        transitionType: .move,
        segments: segments
      )
      let eyeGazeLivenessViewController = EyeGazeLivenessViewController.create(configuration: configuration)
      eyeGazeLivenessViewController.delegate = self.strongDelegate

      self.strongDelegate.eyeGazeLivenessCheckHandler = { score, faceImage in
        eyeGazeLivenessViewController.dismiss(animated: true) {
          if let imageBase64String = faceImage.pngData()?.base64EncodedString().prefix(20) {
            resolve(["score": score, "photoUri": imageBase64String])
          }
        }
      }

      self.strongDelegate.cameraAccessNotAuthorizedHandler = { [weak self] in
        self?.presentCameraAuthorizationStatusAlert(eyeGazeLivenessViewController)
      }

      rootViewController?.present(eyeGazeLivenessViewController, animated: true)
    }
  }

  @objc
  func startFaceAutoCaptureActivity(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
    DispatchQueue.main.async { [weak self] in
      guard let self = self else { return }

      let rootViewController = UIApplication.shared.delegate?.window??.rootViewController

      let configuration = FaceAutoCaptureConfiguration(qualityAttributes: PassiveLivenessQualityProvider().getQualityAttributes())
      let faceAutoCaptureViewController = FaceAutoCaptureViewController.create(configuration: configuration)
      faceAutoCaptureViewController.delegate = self.strongDelegate

      self.strongDelegate.faceAutoCaptureHandler = { photo in
        faceAutoCaptureViewController.dismiss(animated: true) {
          resolve(photo.pngData()?.base64EncodedString())
        }
      }

      self.strongDelegate.cameraAccessNotAuthorizedHandler = { [weak self] in
        self?.presentCameraAuthorizationStatusAlert(faceAutoCaptureViewController)
      }

      rootViewController?.present(faceAutoCaptureViewController, animated: true)
    }
  }

  @objc
  func presentCameraAuthorizationStatusAlert(_ viewControllerToPresent: UIViewController) {
    let alertController = UIAlertController(title: "Error", message: "Please authorize camera access.", preferredStyle: .alert)
    let alertAction = UIAlertAction(title: "OK", style: .default)
    alertController.addAction(alertAction)

    viewControllerToPresent.present(alertController, animated: true)
  }
  
  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }

}

// MARK: - GeneralDelegate

class GeneralDelegate {

  var documentAutoCaptureHandler: ((UIImage) -> Void)?
  var cameraAccessNotAuthorizedHandler: (() -> Void)?
  var eyeGazeLivenessCheckHandler: ((Float, UIImage) -> Void)?
  var faceAutoCaptureHandler: ((UIImage) -> Void)?

}

// MARK: - DocumentAutoCaptureViewControllerDelegate

extension GeneralDelegate: DocumentAutoCaptureViewControllerDelegate {

  func documentAutoCaptureViewController(_ viewController: DocumentAutoCaptureViewController, captured result: DocumentAutoCaptureResult) {
    let documentImage = UIImage(cgImage: CGImageFactory.create(bgraRawImage: result.bgraRawImage))
    documentAutoCaptureHandler?(documentImage)
  }

  func documentAutoCaptureViewControllerNoCameraPermission(_ viewController: DocumentAutoCaptureViewController) {
    switch AVCaptureDevice.authorizationStatus(for: .video) {
    case .denied, .restricted:
      cameraAccessNotAuthorizedHandler?()

    case .notDetermined:
      AVCaptureDevice.requestAccess(for: .video) { granted in
        DispatchQueue.main.async {
          viewController.start()
        }
      }

    default:
      break
    }
  }

  func documentAutoCaptureViewControllerViewDidAppear(_ viewController: DocumentAutoCaptureViewController) {
    viewController.start()
  }

}

// MARK: - EyeGazeLivenessViewControllerDelegate

extension GeneralDelegate: EyeGazeLivenessViewControllerDelegate {

  func eyeGazeLivenessViewController(_ viewController: EyeGazeLivenessViewController, finished score: Float, with segmentImages: [SegmentImage]) {
    guard let firstSegmentImage = segmentImages.first?.image else { return }

    let faceImage = UIImage(cgImage: CGImageFactory.create(bgrRawImage: firstSegmentImage))
    eyeGazeLivenessCheckHandler?(score, faceImage)
  }

  func eyeGazeLivenessViewControllerNoCameraPermission(_ viewController: EyeGazeLivenessViewController) {
    switch AVCaptureDevice.authorizationStatus(for: .video) {
    case .denied, .restricted:
      cameraAccessNotAuthorizedHandler?()

    case .notDetermined:
      AVCaptureDevice.requestAccess(for: .video) { _ in
        DispatchQueue.main.async {
          viewController.start()
        }
      }

    default:
      break
    }
  }

  func eyeGazeLivenessViewControllerFaceTrackingFailed(_ viewController: EyeGazeLivenessViewController) {
    viewController.start()
  }

  func eyeGazeLivenessViewControllerViewDidAppear(_ viewController: EyeGazeLivenessViewController) {
    viewController.start()
  }

  func eyeGazeLivenessViewControllerNoMoreSegments(_ viewController: EyeGazeLivenessViewController) {
    viewController.start()
  }

  func eyeGazeLivenessViewControllerEyesNotDetected(_ viewController: EyeGazeLivenessViewController) {
    viewController.start()
  }

  func eyeGazeLivenessViewController(_ viewController: EyeGazeLivenessViewController, stateChanged state: EyeGazeLivenessState) { }

  func eyeGazeLivenessViewControllerViewWillDisappear(_ viewController: EyeGazeLivenessViewController) {
    viewController.stopAsync()
  }

}

// MARK: - FaceAutoCaptureViewControllerDelegate

extension GeneralDelegate: FaceAutoCaptureViewControllerDelegate {

  func faceAutoCaptureViewController(_ viewController: FaceAutoCaptureViewController, stepChanged captureStepId: CaptureStepId, with detectedFace: DetectedFace?) { }

  func faceAutoCaptureViewController(_ viewController: FaceAutoCaptureViewController, captured detectedFace: DetectedFace) {
    let faceImage = UIImage(cgImage: CGImageFactory.create(bgrRawImage: detectedFace.image))
    faceAutoCaptureHandler?(faceImage)
  }

  func faceAutoCaptureViewControllerNoCameraPermission(_ viewController: FaceAutoCaptureViewController) {
    switch AVCaptureDevice.authorizationStatus(for: .video) {
    case .denied, .restricted:
      cameraAccessNotAuthorizedHandler?()

    case .notDetermined:
      AVCaptureDevice.requestAccess(for: .video) { _ in
        DispatchQueue.main.async {
          viewController.start()
        }
      }

    default:
      break
    }
  }

  func faceAutoCaptureViewControllerViewWillAppear(_ viewController: FaceAutoCaptureViewController) {
    viewController.start()
  }

  func faceAutoCaptureViewControllerViewWillDisappear(_ viewController: FaceAutoCaptureViewController) {
    viewController.stopAsync()
  }

}
