cd ~/android/android-eksempler/AndroidElementer/app/src/main/assets/
rm -rf src AndroidManifest.xml
cp -a ../java ../AndroidManifest.xml .

cd ../../../..
# ./gradlew  assembleDebug
# scp app/build/outputs/apk/app-debug.apk javabog.dk:javabog.dk/filer/android/AndroidElementer-debug.apk
#
# ant clean
# mkdir gen
# rm -rf out
# cd ..
# rm -f AndroidElementer.zip
# zip -9r AndroidElementer.zip AndroidElementer
# scp AndroidElementer.zip javabog.dk:javabog.dk/filer/android/

