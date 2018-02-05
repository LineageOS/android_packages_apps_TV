#
# Copyright (C) 2015 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)


LOCAL_MODULE_TAGS := optional

include $(LOCAL_PATH)/version.mk

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := LiveTv

# It is required for com.android.providers.tv.permission.ALL_EPG_DATA
LOCAL_PRIVILEGED_MODULE := true

LOCAL_SDK_VERSION := system_current
LOCAL_MIN_SDK_VERSION := 23  # M

LOCAL_USE_AAPT2 := true

LOCAL_RESOURCE_DIR := \
    $(LOCAL_PATH)/res \

LOCAL_STATIC_JAVA_LIBRARIES := \
    android-support-annotations \
    lib-exoplayer \
    lib-exoplayer-v2-core \
    jsr330-jar \

LOCAL_STATIC_ANDROID_LIBRARIES := \
    android-support-compat \
    android-support-core-ui \
    android-support-tv-provider \
    android-support-v4 \
    android-support-v7-appcompat \
    android-support-v7-palette \
    android-support-v7-preference \
    android-support-v7-recyclerview \
    android-support-v14-preference \
    android-support-v17-leanback \
    android-support-v17-preference-leanback \
    live-channels-partner-support \
    live-tv-tuner \
    tv-common \


LOCAL_JAVACFLAGS := -Xlint:deprecation -Xlint:unchecked

LOCAL_AAPT_FLAGS += \
    --version-name "$(version_name_package)" \
    --version-code $(version_code_package) \

LOCAL_JNI_SHARED_LIBRARIES := libtunertvinput_jni
LOCAL_AAPT_FLAGS += --extra-packages com.android.tv.tuner

include $(BUILD_PACKAGE)

#############################################################
# Pre-built dependency jars
#############################################################
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
    lib-exoplayer:libs/exoplayer-r1.5.16.aar \
    lib-exoplayer-v2-core:libs/exoplayer-core-2-SNAPHOT-20180114.aar \

# TODO use external/jsr330
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += auto-value-jar:../../../prebuilts/tools/common/m2/repository/com/google/auto/value/auto-value/1.5.2/auto-value-1.5.2.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += guava-22-0-jar:../../../prebuilts/tools/common/m2/repository/com/google/guava/guava/22.0/guava-22.0.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += javax-annotations-jar:../../../prebuilts/tools/common/m2/repository/javax/annotation/javax.annotation-api/1.2/javax.annotation-api-1.2.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += jsr330-jar:../../../prebuilts/tools/common/m2/repository/javax/inject/javax.inject/1/javax.inject-1.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += truth-0-36-prebuilt-jar:../../../prebuilts/tools/common/m2/repository/com/google/truth/truth/0.36/truth-0.36.jar


include $(BUILD_MULTI_PREBUILT)

include $(call all-makefiles-under,$(LOCAL_PATH))
