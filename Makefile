.PHONY: publish-to-bintray
publish-to-bintray:
	./gradlew clean assembleRelease sourcesJar javadocJar :libuplink-android:bintrayUpload