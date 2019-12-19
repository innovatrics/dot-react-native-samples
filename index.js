/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, { Component } from 'react';

import {
  AppRegistry,
  Button,
  NativeModules,
  StyleSheet,
  Text,
  View,
  Image,
} from 'react-native';

const activityStarter = NativeModules.ActivityStarter;

export default class ActivityDemoComponent extends Component {
  constructor(props) {
    super(props);
    this.state = { url: null, isInitialized: null };
  }

  componentDidMount() {
    if (this.state.isInitialized == null) {
      // init DOT, check license
      // resolves when everything is OK
      // reject when there was an error
      activityStarter
        .initDot()
        .then(() => {
          this.setState({
            isInitialized: true,
          });
        })
        .catch(() => {
          this.setState({
            isInitialized: false,
          });
        });
    }
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native ({this.props.buildType})!
        </Text>
        <Text style={styles.instructions}>
          <Text>To get started, edit </Text>
          <Text style={styles.bold}>index.js</Text>
          <Text>.</Text>
        </Text>
        <Text style={styles.instructions}>
          Double tap R on your keyboard to reload,{'\n'}
          Shake or press menu button for dev menu
        </Text>
        <View style={styles.buttonContainer}>
          <Button
            disabled={!this.state.isInitialized}
            onPress={() => {
              // opens a camera with native UI for document capture
              // returns a Promise that resolves to the path to a photo
              activityStarter
                .startDocumentCaptureActivity()
                .then(res => {
                  alert(res);
                  this.setState({ url: res });
                })
                .catch(err => alert(err));
            }}
            title="Document capture activity"
          />
          <Button
            disabled={!this.state.isInitialized}
            onPress={() => {
              // opens a native UI for liveness check
              // returns a Promise that resolves to object { score: number; photoUri: string }
              activityStarter
                .startLivenessCheckActivity()
                .then(res => {
                  alert(JSON.stringify(res, null, 2));
                })
                .catch(err => alert(err));
            }}
            title="Liveness check activity"
          />
        </View>
        {this.state.url && (
          <View style={styles.instructions}>
            <Image
              source={{
                uri: this.state.url,
              }}
              style={{
                width: 400,
                height: 300,
              }}
            />
          </View>
        )}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  bold: {
    fontWeight: 'bold',
  },
  buttonContainer: {
    height: 100,
    justifyContent: 'space-between',
    marginTop: 30,
  },
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#E5ECFF',
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  textInput: {
    backgroundColor: 'white',
    borderColor: 'gray',
    borderWidth: 1,
    height: 40,
    marginTop: 20,
    textAlign: 'center',
    width: '80%',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('DotReactNative', () => ActivityDemoComponent);
