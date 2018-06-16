LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# Include all java files.
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_MODULE := live-channels-partner-support
LOCAL_MODULE_CLASS := STATIC_JAVA_LIBRARIES
LOCAL_MODULE_TAGS := optional
LOCAL_SDK_VERSION := system_current
LOCAL_MIN_SDK_VERSION := 23

LOCAL_USE_AAPT2 := true

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_STATIC_JAVA_LIBRARIES := android-support-annotations

LOCAL_JAVA_LIBRARIES := \
    auto-value-jar \

LOCAL_ANNOTATION_PROCESSORS := \
    auto-value-jar-host \

LOCAL_ANNOTATION_PROCESSOR_CLASSES := \
    com.google.auto.value.processor.AutoValueProcessor


include $(LOCAL_PATH)/buildconfig.mk

include $(BUILD_STATIC_JAVA_LIBRARY)

include $(call all-makefiles-under,$(LOCAL_PATH))
