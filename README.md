# Native DOT SDK in React Native

This repository contains a basic example on how to use DOT SDK (iOS/Android) in React Native (typescript).

# Getting Started

>**Note**: Make sure you have completed the [React Native - Environment Setup](https://reactnative.dev/docs/environment-setup) instructions till "Creating a new application" step, before proceeding. This project was developed and tested using Node.js v22.2.0.

## Step 1: Set up project

To obtain a license file, please contact `sales@innovatrics.com`.

### Android

1. Copy the license file to `android/app/src/main/res/raw/dot_license.lic`.

### iOS

1. Copy the license file to `ios/Assets/dot_license.lic`.
1. Run `pod install --repo-update` from `ios` directory.
1. Open `DotReactNativeSamples.xcworkspace` and update app signing configuration using your certificates and profiles.

## Step 2: Start the Metro Server

You will need to start **Metro**, the JavaScript _bundler_ that ships _with_ React Native. First, make sure you have all dependencies. Run:

```bash
npm install
```

To start Metro, run the following command from the _root_ of your this repository:

```bash
# using npm
npm start

# OR using Yarn
yarn start
```

## Step 3: Start Application

Let Metro Bundler run in its _own_ terminal. Open a _new_ terminal from the _root_ of this repository. Run the following command to start your _Android_ or _iOS_ app:

### For Android

```bash
# using npm
npm run android

# OR using Yarn
yarn android
```

### For iOS

```bash
# using npm
npm run ios

# OR using Yarn
yarn ios
```

If everything is set up _correctly_, you should see DOT SDK React Native Samples App running in your _Android Emulator_ or _iOS Simulator_ shortly provided you have set up your emulator/simulator correctly.

This is one way to run your app — you can also run it directly from within Android Studio and Xcode respectively.

# Technical details

## Android

Code responsible for bridge between the Kotlin and Typescript code can be found in:

- `DotSdkReactModule.kt` - A native module is a Kotlin class that extends the ReactContextBaseJavaModule class and implements the functionality required by the TypeScript. Most methods (annotated by `@ReactModule`) are callable from TypeScript and are responsible for starting activities and retrieving data from them.
- `DotSdkReactPackage.kt` - Apps package that extends `ReactPackage` and uses `createNativeModules` to register a native module.
- `MainApplication.kt` - We have to provide the package to `getPackages` method of the `MainApplication` - this is pre-existing file/class created by react-native

More information about native modules here: https://facebook.github.io/react-native/docs/native-modules-android.

## iOS

Code responsible for bridge between the Swift and Typescript code can be found in:

- `DotSdkReactModuleBridge.m` - A native module bridge which registers required information with React Native.
- `DotSdkReactModule.swift` - A native module Swift class which implements the functionality required by the TypeScript.

More information about native modules here: https://reactnative.dev/docs/native-modules-ios.

## ReactNative

We can use `DotSdk` class found in `NativeModules` exported from `react-native`. It will call native code for the corresponding platform (iOS/Android).

Code can be found in `App.tsx` (root of the repo).

Example:

```typescript
import { NativeModules } from 'react-native';

const dotSdk = NativeModules.DotSdk

// Initialize DOT SDK
dotSdk.initialize()
   .then(...)
   .catch(...);

...
...

// Start Document Auto Capture component

dotSdk.startDocumentAutoCapture(null)
   .then(...)
   .catch(...);

```
