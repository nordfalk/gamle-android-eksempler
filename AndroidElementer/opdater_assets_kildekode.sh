cd ~/android/android-eksempler/AndroidElementer
cd assets/
rm -rf src AndroidManifest.xml
cp -a ../src ../AndroidManifest.xml .

cd ..
ant debug
scp bin/AndroidElementer-debug.apk javabog.dk:javabog.dk/filer/android/

ant clean
mkdir gen
rm -rf out
cd ..
rm -f AndroidElementer.zip
zip -9r AndroidElementer.zip AndroidElementer
scp AndroidElementer.zip javabog.dk:javabog.dk/filer/android/

