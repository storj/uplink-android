.PHONY: publish-to-bintray
publish-to-bintray:
	./gradlew clean assembleRelease sourcesJar javadocJar :libuplink-android:bintrayUpload

.PHONY: build-shared-libraries
build-shared-libraries:
	./scripts/build-android.sh