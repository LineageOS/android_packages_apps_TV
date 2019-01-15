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

LOCAL_JAVA_LIBRARIES := \
    guava-android-jar \

LOCAL_STATIC_JAVA_LIBRARIES := \
    auto-value-jar \
    auto-factory-jar \
    android-support-annotations \
    error-prone-annotations-jar \
    jsr330 \
    lib-dagger \
    lib-exoplayer \
    lib-exoplayer-v2-core \

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
    lib-dagger-android \
    lib-dagger-android-support \
    live-channels-partner-support \
    live-tv-tuner \
    tv-common \

LOCAL_ANNOTATION_PROCESSORS := \
    auto-value-jar-host \
    auto-factory-jar-host \
    guava-jre-jar-host \
    javawriter-jar-host \
    jsr330 \
    lib-dagger-host \
    lib-dagger-android-jarimpl-host \
    lib-dagger-android-support-jarimpl-host \
    lib-dagger-android-processor-host \
    lib-dagger-compiler-host \
    lib-dagger-producers-host \
    lib-dagger-spi-host \
    lib-google-java-format-host \
    lib-javapoet-host \


LOCAL_ANNOTATION_PROCESSOR_CLASSES := \
  com.google.auto.factory.processor.AutoFactoryProcessor,com.google.auto.value.processor.AutoValueProcessor,dagger.internal.codegen.ComponentProcessor,dagger.android.processor.AndroidProcessor


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
# name,path,version
m2-path =../../../prebuilts/tools/common/m2/repository/$2/$1/$3/$1-$3.jar
m2 = $1-jar:$(call m2-path,$1,$2,$3)

prebuilts := \
    $(call m2,auto-value,com/google/auto/value,1.5.2) \
    $(call m2,auto-factory,com/google/auto/factory,1.0-beta2) \
    $(call m2,javawriter,com/squareup,2.5.1) \
    error-prone-annotations-jar:$(call m2-path,error_prone_annotations,com/google/errorprone,2.3.1) \
    guava-jre-jar:$(call m2-path,guava,com/google/guava,23.5-jre) \
    guava-android-jar:$(call m2-path,guava,com/google/guava,23.6-android) \
    javax-annotations-jar:$(call m2-path,javax.annotation-api,javax/annotation,1.2) \
    lib-dagger:libs/dagger-2.15.jar \
    lib-dagger-compiler:libs/dagger-compiler-2.15.jar \
    lib-dagger-android:libs/dagger-android-2.15.aar \
    lib-dagger-android-jarimpl:libs/dagger-android-jarimpl-2.15.jar \
    lib-dagger-android-processor:libs/dagger-android-processor-2.15.jar \
    lib-dagger-android-support:libs/dagger-android-support-2.15.aar \
    lib-dagger-android-support-jarimpl:libs/dagger-android-support-jarimpl-2.15.jar \
    lib-dagger-producers:libs/dagger-producers-2.15.jar \
    lib-dagger-spi:libs/dagger-spi-2.15.jar \
    lib-exoplayer:libs/exoplayer-r1.5.16.aar \
    lib-exoplayer-v2-core:libs/exoplayer-core-2.9.0.aar \
    lib-google-java-format:libs/google-java-format-1.4-all-deps.jar \
    lib-javapoet:libs/javapoet-1.8.0.jar \
    truth-0-36-prebuilt-jar:$(call m2-path,truth,com/google/truth,0.36) \

define define-prebuilt
  $(eval tw := $(subst :, ,$(strip $(1)))) \
  $(eval include $(CLEAR_VARS)) \
  $(eval LOCAL_MODULE := $(word 1,$(tw))) \
  $(eval LOCAL_MODULE_TAGS := optional) \
  $(eval LOCAL_MODULE_CLASS := JAVA_LIBRARIES) \
  $(eval LOCAL_SRC_FILES := $(word 2,$(tw))) \
  $(eval LOCAL_UNINSTALLABLE_MODULE := true) \
  $(eval LOCAL_SDK_VERSION := current) \
  $(eval include $(BUILD_PREBUILT)) \
  $(eval include $(CLEAR_VARS)) \
  $(eval LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := $(word 1,$(tw))-host:$(word 2,$(tw))) \
  $(eval include $(BUILD_HOST_PREBUILT))
endef

$(foreach p,$(prebuilts),\
  $(call define-prebuilt,$(p)))

prebuilts :=

include $(call all-makefiles-under,$(LOCAL_PATH))
