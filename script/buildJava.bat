
javac -d ./dist *.java
cd dist
jar cfm neko.jar ../MANIFEST.MF -C . .
copy neko.jar ..