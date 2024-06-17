import Foundation
import DotDocument
import DotCore

extension DocumentAutoCaptureResult: Encodable {
  
  enum Keys: String, CodingKey {
    case bgraRawImage
    case documentDetectorResult
    case machineReadableZone
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(bgraRawImage, forKey: .bgraRawImage)
    try container.encode(documentDetectorResult, forKey: .documentDetectorResult)
    try container.encodeIfPresent(machineReadableZone, forKey: .machineReadableZone)
  }
}

extension MachineReadableZone: Encodable {
  
  enum Keys: String, CodingKey {
    case td1
    case td2
    case td3
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encodeIfPresent(td1, forKey: .td1)
    try container.encodeIfPresent(td2, forKey: .td2)
    try container.encodeIfPresent(td3, forKey: .td3)
  }
}

extension Td1MachineReadableZone: Encodable {
  
  enum Keys: String, CodingKey {
    case documentCode
    case issuingStateOrOrganization
    case documentNumber
    case dateOfBirth
    case sex
    case dateOfExpiry
    case nationality
    case name
    case optionalData
    case hasValidChecksum
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(documentCode, forKey: .documentCode)
    try container.encode(issuingStateOrOrganization, forKey: .issuingStateOrOrganization)
    try container.encode(documentNumber, forKey: .documentNumber)
    try container.encode(dateOfBirth, forKey: .dateOfBirth)
    try container.encode(sex, forKey: .sex)
    try container.encode(dateOfExpiry, forKey: .dateOfExpiry)
    try container.encode(nationality, forKey: .nationality)
    try container.encode(name, forKey: .name)
    try container.encode(optionalData, forKey: .optionalData)
    try container.encode(hasValidChecksum, forKey: .hasValidChecksum)
  }
}

extension Td2MachineReadableZone: Encodable {
  
  enum Keys: String, CodingKey {
    case documentCode
    case issuingStateOrOrganization
    case name
    case documentNumber
    case nationality
    case dateOfBirth
    case sex
    case dateOfExpiry
    case optionalData
    case hasValidChecksum
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(documentCode, forKey: .documentCode)
    try container.encode(issuingStateOrOrganization, forKey: .issuingStateOrOrganization)
    try container.encode(name, forKey: .name)
    try container.encode(documentNumber, forKey: .documentNumber)
    try container.encode(nationality, forKey: .nationality)
    try container.encode(dateOfBirth, forKey: .dateOfBirth)
    try container.encode(sex, forKey: .sex)
    try container.encode(dateOfExpiry, forKey: .dateOfExpiry)
    try container.encodeIfPresent(optionalData, forKey: .optionalData)
    try container.encode(hasValidChecksum, forKey: .hasValidChecksum)
  }
}

extension Td3MachineReadableZone: Encodable {
  
  enum Keys: String, CodingKey {
    case documentCode
    case issuingStateOrOrganization
    case name
    case passportNumber
    case nationality
    case dateOfBirth
    case sex
    case dateOfExpiry
    case personalNumberOrOtherOptionalData
    case hasValidChecksum
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(documentCode, forKey: .documentCode)
    try container.encode(issuingStateOrOrganization, forKey: .issuingStateOrOrganization)
    try container.encode(name, forKey: .name)
    try container.encode(passportNumber, forKey: .passportNumber)
    try container.encode(nationality, forKey: .nationality)
    try container.encode(dateOfBirth, forKey: .dateOfBirth)
    try container.encode(sex, forKey: .sex)
    try container.encode(dateOfExpiry, forKey: .dateOfExpiry)
    try container.encode(personalNumberOrOtherOptionalData, forKey: .personalNumberOrOtherOptionalData)
    try container.encode(hasValidChecksum, forKey: .hasValidChecksum)
  }
}

extension MrzNameElement: Encodable {
  
  enum Keys: String, CodingKey {
    case primaryElement
    case secondaryElement
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encodeIfPresent(primaryElement, forKey: .primaryElement)
    try container.encodeIfPresent(secondaryElement, forKey: .secondaryElement)
  }
}

extension MrzElement: Encodable {
  
  enum Keys: String, CodingKey {
    case value
    case positions
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(value, forKey: .value)
    try container.encode(positions, forKey: .positions)
  }
}

extension MrzStringPosition: Encodable {
  
  enum Keys: String, CodingKey {
    case lineIndex
    case startIndex
    case endIndex
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(lineIndex, forKey: .lineIndex)
    try container.encode(startIndex, forKey: .startIndex)
    try container.encode(endIndex, forKey: .endIndex)
  }
}

extension BgraRawImage: Encodable {
  
  enum Keys: String, CodingKey {
    case size
    case bytes
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(size, forKey: .size)
    try container.encode(bytes.description, forKey: .bytes)
  }
}

extension ImageSize: Encodable {
  
  enum Keys: String, CodingKey {
    case width
    case height
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(width, forKey: .width)
    try container.encode(height, forKey: .height)
  }
}

extension DocumentDetector.Result: Encodable {
  
  enum Keys: String, CodingKey {
    case confidence
    case widthToHeightRatio
    case corners
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(confidence, forKey: .confidence)
    try container.encode(widthToHeightRatio.value, forKey: .widthToHeightRatio)
    try container.encode(corners, forKey: .corners)
  }
}

extension Corners: Encodable {
  
  enum Keys: String, CodingKey {
    case topLeft
    case topRight
    case bottomRight
    case bottomLeft
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(topLeft, forKey: .topLeft)
    try container.encode(topRight, forKey: .topRight)
    try container.encode(bottomRight, forKey: .bottomRight)
    try container.encode(bottomLeft, forKey: .bottomLeft)
  }
}

extension PointDouble: Encodable {
  
  enum Keys: String, CodingKey {
    case x
    case y
  }
  
  public func encode(to encoder: Encoder) throws {
    var container = encoder.container(keyedBy: Keys.self)
    try container.encode(x, forKey: .x)
    try container.encode(y, forKey: .y)
  }
}

extension MrzValidation {
  
  static func from(string: String) -> MrzValidation? {
    switch string {
    case "VALIDATE_ALWAYS": return .validateAlways
    case "VALIDATE_IF_PRESENT": return .validateIfPresent
    case "NONE": return MrzValidation.none
    default: return nil
    }
  }
}
