#include <jni.h>
#include <android/log.h>
#include <string>

#define TAG "MuseOS-Core"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    LOGI("🎨 MuseOS Core Library loaded");
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL
Java_com_muse_core_MuseOSCore_nativeInit(JNIEnv* env, jobject thiz) {
    LOGI("Initializing MuseOS native core...");
    // Initialize system-level services
}

} // extern "C"
