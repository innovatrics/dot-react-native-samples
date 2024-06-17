import DotNfc
import UIKit

class NfcReadingHandler {
  
  enum Error: LocalizedError {
    case missingFaceImage
    case failedToEncode
    case nfcCanceled
    case nfcFailed
    
    var errorDescription: String? {
      switch self {
      case .missingFaceImage: return "Failed to get face image from travel document."
      case .failedToEncode: return "Failed to encode travel document."
      case .nfcCanceled: return "NFC reading was canceled."
      case .nfcFailed: return "NFC reading failed."
      }
    }
  }
  
  private let promise: ReactNativePromise
  private let imageWriter: ImageWriter
  private let encoder: JSONEncoder = {
      let encoder = JSONEncoder()
      encoder.outputFormatting = .prettyPrinted
      return encoder
  }()
  
  private lazy var travelDocumentReader: NfcTravelDocumentReaderProtocol = {
    let authorityCertificatesUrl = Bundle.main.url(forResource: "master_list", withExtension: "pem")
    let configuration = NfcTravelDocumentReaderConfiguration(authorityCertificatesUrl: authorityCertificatesUrl)
    let travelDocumentReader = NfcTravelDocumentReaderFactory().create(configuration: configuration)
    travelDocumentReader.setDelegate(self)
    return travelDocumentReader
  }()
  
  init(promise: ReactNativePromise, imageWriter: ImageWriter) {
    self.promise = promise
    self.imageWriter = imageWriter
  }
  
  func execute(nfcKey: NfcKey) {
    travelDocumentReader.read(nfcKey: nfcKey)
  }
  
  private func createResult<T: Encodable>(cgImage: CGImage, result: T) throws -> ReactNativeResult {
    guard let jsonString = String(data: try encoder.encode(result), encoding: .utf8) else { throw Error.failedToEncode }
    let imageUrl = try imageWriter.write(cgImage: cgImage)
    return .init(imageUri: imageUrl.absoluteString, jsonData: jsonString)
  }
}

extension NfcReadingHandler: NfcTravelDocumentReaderDelegate {
  
  func nfcTravelDocumentReader(_ nfcTravelDocumentReader: NfcTravelDocumentReaderProtocol, succeeded travelDocument: TravelDocument) {
    guard let bytes = travelDocument.encodedIdentificationFeaturesFace.faceImage?.bytes, let image = UIImage(data: bytes), let cgImage = image.cgImage else {
      promise.reject(error: .init(message: Error.missingFaceImage.localizedDescription))
      return
    }
    do {
      let result = try createResult(cgImage: cgImage, result: travelDocument)
      promise.resolve(result: result)
    } catch {
      promise.reject(error: .init(message: error.localizedDescription))
    }
  }
  
  func nfcTravelDocumentReaderCanceled(_ nfcTravelDocumentReader: NfcTravelDocumentReaderProtocol) {
    promise.reject(error: .init(message: Error.nfcCanceled.localizedDescription))
  }
  
  func nfcTravelDocumentReader(_ nfcTravelDocumentReader: NfcTravelDocumentReaderProtocol, failed error: NfcTravelDocumentReaderError) {
    promise.reject(error: .init(message: Error.nfcFailed.localizedDescription))
  }
}
