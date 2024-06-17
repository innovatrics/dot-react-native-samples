import UIKit
import Foundation
import DotCore
import DotDocument

class DocumentAutoCaptureViewControllerHandler: ViewControllerHandler, DocumentAutoCaptureViewControllerDelegate {
  
  init(promise: ReactNativePromise, imageWriter: ImageWriter, configuration: DocumentAutoCaptureConfiguration) {
    let viewController = DocumentAutoCaptureViewController.create(configuration: configuration)
    super.init(promise: promise, viewController: viewController, imageWriter: imageWriter)
    viewController.delegate = self
  }
  
  func documentAutoCaptureViewControllerViewDidAppear(_ viewController: DocumentAutoCaptureViewController) {
    viewController.start()
  }
  
  func documentAutoCaptureViewController(_ viewController: DocumentAutoCaptureViewController, captured result: DocumentAutoCaptureResult) {
    dismiss()
    let cgImage = CGImageFactory.create(bgraRawImage: result.bgraRawImage)
    do {
      let result = try createResult(cgImage: cgImage, result: result)
      promise.resolve(result: result)
    } catch {
      promise.reject(error: .init(message: error.localizedDescription))
    }
  }
}
