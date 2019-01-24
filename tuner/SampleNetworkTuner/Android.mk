LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# Include all java files.
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := SampleNetworkTuner
LOCAL_MODULE_TAGS := optional

LOCAL_JAVA_LIBRARIES := \
    tv-guava-android-jar \

LOCAL_STATIC_JAVA_LIBRARIES := \
    tv-auto-value-jar \
    android-support-annotations \
    tv-error-prone-annotations-jar \
    jsr330 \
    tv-lib-dagger \
    tv-lib-exoplayer \
    tv-lib-exoplayer-v2-core \

LOCAL_STATIC_ANDROID_LIBRARIES := \
    tv-lib-dagger-android \
    live-channels-partner-support \
    live-tv-tuner \
    tv-common \

LOCAL_ANNOTATION_PROCESSORS := \
    tv-guava-jre-jar \
    jsr330 \
    tv-lib-dagger-android-processor\
    tv-lib-dagger-compiler \


LOCAL_ANNOTATION_PROCESSOR_CLASSES := \
   dagger.internal.codegen.ComponentProcessor,dagger.android.processor.AndroidProcessor

LOCAL_USE_AAPT2 := true

LOCAL_PROGUARD_ENABLED := disabled
LOCAL_SDK_VERSION := system_current
LOCAL_MIN_SDK_VERSION := 23  # M

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

include $(BUILD_PACKAGE)
