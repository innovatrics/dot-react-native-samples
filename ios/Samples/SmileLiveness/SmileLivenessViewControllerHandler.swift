import UIKit
import Foundation
import DotCore
import DotFaceCore

class SmileLivenessViewControllerHandler: ViewControllerHandler, SmileLivenessViewControllerDelegate {
  
  init(promise: ReactNativePromise, imageWriter: ImageWriter) {
    let viewController = SmileLivenessViewController.create()
    super.init(promise: promise, viewController: viewController, imageWriter: imageWriter)
    viewController.delegate = self
  }
  
  func smileLivenessViewController(_ viewController: SmileLivenessViewController, finished result: SmileLivenessResult) {
    dismiss()
    let cgImage = CGImageFactory.create(bgrRawImage: result.bgrRawImage)
    do {
      let result = try createResult(cgImage: cgImage, result: try FaceAutoCaptureSampleResult.from(detectedFace: result.detectedFace))
      promise.resolve(result: result)
    } catch {
      promise.reject(error: .init(message: error.localizedDescription))
    }
  }
  
  func smileLivenessViewControllerViewDidAppear(_ viewController: SmileLivenessViewController) {
    viewController.start()
  }
}
