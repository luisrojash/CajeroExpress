#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL

/*/Java_pe_farmacias_peruanas_cajeroexpress_di_myBaseUrl(
        JNIEnv *env,
        jobject) {
    std::string myBaseUrl= "http://10.18.3.15:8080/";
    return env->NewStringUTF(myBaseUrl.c_str());
}*/

Java_pe_farmacias_peruanas_cajeroexpress_di_CoreDataModule_stringBaseLocal(
        JNIEnv *env,
        jobject /* this */) {
    std::string baseUrl = "http://10.18.3.20:8080/";
    return env->NewStringUTF(baseUrl.c_str());
}
