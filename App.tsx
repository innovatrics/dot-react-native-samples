import React, { useEffect, useState } from 'react';
import {
  NativeModules,
  StyleSheet,
  Text,
  View,
  Image,
  Modal,
  Pressable,
  SafeAreaView,
  ScrollView,
} from 'react-native';

const dotSdk = NativeModules.DotSdk

class DotSdkResult {
  constructor(
    public imageUri: string | null,
    public jsonData: string,
  ) { }
}

class NfcKey {
  constructor(
    public documentNumber: string,
    public dateOfExpiry: string,
    public dateOfBirth: string,
  ) { }
}

function getNfcKeyFromResult(result: DotSdkResult): NfcKey {
  const jsonData = JSON.parse(result.jsonData);
  if (jsonData.machineReadableZone.td1 != null) {
    return new NfcKey(
      jsonData.machineReadableZone.td1.documentNumber.value,
      jsonData.machineReadableZone.td1.dateOfExpiry.value,
      jsonData.machineReadableZone.td1.dateOfBirth.value
    )
  } else if (jsonData.machineReadableZone.td2 != null) {
    return new NfcKey(
      jsonData.machineReadableZone.td2.documentNumber.value,
      jsonData.machineReadableZone.td2.dateOfExpiry.value,
      jsonData.machineReadableZone.td2.dateOfBirth.value
    )
  } else {
    return new NfcKey(
      jsonData.machineReadableZone.td3.passportNumber.value,
      jsonData.machineReadableZone.td3.dateOfExpiry.value,
      jsonData.machineReadableZone.td3.dateOfBirth.value
    )
  }
}

function App(): React.JSX.Element {

  const [isSdkInitialized, setSdkInitialized] = useState<boolean>(false);
  const [nfcKey, setNfcKey] = useState<NfcKey | null>(null);
  const [result, setResult] = useState<DotSdkResult | null>(null);

  useEffect(() => {
    dotSdk.isInitialized()
      .then((isInitialized: boolean) => {
        if (!isInitialized) {
          return dotSdk.initialize();
        } else {
          Promise.resolve();
        }
      })
      .then(() => {
        setSdkInitialized(true);
        console.log("DotSdk initialized successfully");
      })
      .catch((error: any) => {
        setSdkInitialized(false);
        console.error('Error initializing dotSdk:', error);
      });
  });

  useEffect(() => {
    if (nfcKey != null) {
      setNfcKey(null)

      dotSdk.startNfcReading(JSON.stringify(nfcKey))
        .then((result: DotSdkResult) => {
          setResult(result);
        })
        .catch((error: any) => {
          console.log("Error starting NFC reading activity", error);
        })
    }
  })

  return (
    <ScrollView>
      <SafeAreaView style={styles.container}>
        <Image
          source={require('./assets/img/logo-dot.png')}
          style={styles.logo}
        />
        <Text style={styles.welcome}>Welcome to React Native!</Text>
        <Text style={styles.instructions}>
          <Text>This is a demo of DOT for Android/iOS in React Native.</Text>
        </Text>
        <Text style={styles.instructions}>
          Clicking on buttons below will call DOT kit native code.
        </Text>
        <Pressable
          disabled={!isSdkInitialized}
          onPress={() => {
            dotSdk.startDocumentAutoCapture(null)
              .then((result: DotSdkResult) => {
                setResult(result);
              })
              .catch((error: any) => {
                console.log("Error occured during Document Auto Capture", error);
              })
          }}
          style={[styles.button, !isSdkInitialized ? styles.buttonDisabled : null]}>
          <Text
            style={[
              styles.buttonText,
              !isSdkInitialized ? styles.buttonTextDisabled : null,
            ]}>
            {'Document Auto Capture'}
          </Text>
        </Pressable>
        <Pressable
          disabled={!isSdkInitialized}
          onPress={() => {
            dotSdk.startDocumentAutoCapture(JSON.stringify({
              mrzValidation: "VALIDATE_ALWAYS"
            }))
              .then((result: DotSdkResult) => {
                setNfcKey(getNfcKeyFromResult(result))
              })
              .catch((error: any) => {
                console.log("Error occured during Document Auto Capture", error);
              })
          }}
          style={[styles.button, !isSdkInitialized ? styles.buttonDisabled : null]}>
          <Text
            style={[
              styles.buttonText, !isSdkInitialized ? styles.buttonTextDisabled : null,
            ]}>
            {'NFC Reading'}
          </Text>
        </Pressable>
        <Pressable
          disabled={!isSdkInitialized}
          onPress={() => {
            dotSdk.startFaceAutoCapture()
              .then((result: DotSdkResult) => {
                setResult(result);
              })
              .catch((error: any) => {
                console.log("Error occured during Face Auto Capture", error);
              })
          }}
          style={[styles.button, !isSdkInitialized ? styles.buttonDisabled : null]}>
          <Text
            style={[
              styles.buttonText,
              !isSdkInitialized ? styles.buttonTextDisabled : null,
            ]}>
            {'Face Auto Capture'}
          </Text>
        </Pressable>
        <Pressable
          disabled={!isSdkInitialized}
          onPress={() => {
            dotSdk.startSmileLiveness()
              .then((result: DotSdkResult) => {
                setResult(result);
              })
              .catch((error: any) => {
                console.log("Error occured during Smile Liveness", error);
              })
          }}
          style={[styles.button, !isSdkInitialized ? styles.buttonDisabled : null]}>
          <Text
            style={[
              styles.buttonText,
              !isSdkInitialized ? styles.buttonTextDisabled : null,
            ]}>
            {'Smile Liveness'}
          </Text>
        </Pressable>
        <Pressable
          disabled={!isSdkInitialized}
          onPress={() => {
            dotSdk.startMagnifEyeLiveness()
              .then((result: DotSdkResult) => {
                setResult(result);
              })
              .catch((error: any) => {
                console.log("Error occured during MagnifEye Liveness", error);
              })
          }}
          style={[styles.button, !isSdkInitialized ? styles.buttonDisabled : null]}>
          <Text
            style={[
              styles.buttonText,
              !isSdkInitialized ? styles.buttonTextDisabled : null,
            ]}>
            {'MagnifEye Liveness'}
          </Text>
        </Pressable>
        <Modal
          animationType="slide"
          transparent={true}
          visible={!!result}
          onRequestClose={() => {
            setResult(null);
          }}>
          <View style={styles.centeredView}>
            <View style={styles.modalView}>
              <ScrollView>
                {result?.imageUri ? (
                  <Image
                    source={{
                      uri: `${result?.imageUri}`,
                    }}
                    style={styles.resultImage}
                    resizeMode="contain"
                  />
                ) : null}
                <Text style={styles.resultText}>
                  {result?.jsonData}
                </Text>
              </ScrollView>
              <Pressable
                style={[styles.button, styles.buttonClose]}
                onPress={() => setResult(null)}>
                <Text style={styles.buttonCloseText}>Close</Text>
              </Pressable>
            </View>
          </View>
        </Modal>
      </SafeAreaView>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  logo: {
    width: 238,
    height: 100,
    marginBottom: 50,
    marginTop: 50,
  },
  centeredView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 22,
  },
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 14,
    paddingHorizontal: 28,
    elevation: 3,
    backgroundColor: '#42BE65',
    borderRadius: 26,
    marginBottom: 30,
  },
  buttonDisabled: {
    backgroundColor: '#354967',
  },
  buttonTextDisabled: {
    color: '#808DA0',
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 14,
    lineHeight: 24,
    textAlign: 'center',
  },
  resultText: {
    color: '#5e5e5e',
  },
  container: {
    flex: 1,
    alignItems: 'center',
    color: '#FFFFFF',
    backgroundColor: '#021B41',
  },
  instructions: {
    textAlign: 'center',
    color: '#FFFFFF',
    marginBottom: 5,
  },
  resultImage: {
    width: 300,
    height: 300,
    marginBottom: 30,
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
    color: '#FFFFFF',
  },
  modalView: {
    margin: 20,
    backgroundColor: 'white',
    borderRadius: 16,
    paddingTop: 30,
    paddingLeft: 20,
    paddingRight: 20,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
  },
  buttonClose: {
    borderWidth: 2,
    borderStyle: 'solid',
    borderColor: '#42BE65',
    backgroundColor: '#FFFFFF',
  },
  buttonCloseText: {
    color: '#42BE65',
  },
});

export default App;
