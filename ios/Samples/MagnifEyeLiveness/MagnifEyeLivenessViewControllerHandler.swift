import UIKit
import Foundation
import DotCore
import DotFaceCore

class MagnifEyeLivenessViewControllerHandler: ViewControllerHandler, MagnifEyeLivenessViewControllerDelegate {
  
  init(promise: ReactNativePromise, imageWriter: ImageWriter) {
    let viewController = MagnifEyeLivenessViewController.create()
    super.init(promise: promise, viewController: viewController, imageWriter: imageWriter)
    viewController.delegate = self
  }
  
  func magnifEyeLivenessViewController(_ viewController: MagnifEyeLivenessViewController, finished result: MagnifEyeLivenessResult) {
    dismiss()
    let cgImage = CGImageFactory.create(bgrRawImage: result.bgrRawImage)
    do {
      let result = try createResult(cgImage: cgImage, result: try FaceAutoCaptureSampleResult.from(detectedFace: result.detectedFace))
      promise.resolve(result: result)
    } catch {
      promise.reject(error: .init(message: error.localizedDescription))
    }
  }
  
  func magnifEyeLivenessViewControllerViewDidAppear(_ viewController: MagnifEyeLivenessViewController) {
    viewController.start()
  }
}
