LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# Include all java and proto files.
LOCAL_SRC_FILES := \
    $(call all-java-files-under, src) \
    $(call all-proto-files-under, proto)


LOCAL_MODULE := live-tv-tuner
LOCAL_MODULE_CLASS := STATIC_JAVA_LIBRARIES
LOCAL_MODULE_TAGS := optional
LOCAL_SDK_VERSION := system_current

LOCAL_USE_AAPT2 := true

LOCAL_PROTOC_OPTIMIZE_TYPE := nano
LOCAL_PROTOC_FLAGS := --proto_path=$(LOCAL_PATH)/proto/
LOCAL_PROTO_JAVA_OUTPUT_PARAMS := enum_style=java

LOCAL_RESOURCE_DIR := \
    $(LOCAL_PATH)/res \
    $(TOP)/prebuilts/sdk/current/support/v17/leanback/res \

LOCAL_STATIC_JAVA_LIBRARIES := \
    tv-common \
    lib-exoplayer \
    lib-exoplayer-v2 \
    lib-exoplayer-v2-core \
    android-support-annotations \
    android-support-compat \
    android-support-core-ui \
    android-support-tv-provider \
    android-support-v7-palette \
    android-support-v7-recyclerview \
    android-support-v17-leanback \
    android-support-tv-provider \
    javax-annotations-jar \

LOCAL_AAPT_FLAGS := --auto-add-overlay \
    --extra-packages android.support.v17.leanback \
    --extra-packages com.android.tv.common \

include $(LOCAL_PATH)/buildconfig.mk

include $(BUILD_STATIC_JAVA_LIBRARY)

