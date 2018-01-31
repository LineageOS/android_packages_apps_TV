LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# Include all java files.
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := PartnerSupportSampleTvInput
LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := \
    android-support-annotations \
    android-support-compat \
    android-support-core-ui \
    android-support-v7-recyclerview \
    android-support-v17-leanback \
    live-channels-partner-support \
    android-support-tv-provider \


LOCAL_AAPT_FLAGS := --auto-add-overlay \
    --extra-packages android.support.compat \
    --extra-packages android.support.v7.recyclerview \
    --extra-packages android.support.v17.leanback \

LOCAL_PROGUARD_ENABLED := disabled
# Overlay view related functionality requires system APIs.
LOCAL_SDK_VERSION := system_current
LOCAL_MIN_SDK_VERSION := 23  # M

# Required for com.android.tv.permission.RECEIVE_INPUT_EVENT
LOCAL_PRIVILEGED_MODULE := true

LOCAL_RESOURCE_DIR := \
    $(TOP)/prebuilts/sdk/current/support/compat/res \
    $(TOP)/prebuilts/sdk/current/support/v7/recyclerview/res \
    $(TOP)/prebuilts/sdk/current/support/v17/leanback/res \
    $(LOCAL_PATH)/res

include $(BUILD_PACKAGE)
