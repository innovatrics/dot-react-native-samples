import Foundation

final class ImageWriter {
  
  enum Compression {
    case png
    case jpg
  }
  
  enum Error: LocalizedError {
    case failedToCompress
    
    var errorDescription: String? {
      return "Failed to compress the image."
    }
  }
  
  let baseUrl: URL
  
  convenience init() throws {
    let baseUrl = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask)[0].appendingPathComponent("capturedImages")
    try FileManager.default.createDirectory(at: baseUrl, withIntermediateDirectories: true)
    self.init(baseUrl: baseUrl)
  }
  
  init(baseUrl: URL) {
    self.baseUrl = baseUrl
  }
  
  func write(cgImage: CGImage, compression: Compression = .jpg) throws -> URL {
    switch compression {
    case .png: return try writePng(cgImage: cgImage)
    case .jpg: return try writeJpg(cgImage: cgImage)
    }
  }
  
  private func writePng(cgImage: CGImage) throws -> URL {
    guard let pngData = UIImage(cgImage: cgImage).pngData() else { throw Error.failedToCompress }
    let filename = UUID().uuidString + ".png"
    return try write(data: pngData, filename: filename)
  }
  
  private func writeJpg(cgImage: CGImage) throws -> URL {
    guard let jpgData = UIImage(cgImage: cgImage).jpegData(compressionQuality: 0.9) else { throw Error.failedToCompress }
    let filename = UUID().uuidString + ".jpg"
    return try write(data: jpgData, filename: filename)
  }
  
  private func write(data: Data, filename: String) throws -> URL {
    let fileUrl = baseUrl.appendingPathComponent(filename)
    try data.write(to: fileUrl)
    return fileUrl
  }
}
