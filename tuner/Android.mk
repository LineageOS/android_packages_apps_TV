#
# Copyright (C) 2019 The Android Open Source Project
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

# Include all java and proto files.
LOCAL_SRC_FILES := \
    $(call all-java-files-under, src) \


LOCAL_MODULE := live-tv-tuner
LOCAL_MODULE_CLASS := STATIC_JAVA_LIBRARIES
LOCAL_MODULE_TAGS := optional
LOCAL_SDK_VERSION := system_current

LOCAL_USE_AAPT2 := true

LOCAL_RESOURCE_DIR := \
    $(LOCAL_PATH)/res \

LOCAL_JAVA_LIBRARIES := \
    tv-auto-value-jar \
    tv-auto-factory-jar \
    android-support-annotations \
    tv-error-prone-annotations-jar \
    tv-guava-android-jar \
    tv-javax-annotations-jar \
    jsr330 \
    tv-lib-dagger \
    tv-lib-exoplayer \
    tv-lib-exoplayer-v2-core \
    live-tv-tuner-proto \

LOCAL_SHARED_ANDROID_LIBRARIES := \
    android-support-compat \
    android-support-core-ui \
    android-support-v7-palette \
    android-support-v7-recyclerview \
    android-support-v17-leanback \
    androidx.tvprovider_tvprovider \
    tv-lib-dagger-android \
    tv-common \

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

include $(BUILD_STATIC_JAVA_LIBRARY)

include $(call all-makefiles-under,$(LOCAL_PATH))
