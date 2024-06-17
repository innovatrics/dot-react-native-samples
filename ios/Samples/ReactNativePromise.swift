import Foundation

struct ReactNativeResult {
  let imageUri: String?
  let jsonData: String
}

struct ReactNativeError {
  let code: String = "1"
  let message: String
}

final class ReactNativePromise {
  
  let resolveBlock: RCTPromiseResolveBlock
  let rejectBlock: RCTPromiseRejectBlock
  
  init(resolveBlock: @escaping RCTPromiseResolveBlock, rejectBlock: @escaping RCTPromiseRejectBlock) {
    self.resolveBlock = resolveBlock
    self.rejectBlock = rejectBlock
  }
  
  func resolve(result: ReactNativeResult) {
    resolveBlock(["imageUri": result.imageUri, "jsonData": result.jsonData])
  }
  
  func reject(error: ReactNativeError) {
    rejectBlock(error.code, error.message, nil)
  }
}
