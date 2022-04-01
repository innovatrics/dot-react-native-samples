/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useEffect, useState} from 'react';
import type {Node} from 'react';
import {
  NativeModules,
  StyleSheet,
  Text,
  View,
  Image,
  Modal,
  Pressable,
  SafeAreaView,
} from 'react-native';

//Activity starter of Native module
const activityStarter = NativeModules.ActivityStarter;

const App: () => Node = () => {
  const [url, setUrl] = useState(null);
  const [isInitialized, setIsInitialized] = useState(null);

  useEffect(() => {
    if (isInitialized == null) {
      // init DOT, check license
      // resolves when everything is OK
      // reject when there was an error
      activityStarter
        .initDot()
        .then(() => {
          setIsInitialized(true);
        })
        .catch(() => {
          setIsInitialized(false);
        });
    }
  });

  return (
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
      <View style={styles.buttonContainer}>
        <Pressable
          disabled={!isInitialized}
          onPress={() => {
            // opens a camera with native UI for document capture
            // returns a Promise that resolves to the path to a photo
            activityStarter
              .startDocumentCaptureActivity()
              .then(res => {
                setUrl(res);
              })
              .catch(e => alert(e));
          }}
          style={[styles.button, !isInitialized ? styles.buttonDisabled : '']}>
          <Text
            style={[
              styles.buttonText,
              !isInitialized ? styles.buttonTextDisabled : '',
            ]}>
            {'Document capture activity'}
          </Text>
        </Pressable>
        <Pressable
          disabled={!isInitialized}
          onPress={() => {
            // opens a native UI for liveness check
            // returns a Promise that resolves to object { score: number; photoUri: string }
            activityStarter
              .startLivenessCheckActivity()
              .then(res => {
                alert(`Liveness score: ${res.score}`);
              })
              .catch(e => alert(e));
          }}
          style={[styles.button, !isInitialized ? styles.buttonDisabled : '']}>
          <Text
            style={[
              styles.buttonText,
              !isInitialized ? styles.buttonTextDisabled : '',
            ]}>
            {'Liveness check activity'}
          </Text>
        </Pressable>
        <Pressable
          disabled={!isInitialized}
          onPress={() => {
            // opens a native UI for face autocapture
            // returns a Promise that resolves to object { score: number; photoUri: string }
            activityStarter
              .startFaceAutoCaptureActivity()
              .then(res => {
                setUrl(res);
              })
              .catch(e => alert(e));
          }}
          style={[styles.button, !isInitialized ? styles.buttonDisabled : '']}>
          <Text
            style={[
              styles.buttonText,
              !isInitialized ? styles.buttonTextDisabled : '',
            ]}>
            {'Face autocapture activity'}
          </Text>
        </Pressable>
      </View>
      <Modal
        animationType="slide"
        transparent={true}
        visible={!!url}
        onRequestClose={() => {
          setUrl(null);
        }}>
        <View style={styles.centeredView}>
          <View style={styles.modalView}>
            <Image
              source={{
                uri: `data:image/png;base64,${url}`,
              }}
              style={styles.resultImage}
              resizeMode="contain"
            />
            <Pressable
              style={[styles.button, styles.buttonClose]}
              onPress={() => setUrl(null)}>
              <Text style={styles.buttonCloseText}>Close</Text>
            </Pressable>
          </View>
        </View>
      </Modal>
    </SafeAreaView>
  );
};

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
  buttonContainer: {
    height: 100,
    justifyContent: 'space-between',
    marginTop: 30,
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
