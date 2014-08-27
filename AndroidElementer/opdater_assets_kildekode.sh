cd ~/android/android-eksempler/AndroidElementer
svn up
cd assets/
rm -rf src AndroidManifest.xml
cp -a ../src ../AndroidManifest.xml .
svn commit -m "Ny kildekode" .

cd ..
ant clean
mkdir gen
rm -rf out
cd ..
rm -f AndroidElementer.zip
zip -9r AndroidElementer.zip AndroidElementer
scp AndroidElementer.zip javabog.dk:javabog.dk/filer/android/
