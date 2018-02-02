LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# Include all java files.
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_MODULE := live-channels-partner-support
LOCAL_MODULE_CLASS := STATIC_JAVA_LIBRARIES
LOCAL_MODULE_TAGS := optional
LOCAL_SDK_VERSION := system_current

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_STATIC_JAVA_LIBRARIES := android-support-annotations
LOCAL_STATIC_JAVA_LIBRARIES += auto-value-jar

# Libraries needed by the compiler (JACK) to generate code.
PROCESSOR_LIBRARIES_TARGET := \
    auto-value-jar \

# Resolve the jar paths.
PROCESSOR_JARS := $(call java-lib-deps, $(PROCESSOR_LIBRARIES_TARGET))
# Necessary for annotation processors to work correctly.
LOCAL_ADDITIONAL_DEPENDENCIES += $(PROCESSOR_JARS)

LOCAL_JACK_FLAGS += --processorpath $(call normalize-path-list,$(PROCESSOR_JARS))
LOCAL_JAVACFLAGS += -processorpath $(call normalize-path-list,$(PROCESSOR_JARS))

include $(LOCAL_PATH)/buildconfig.mk

include $(BUILD_STATIC_JAVA_LIBRARY)

include $(call all-makefiles-under,$(LOCAL_PATH))


#############################################################
# Pre-built dependency jars
#############################################################
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += auto-value-jar:../../../../../prebuilts/tools/common/m2/repository/com/google/auto/value/auto-value/1.3/auto-value-1.3.jar



