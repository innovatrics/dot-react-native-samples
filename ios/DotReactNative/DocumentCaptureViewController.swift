//
//  DocumentCaptureViewController.swift
//  DotReactNative
//
//  Created by Pavol Porubský on 30/12/2019.
//  Copyright © 2019 Innovatrics. All rights reserved.
//

import UIKit
import AVKit

protocol DocumentCaptureViewControllerDelegate: class {
    func didPressRightBarButton(for viewController: DocumentCaptureViewController)
    func didTakePhoto(_ photo: UIImage)
}

final class DocumentCaptureViewController: UIViewController {

    weak var delegate: DocumentCaptureViewControllerDelegate?
    
    private let topView = UIView()
    private let captureView = CapturePreviewView()
    private let bottomView = UIView()
    
    private let titleLabel = UILabel()
    private let documentSideLabel = UILabel()
    
    private let takePhotoButton = UIButton(type: .system)
    
    private let captureSession = CaptureSession()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white
        
        setupSubviews()
        
        captureSession.delegate = self
        captureSession.previewLayer = captureView.capturePreviewLayer
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        captureSession.startSession()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        captureSession.stopSession()
    }
    
    @objc private func takePhotoButtonAction(_ sender: UIButton) {
        captureSession.takePhoto()
    }

}

extension DocumentCaptureViewController {
    
    func setupSubviews() {
        
        let setupSubview: (UIView) -> Void = { subview in
            self.view.addSubview(subview)
            subview.translatesAutoresizingMaskIntoConstraints = false
        }
        setupSubview(captureView)
        setupSubview(topView)
        setupSubview(bottomView)
        
        captureView.capturePreviewLayer.videoGravity = .resizeAspectFill
        
        topView.backgroundColor = .white
        bottomView.backgroundColor = .white
        
        let setupTopLabels: (UILabel) -> Void = { label in
            self.topView.addSubview(label)
            label.translatesAutoresizingMaskIntoConstraints = false
            label.numberOfLines = 0
            label.textAlignment = .center
        }
        setupTopLabels(titleLabel)
        setupTopLabels(documentSideLabel)
        
        titleLabel.text = "Take a picture\nof your ID document"
        
        documentSideLabel.text = "Front side"
        documentSideLabel.textColor = .red
        
        
        bottomView.addSubview(takePhotoButton)
        takePhotoButton.translatesAutoresizingMaskIntoConstraints = false
        takePhotoButton.setTitle("Take Photo", for: .normal)
        takePhotoButton.addTarget(self, action: #selector(takePhotoButtonAction(_:)), for: .touchUpInside)
        
        NSLayoutConstraint.activate([
            topView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            topView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor),
            topView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor),
            topView.heightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.heightAnchor, multiplier: 0.3),
            
            captureView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor),
            captureView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor),
            captureView.topAnchor.constraint(equalTo: topView.bottomAnchor),
            captureView.bottomAnchor.constraint(equalTo: bottomView.topAnchor),
            
            bottomView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor),
            bottomView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor),
            bottomView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
            bottomView.heightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.heightAnchor, multiplier: 0.15),
            
            titleLabel.leadingAnchor.constraint(equalTo: topView.leadingAnchor, constant: 10),
            titleLabel.topAnchor.constraint(equalTo: topView.topAnchor, constant: 13),
            titleLabel.trailingAnchor.constraint(equalTo: topView.trailingAnchor, constant: -10),
            
            documentSideLabel.leadingAnchor.constraint(equalTo: topView.leadingAnchor, constant: 10),
            documentSideLabel.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 7),
            documentSideLabel.trailingAnchor.constraint(equalTo: topView.trailingAnchor, constant: -10),
            
            takePhotoButton.centerYAnchor.constraint(equalTo: bottomView.centerYAnchor),
            takePhotoButton.centerXAnchor.constraint(equalTo: bottomView.centerXAnchor),
            takePhotoButton.heightAnchor.constraint(equalToConstant: 50),
            takePhotoButton.widthAnchor.constraint(equalToConstant: 200)
        ])
    }
}

extension DocumentCaptureViewController: CaptureSessionDelegate {
    
    func didCapturePhoto(_ photoImage: UIImage, with captureSession: CaptureSession) {
      delegate?.didTakePhoto(photoImage)
    }
}

class PreviewVC: UIViewController {
    
    let originalImageView = UIImageView()
    let imageView = UIImageView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        view.addSubview(imageView)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.contentMode = .scaleAspectFit
        
        view.addSubview(originalImageView)
        originalImageView.translatesAutoresizingMaskIntoConstraints = false
        originalImageView.contentMode = .scaleAspectFit
        
        NSLayoutConstraint.activate([
            imageView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor),
            imageView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            imageView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor),
            imageView.heightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.heightAnchor, multiplier: 0.5),
            
            originalImageView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor),
            originalImageView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
            originalImageView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor),
            originalImageView.heightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.heightAnchor, multiplier: 0.5)
        ])
    }
}

protocol CaptureSessionDelegate: class {
    func didCapturePhoto(_ photoImage: UIImage, with captureSession: CaptureSession)
}

final class CaptureSession: NSObject {
    
    weak var delegate: CaptureSessionDelegate?
    
    private let session = AVCaptureSession()
    private let sessionQueue = DispatchQueue(label: "CaptureSession.sessionQueue")
    private var didSetupSession = false
    private let photoOutput: AVCapturePhotoOutput = {
        let output = AVCapturePhotoOutput()
        output.isHighResolutionCaptureEnabled = true
        return output
    }()
    private let videoinput: AVCaptureDeviceInput? = {
        guard let videoDevice = AVCaptureDevice.DiscoverySession(deviceTypes: [.builtInDualCamera, .builtInWideAngleCamera], mediaType: .video, position: .back).devices.first else { return nil }
        return try? AVCaptureDeviceInput(device: videoDevice)
    }()
    
    weak var previewLayer: AVCaptureVideoPreviewLayer? {
        didSet {
            previewLayer?.session = session
        }
    }
    
    var isCameraAuthorized: Bool {
        return AVCaptureDevice.authorizationStatus(for: .video) == .authorized
    }
    
    func startSession() {
        sessionQueue.async {
            if !self.didSetupSession {
                self.setupSession()
            }
            
            if !self.session.isRunning {
                self.session.startRunning()
            }
        }
    }
    
    func stopSession() {
        sessionQueue.async {
            if self.session.isRunning {
                self.session.stopRunning()
            }
        }
    }
    
    func takePhoto() {
        
        let videoPreviewLayerOrientation = previewLayer?.connection?.videoOrientation
        
        sessionQueue.async {
            if let photoOutputConnection = self.photoOutput.connection(with: .video), let videoOrientation = videoPreviewLayerOrientation {
                photoOutputConnection.videoOrientation = videoOrientation
            }
            var photoSettings = AVCapturePhotoSettings()
            if self.photoOutput.availablePhotoCodecTypes.contains(.jpeg) {
                photoSettings = AVCapturePhotoSettings(format: [AVVideoCodecKey: AVVideoCodecType.jpeg])
            }
            
            if self.videoinput?.device.isFlashAvailable ?? false {
                //photoSettings.flashMode = .auto
            }
            
            photoSettings.isHighResolutionPhotoEnabled = true
            
            self.photoOutput.capturePhoto(with: photoSettings, delegate: self)
        }
    }
    
    private func setupSession() {
        guard let videoInput = self.videoinput else {
            //delegate callback
            return
        }
        
        guard session.canAddInput(videoInput) else {
            //delegate callback
            return
        }
        
        guard session.canAddOutput(photoOutput) else {
            //delegate callback
            return
        }
        
        session.beginConfiguration()
        session.addInput(videoInput)
        session.addOutput(photoOutput)
        session.sessionPreset = .photo
        session.commitConfiguration()
        
        didSetupSession = true
    }
}

extension CaptureSession: AVCapturePhotoCaptureDelegate {
    
    func photoOutput(_ output: AVCapturePhotoOutput, willBeginCaptureFor resolvedSettings: AVCaptureResolvedPhotoSettings) {
        
    }

    func photoOutput(_ output: AVCapturePhotoOutput, willCapturePhotoFor resolvedSettings: AVCaptureResolvedPhotoSettings) {
        
    }

    func photoOutput(_ output: AVCapturePhotoOutput, didCapturePhotoFor resolvedSettings: AVCaptureResolvedPhotoSettings) {
        
    }
    
    func photoOutput(_ output: AVCapturePhotoOutput, didFinishProcessingPhoto photo: AVCapturePhoto, error: Error?) {
        if let data = photo.fileDataRepresentation(), let image = UIImage(data: data) {
            delegate?.didCapturePhoto(image, with: self)
        }
    }
    
    func photoOutput(_ output: AVCapturePhotoOutput, didFinishCaptureFor resolvedSettings: AVCaptureResolvedPhotoSettings, error: Error?) {
        
    }
}



final class CapturePreviewView: UIView {
    
    override class var layerClass: AnyClass {
        return AVCaptureVideoPreviewLayer.self
    }
    
    // swiftlint:disable force_cast
    var capturePreviewLayer: AVCaptureVideoPreviewLayer {
        return layer as! AVCaptureVideoPreviewLayer
    }
    // swiftlint:enable force_cast
}
