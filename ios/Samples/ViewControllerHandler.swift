import Foundation

class ViewControllerHandler {
  
  enum Error: LocalizedError {
    case failedToEncode
    
    var errorDescription: String? {
      return "Failed to encode the result."
    }
  }
  
  let promise: ReactNativePromise
  let viewController: UIViewController
  
  private let imageWriter: ImageWriter
  private let encoder: JSONEncoder = {
      let encoder = JSONEncoder()
      encoder.outputFormatting = .prettyPrinted
      return encoder
  }()
  
  init(promise: ReactNativePromise, viewController: UIViewController, imageWriter: ImageWriter) {
    self.promise = promise
    self.viewController = viewController
    self.imageWriter = imageWriter
  }
  
  func present(on topViewController: UIViewController) {
    guard !viewController.isBeingDismissed, !viewController.isBeingPresented, viewController.presentingViewController == nil else { return }
    viewController.modalPresentationStyle = .fullScreen
    addDismissButton()
    topViewController.present(viewController, animated: true)
  }
  
  func dismiss() {
    viewController.dismiss(animated: true)
  }
  
  func createResult<T: Encodable>(cgImage: CGImage, result: T) throws -> ReactNativeResult {
    guard let jsonString = String(data: try encoder.encode(result), encoding: .utf8) else { throw Error.failedToEncode }
    let imageUrl = try imageWriter.write(cgImage: cgImage)
    return .init(imageUri: imageUrl.absoluteString, jsonData: jsonString)
  }
  
  private func addDismissButton() {
    
    let dismissButton = UIButton(type: .system)
    dismissButton.setTitle("Back", for: .normal)
    dismissButton.setTitleColor(.orange, for: .normal)
    viewController.view.addSubview(dismissButton)
    dismissButton.translatesAutoresizingMaskIntoConstraints = false
    dismissButton.addTarget(self, action: #selector(dismissAction), for: .touchUpInside)
    
    NSLayoutConstraint.activate([
      dismissButton.widthAnchor.constraint(equalToConstant: 80),
      dismissButton.heightAnchor.constraint(equalToConstant: 50),
      dismissButton.leadingAnchor.constraint(equalTo: viewController.view.safeAreaLayoutGuide.leadingAnchor),
      dismissButton.topAnchor.constraint(equalTo: viewController.view.safeAreaLayoutGuide.topAnchor)
    ])
  }
  
  @objc private func dismissAction() {
      dismiss()
  }
}
