LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# Include all common java files.
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_SRC_FILES += $(call all-proto-files-under, src)

LOCAL_MODULE := tv-common
LOCAL_MODULE_CLASS := STATIC_JAVA_LIBRARIES
LOCAL_MODULE_TAGS := optional
LOCAL_SDK_VERSION := system_current

LOCAL_PROTOC_OPTIMIZE_TYPE := lite
LOCAL_PROTOC_FLAGS := --proto_path=$(LOCAL_PATH)/src/


LOCAL_USE_AAPT2 := true

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_JAVA_LIBRARIES := \
    tv-auto-value-jar \
    tv-auto-factory-jar \
    android-support-annotations \
    tv-error-prone-annotations-jar \
    tv-guava-android-jar \
    jsr330 \
    tv-lib-dagger \
    tv-lib-exoplayer \
    tv-lib-exoplayer-v2-core \


LOCAL_DISABLE_RESOLVE_SUPPORT_LIBRARIES := true

LOCAL_SHARED_ANDROID_LIBRARIES := \
    android-support-compat \
    android-support-core-ui \
    android-support-v7-recyclerview \
    android-support-v17-leanback \

LOCAL_STATIC_ANDROID_LIBRARIES := \
    tv-lib-dagger-android \

LOCAL_ANNOTATION_PROCESSORS := \
    tv-auto-value-jar \
    tv-auto-factory-jar \
    tv-guava-jre-jar \
    tv-javawriter-jar \
    tv-javax-annotations-jar \
    jsr330 \

LOCAL_ANNOTATION_PROCESSOR_CLASSES := \
  com.google.auto.factory.processor.AutoFactoryProcessor,com.google.auto.value.processor.AutoValueProcessor

LOCAL_MIN_SDK_VERSION := 23

# TODO(b/77284273): generate build config after dagger supports libraries
#include $(LOCAL_PATH)/buildconfig.mk

include $(BUILD_STATIC_JAVA_LIBRARY)
