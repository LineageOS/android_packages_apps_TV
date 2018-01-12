LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := tests

# Include all test java files.
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := UsbTunerTvInputTests

LOCAL_STATIC_JAVA_LIBRARIES := \
    android-support-test \
    tv-test-common \

LOCAL_JAVA_LIBRARIES := \
    android.test.runner.stubs \
    android.test.base.stubs \
    android.test.mock.stubs \


LOCAL_INSTRUMENTATION_FOR := LiveTv

LOCAL_SDK_VERSION := current
LOCAL_MIN_SDK_VERSION := 23  # M

include $(BUILD_PACKAGE)
