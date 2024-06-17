import Foundation
import DotCore
import DotNfc
import DotDocument
import DotFaceCore
import DotFaceExpressionNeutral
import DotFaceDetectionFast
import DotFaceBackgroundUniformity

@objc(DotSdkReactModule)
class DotSdkReactModule: NSObject {

  private enum ModuleError: LocalizedError {
    case missingLicenseFile
    case failedToCreateImageWriter
    case failedToGetTopViewController
    case failedToParse
    var errorDescription: String? {
      switch self {
      case .missingLicenseFile: return "Missing DotSdk license file with name: `dot_license.lic`."
      case .failedToCreateImageWriter: return "Failed to create ImageWriter."
      case .failedToGetTopViewController: return "Failed to get top most view controller."
      case .failedToParse: return "Failed to parse."
      }
    }
  }
  
  private var viewControllerHandler: ViewControllerHandler?
  private var nfcReadingHandler: NfcReadingHandler?
  
  @objc
  func isInitialized(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
    resolve(DotSdk.shared.isInitialized)
  }
  
  @objc
  func initialize(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
    let promise = ReactNativePromise(resolveBlock: resolve, rejectBlock: reject)
    guard let url = Bundle.main.url(forResource: "dot_license", withExtension: "lic") else {
      promise.reject(error: .init(message: ModuleError.missingLicenseFile.localizedDescription))
      return
    }
    
    switch initializeDotSdk(url: url) {
    case .success: resolve(())
    case .failure(let error): promise.reject(error: .init(message: error.localizedDescription))
    }
  }
  
  @objc
  func startDocumentAutoCapture(_ configurationJson: String?, 
                                resolve: @escaping RCTPromiseResolveBlock,
                                rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
    let promise = ReactNativePromise(resolveBlock: resolve, rejectBlock: reject)
    guard let imageWriter = try? ImageWriter() else {
      promise.reject(error: .init(message: ModuleError.failedToCreateImageWriter.localizedDescription))
      return
    }
    guard let topViewController = getTopMostViewController() else {
      promise.reject(error: .init(message: ModuleError.failedToGetTopViewController.localizedDescription))
      return
    }
    do {
      let configuration = try configurationJson.map({ try parse(configurationJson: $0) }) ?? .init()
      let viewControllerHandler = DocumentAutoCaptureViewControllerHandler(promise: promise, imageWriter: imageWriter, configuration: configuration)
      viewControllerHandler.present(on: topViewController)
      self.viewControllerHandler = viewControllerHandler
    } catch {
      promise.reject(error: .init(message: error.localizedDescription))
      return
    }
  }
  
  @objc
  func startNfcReading(_ nfcKeyJson: String?,
                       resolve: @escaping RCTPromiseResolveBlock,
                       rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
    let promise = ReactNativePromise(resolveBlock: resolve, rejectBlock: reject)
    guard let imageWriter = try? ImageWriter() else {
      promise.reject(error: .init(message: ModuleError.failedToCreateImageWriter.localizedDescription))
      return
    }
    guard let nfcKeyJson = nfcKeyJson, let nfcKey = try? parse(nfcKeyJson: nfcKeyJson) else {
      promise.reject(error: .init(message: ModuleError.failedToGetTopViewController.localizedDescription))
      return
    }
    
    let nfcReadinghandler = NfcReadingHandler(promise: promise, imageWriter: imageWriter)
    self.nfcReadingHandler = nfcReadinghandler
    self.viewControllerHandler = nil
    nfcReadinghandler.execute(nfcKey: nfcKey)
  }
  
  @objc
  func startFaceAutoCapture(_ resolve: @escaping RCTPromiseResolveBlock,
                            rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
    let promise = ReactNativePromise(resolveBlock: resolve, rejectBlock: reject)
    guard let imageWriter = try? ImageWriter() else {
      promise.reject(error: .init(message: ModuleError.failedToCreateImageWriter.localizedDescription))
      return
    }
    guard let topViewController = getTopMostViewController() else {
      promise.reject(error: .init(message: ModuleError.failedToGetTopViewController.localizedDescription))
      return
    }
    let viewControllerHandler = FaceAutoCaptureViewControllerHandler(promise: promise, imageWriter: imageWriter)
    viewControllerHandler.present(on: topViewController)
    self.viewControllerHandler = viewControllerHandler
  }
  
  @objc
  func startSmileLiveness(_ resolve: @escaping RCTPromiseResolveBlock,
                            rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
    let promise = ReactNativePromise(resolveBlock: resolve, rejectBlock: reject)
    guard let imageWriter = try? ImageWriter() else {
      promise.reject(error: .init(message: ModuleError.failedToCreateImageWriter.localizedDescription))
      return
    }
    guard let topViewController = getTopMostViewController() else {
      promise.reject(error: .init(message: ModuleError.failedToGetTopViewController.localizedDescription))
      return
    }
    let viewControllerHandler = SmileLivenessViewControllerHandler(promise: promise, imageWriter: imageWriter)
    viewControllerHandler.present(on: topViewController)
    self.viewControllerHandler = viewControllerHandler
  }
  
  @objc
  func startMagnifEyeLiveness(_ resolve: @escaping RCTPromiseResolveBlock,
                            rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
    let promise = ReactNativePromise(resolveBlock: resolve, rejectBlock: reject)
    guard let imageWriter = try? ImageWriter() else {
      promise.reject(error: .init(message: ModuleError.failedToCreateImageWriter.localizedDescription))
      return
    }
    guard let topViewController = getTopMostViewController() else {
      promise.reject(error: .init(message: ModuleError.failedToGetTopViewController.localizedDescription))
      return
    }
    let viewControllerHandler = MagnifEyeLivenessViewControllerHandler(promise: promise, imageWriter: imageWriter)
    viewControllerHandler.present(on: topViewController)
    self.viewControllerHandler = viewControllerHandler
  }
}

extension DotSdkReactModule {
  
  private func initializeDotSdk(url: URL) -> Result<Void, Error> {
    do {
      let license = try Data(contentsOf: url)
      let dotFaceLibraryConfiguration = DotFaceLibraryConfiguration(
        modules: [
          DotFaceDetectionFastModule.shared,
          DotFaceExpressionNeutralModule.shared,
          DotFaceBackgroundUniformityModule.shared
        ]
      )
      let dotSdkConfiguration = DotSdkConfiguration(
        licenseBytes: license,
        libraries: [
          DotFaceLibrary(configuration: dotFaceLibraryConfiguration),
          DotDocumentLibrary(),
          DotNfcLibrary()
        ]
      )
      try DotSdk.shared.initialize(configuration: dotSdkConfiguration)
      return .success(())
    }
    catch {
      return .failure(error)
    }
  }
  
  private func getTopMostViewController() -> UIViewController? {
    let keyWindow = UIApplication.shared.windows.filter {$0.isKeyWindow}.first
    
    if var topController = keyWindow?.rootViewController {
      while let presentedViewController = topController.presentedViewController {
        topController = presentedViewController
      }
      return topController
    }
    return nil
  }
  
  private func parse(configurationJson: String) throws -> DocumentAutoCaptureConfiguration {
    guard let data = configurationJson.data(using: .utf8),
          let json = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: String],
          let mrzValidationString = json["mrzValidation"],
          let mrzValidation = MrzValidation.from(string: mrzValidationString)
    else { throw ModuleError.failedToParse }
    
    return .init(mrzValidation: mrzValidation)
  }
  
  private func parse(nfcKeyJson: String) throws -> NfcKey {
    guard let data = nfcKeyJson.data(using: .utf8),
          let json = try? JSONSerialization.jsonObject(with: data, options: []) as? [String: String],
          let documentNumber = json["documentNumber"],
          let dateOfExpiry = json["dateOfExpiry"],
          let dateOfBirth = json["dateOfBirth"]
    else { throw ModuleError.failedToParse }
    return try NfcKey(documentNumber: documentNumber, dateOfExpiry: dateOfExpiry, dateOfBirth: dateOfBirth)
  }
}
