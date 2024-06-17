import UIKit
import DotCore
import DotFaceCore

class FaceAutoCaptureSampleResult {
    
    let confidence: Double
    let faceAspects: FaceAspects
    let faceQuality: FaceQuality

    init(confidence: Double, faceAspects: FaceAspects, faceQuality: FaceQuality) {
        self.confidence = confidence
        self.faceAspects = faceAspects
        self.faceQuality = faceQuality
    }
}

extension FaceAutoCaptureSampleResult {
  
  static func from(detectedFace: DetectedFace) throws -> FaceAutoCaptureSampleResult {
    let faceAspects = try detectedFace.evaluateFaceAspects()
    let faceQuality = try detectedFace.evaluateFaceQuality()
    
    return FaceAutoCaptureSampleResult(confidence: detectedFace.confidence,
                                       faceAspects: faceAspects,
                                       faceQuality: faceQuality)
  }
}

extension FaceAutoCaptureSampleResult: Encodable {
    
    enum Keys: String, CodingKey {
        case confidence
        case faceAspects
        case faceQuality
    }
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: Keys.self)
        try container.encode(confidence, forKey: .confidence)
        try container.encode(faceAspects, forKey: .faceAspects)
        try container.encode(faceQuality, forKey: .faceQuality)
    }
}
