PATH  := node_modules/.bin:$(PATH)
SHELL := /bin/bash

example:
	rninit init RNLinkedinLoginExample --source react-native@0.44.2;
	mv RNLinkedinLoginExample example;

install:
	cd example && npm install file:../ --save && react-native link;
	cp -rf src/for_example_only/Example.js example/Example.js;
	cp -rf src/for_example_only/index.android.js example/index.android.js;
	cp -rf src/for_example_only/index.ios.js example/index.ios.js;

uninstall:
	cd example && npm uninstall react-native-linkedin-login --save;

run-android:
	cd example && react-native run-android;

run-ios:
	cd example && react-native run-ios;

clean:
	rm -rf RNLinkedinLoginExample; 
	rm -rf example;