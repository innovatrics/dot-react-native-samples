# Native DOT libs in React Native

## Android

### Java side

We use a source code from `com.innovatrics.android.dot.sample@2.12.0` (found in `android/app/src/main/java/com/innovatrics/android/dot/sample`). It's inlined in this repository. Final React Native app shares the same package name, as the `iFace` uses this name for license check (same as hardware id on desktop).

Documented code responsible for bridge between the native and javascript code can be found in:

- `ActivityStarterModule.java` - A native module is a Java class that usually extends the ReactContextBaseJavaModule class and implements the functionality required by the JavaScript.
- `ActivityStarterReactPackage.java` - Apps package that extends `ReactPackage` and uses `createNativeModules` to register a native module
- `MainApplication.java` - We have to provide the package to `getPackages` method of the `MainApplication` - this is pre-existing file/class created by react-native

Inspiration for this approach can be found here: https://github.com/petterh/react-native-android-activity. More information about native modules here: https://facebook.github.io/react-native/docs/native-modules-android.

### Javascript side

We can use `ActivityStarter` class found in `NativeModules` exported from `react-native`. Code can be found in `index.js` (root of the repo).

Example:

```javascript
import { NativeModules } from 'react-native';

NativeModules.ActivityStarter.initDot().then(...).catch(...);
...
...
async getLivenessData() {
  const { score, photoUri } = await NativeModules.ActivityStarter.startLivenessCheckActivity();
};
```
