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

LOCAL_SRC_FILES += $(call all-java-files-under, src)

# TODO(b/77284273): Stop compiling everything at once dagger properly supported in libraries
LOCAL_SRC_FILES += $(call all-java-files-under, common/src)
LOCAL_SRC_FILES += $(call all-proto-files-under, common/src)

LOCAL_SRC_FILES += $(call all-java-files-under, tuner/src)



LOCAL_PACKAGE_NAME := LiveTv

# TODO(b/122608868) turn proguard back on
LOCAL_PROGUARD_ENABLED := disabled

# It is required for com.android.providers.tv.permission.ALL_EPG_DATA
LOCAL_PRIVILEGED_MODULE := true

LOCAL_SDK_VERSION := system_current
LOCAL_MIN_SDK_VERSION := 23  # M

LOCAL_USE_AAPT2 := true

LOCAL_RESOURCE_DIR := \
    $(LOCAL_PATH)/res \
    $(LOCAL_PATH)/material_res \
    $(LOCAL_PATH)/common/res \
    $(LOCAL_PATH)/tuner/res \

LOCAL_JAVA_LIBRARIES := \
    tv-guava-android-jar \

LOCAL_STATIC_JAVA_LIBRARIES := \
    tv-auto-value-jar \
    tv-auto-factory-jar \
    android-support-annotations \
    tv-error-prone-annotations-jar \
    jsr330 \
    tv-lib-dagger \
    tv-lib-exoplayer \
    tv-lib-exoplayer-v2-core \
    live-tv-tuner-proto \

LOCAL_STATIC_ANDROID_LIBRARIES := \
    android-support-compat \
    android-support-core-ui \
    androidx.tvprovider_tvprovider \
    android-support-v4 \
    android-support-v7-appcompat \
    android-support-v7-palette \
    android-support-v7-preference \
    android-support-v7-recyclerview \
    android-support-v14-preference \
    android-support-v17-leanback \
    android-support-v17-preference-leanback \
    tv-lib-dagger-android \
    live-channels-partner-support \

LOCAL_ANNOTATION_PROCESSORS := \
    tv-auto-value-jar \
    tv-auto-factory-jar \
    tv-guava-jre-jar \
    tv-lib-dagger-android-processor \
    tv-lib-dagger-compiler \


LOCAL_ANNOTATION_PROCESSOR_CLASSES := \
  com.google.auto.factory.processor.AutoFactoryProcessor,com.google.auto.value.processor.AutoValueProcessor,dagger.internal.codegen.ComponentProcessor,dagger.android.processor.AndroidProcessor


LOCAL_JAVACFLAGS := -Xlint:deprecation -Xlint:unchecked

LOCAL_AAPT_FLAGS += \
    --version-name "$(version_name_package)" \
    --version-code $(version_code_package) \

LOCAL_JNI_SHARED_LIBRARIES := libtunertvinput_jni
LOCAL_AAPT_FLAGS += --extra-packages com.android.tv.tuner
LOCAL_AAPT_FLAGS += --extra-packages com.android.tv.common

include $(BUILD_PACKAGE)

include $(call all-makefiles-under,$(LOCAL_PATH))
