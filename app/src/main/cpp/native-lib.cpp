#include <jni.h>
#include <string>
#include <cstdio>
#include <iostream>
#include <string>
#include <memory>
#include<cstring>
std::string list()
{
    std::string total_txt;
    char buffer[121] = {0};
    FILE *fp = popen("su -c pm list packages -3", "r");
    while (fgets(buffer, sizeof(buffer), fp) != nullptr)
    {
        total_txt +=  buffer;
    }
    pclose(fp);

    return total_txt;
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_myapplication_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string final = list();
    return env->NewStringUTF(final.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_myapplication_MainActivity_getAppPath(JNIEnv* env, jobject /* this */, jstring packageName) {
    FILE *banaw  = popen("su -c mkdir /storage/emulated/0/Download/extractor", "r");
    pclose(banaw);
    const char* app = env->GetStringUTFChars(packageName, nullptr);
    std::string command = "su -c pm path " + std::string(app);
    env->ReleaseStringUTFChars(packageName, app);
    FILE *fp  = popen(command.c_str(), "r");
    std::string total_txt;
    char buffer[121] = {0};
    std::string line;
    while (fgets(buffer, sizeof(buffer), fp) != nullptr)
    {

        total_txt +=  buffer;
    }
    pclose(fp);
    return env->NewStringUTF(total_txt.c_str());
}



extern "C" JNIEXPORT jstring JNICALL
Java_com_example_myapplication_MainActivity_extractor(JNIEnv* env, jobject /* this */, jstring packagepath) {
    const char* path = env->GetStringUTFChars(packagepath, nullptr);
    if (path == nullptr) {
        return env->NewStringUTF("Error: Unable to get package path.");
    }

    std::string command = "su -c cp " + std::string(path) + " /storage/emulated/0/Download/extractor/";

    FILE *fp = popen(command.c_str(), "r");
    if (fp == nullptr) {
        env->ReleaseStringUTFChars(packagepath, path);
        return env->NewStringUTF("Error: popen failed.");
    }

    pclose(fp);
    env->ReleaseStringUTFChars(packagepath, path);

    return env->NewStringUTF("Success");
}
