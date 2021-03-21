#!/bin/bash

# Bomb out on first error
set -e

# Change to the directory of this script, if not already
pushd "$(dirname "$0")"

echo -e "\nDetecting Java..."
java -version

# Compile
echo -e "\nCompiling..."
javac SoundRecorder.java

# Jar
echo -e "\nCreating jar..."
jar cvfe SoundRecorder.jar SoundRecorder SoundRecorder.class

# Create App Structure
echo -e "\nCreating empty bundle..."
rm -rf SoundRecorder.app
mkdir -p SoundRecorder.app/Contents/MacOS
mkdir -p SoundRecorder.app/Contents/Resources

# Copy jar
echo -e "\nCopying jar..."
cp SoundRecorder.jar SoundRecorder.app/SoundRecorder.jar

# Create Launcher
echo -e "\nCreating launcher..."
echo -e "#!/bin/bash\njava -jar \"\$(dirname "\$0")/../../SoundRecorder.jar\" > /tmp/soundrecorder.log 2>&1\n" > SoundRecorder.app/Contents/MacOS/SoundRecorder
chmod +x SoundRecorder.app/Contents/MacOS/SoundRecorder

# Creating icon
echo -e "\nCopying resource files..."
cp /usr/share/httpd/icons/small/sound.png SoundRecorder.app/Contents/Resources/SoundRecorder.png || true
cp Info.plist SoundRecorder.app/Contents/Info.plist

echo -e "\nCleaning up..."
rm -f SoundRecorder.class
rm -f SoundRecorder.jar

popd
