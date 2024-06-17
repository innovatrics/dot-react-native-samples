import UIKit
import Foundation
import DotCore
import DotFaceCore

class FaceAutoCaptureViewControllerHandler: ViewControllerHandler, FaceAutoCaptureViewControllerDelegate {
  
  init(promise: ReactNativePromise, imageWriter: ImageWriter) {
    let viewController = FaceAutoCaptureViewController.create()
    super.init(promise: promise, viewController: viewController, imageWriter: imageWriter)
    viewController.delegate = self
  }
  
  func faceAutoCaptureViewControllerViewDidAppear(_ viewController: FaceAutoCaptureViewController) {
    viewController.start()
  }
  
  func faceAutoCaptureViewController(_ viewController: FaceAutoCaptureViewController, captured result: FaceAutoCaptureResult) {
    dismiss()
    let cgImage = CGImageFactory.create(bgrRawImage: result.bgrRawImage)
    do {
      let result = try createResult(cgImage: cgImage, result: try FaceAutoCaptureSampleResult.from(detectedFace: result.detectedFace!))
      promise.resolve(result: result)
    } catch {
      promise.reject(error: .init(message: error.localizedDescription))
    }
  }
}
