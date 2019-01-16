LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# Include all java files.
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := SampleNetworkTuner
LOCAL_MODULE_TAGS := optional

LOCAL_JAVA_LIBRARIES := \
    guava-android-jar \

LOCAL_STATIC_JAVA_LIBRARIES := \
    auto-value-jar \
    android-support-annotations \
    error-prone-annotations-jar \
    jsr330 \
    lib-dagger \
    lib-exoplayer \
    lib-exoplayer-v2-core \

LOCAL_STATIC_ANDROID_LIBRARIES := \
    lib-dagger-android \
    lib-dagger-android-support \
    live-channels-partner-support \
    live-tv-tuner \
    tv-common \

LOCAL_ANNOTATION_PROCESSORS := \
    guava-jre-jar-host \
    jsr330 \
    lib-dagger-host \
    lib-dagger-android-jarimpl-host \
    lib-dagger-android-support-jarimpl-host \
    lib-dagger-android-processor-host \
    lib-dagger-compiler-host \
    lib-dagger-producers-host \
    lib-dagger-spi-host \
    lib-google-java-format-host \
    lib-javapoet-host \


LOCAL_ANNOTATION_PROCESSOR_CLASSES := \
   dagger.internal.codegen.ComponentProcessor,dagger.android.processor.AndroidProcessor

LOCAL_USE_AAPT2 := true

LOCAL_PROGUARD_ENABLED := disabled
LOCAL_SDK_VERSION := system_current
LOCAL_MIN_SDK_VERSION := 23  # M

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

include $(BUILD_PACKAGE)
