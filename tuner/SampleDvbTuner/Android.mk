LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# Include all java files.
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := SampleDvbTuner
LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_ANDROID_LIBRARIES := \
    live-channels-partner-support \
    live-tv-tuner \
    tv-common \

LOCAL_USE_AAPT2 := true

LOCAL_PROGUARD_ENABLED := disabled
LOCAL_SDK_VERSION := system_current
LOCAL_MIN_SDK_VERSION := 23  # M

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

include $(BUILD_PACKAGE)
