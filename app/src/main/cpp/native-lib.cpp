#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring
Java_com_george_news_NewsActivityKt_getAPIKey(JNIEnv *env, jclass clazz) {
std::string api_key = "f71af7261c434b5d8be60816ed910d8b";
return env->NewStringUTF(api_key.c_str());
}