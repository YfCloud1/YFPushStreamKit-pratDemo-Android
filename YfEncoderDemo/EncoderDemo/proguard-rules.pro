# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontpreverify
#-dontskipnonpubliclibraryclassmembers
#-verbose
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#-keepattributes *Annotation*,EnclosingMethod
#-keepattributes Signature
#-ignorewarnings
#-optimizations Dmaximum.inlined.code.length=4000
#
#-keepattributes *Annotation*
#-keepattributes Signature

#-libraryjars libs/armeabi-v7a/libaudioprocessing_so.so
#-libraryjars libs/armeabi-v7a/libc++_shared.so
#-libraryjars libs/armeabi-v7a/libffmpeg.so
#-libraryjars libs/armeabi-v7a/libK2Pagent.so
#-libraryjars libs/armeabi-v7a/libmuxer.so
#-libraryjars libs/armeabi-v7a/libnama.so
#-libraryjars libs/armeabi-v7a/libprotobuf_lite.cr.so
#-libraryjars libs/arm64-v8a/libaudioprocessing_so.so
#-libraryjars libs/arm64-v8a/libc++_shared.so
#-libraryjars libs/arm64-v8a/libffmpeg.so
#-libraryjars libs/arm64-v8a/libK2Pagent.so
#-libraryjars libs/arm64-v8a/libmuxer.so
#-libraryjars libs/arm64-v8a/libnama.so
#-libraryjars libs/arm64-v8a/libprotobuf_lite.cr.so
#-libraryjars libs/YfEncoderKit.jar

#-dontwarn android.support.v4.**
#-dontwarn org.apache.commons.net.**
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#-keepclasseswithmembernames class * {
#    public <init>(android.content.Context, android.Util.AttributeSet);
# }
#-keepclasseswithmembernames class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context);
#}
#-dontshrink
#-dontoptimize
#-dontwarn android.webkit.WebView
#-dontwarn java.nio.file.Files
#-dontwarn java.nio.file.Path
#-dontwarn java.nio.file.OpenOption
#-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#
#-keep public class * extends android.app.Fragment
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.support.annotations.**
#-keep public class com.android.vending.licensing.ILicensingService
#-keep class android.annotation.** { *; }
#-keep public class * extends android.app.Fragment
#-keep public class * extends android.app.FragmentActivity
#-keep public class * extends android.support.v4.app.FragmentActivity
#-keep public class * extends android.support.v4.app.Fragment
#-keep public class * extends android.os.Binder
#-keep class * implements android.os.Parcelable {*;}
#-keep class * implements java.io.Serializable {*;}

#-printmapping mapping.txt
#-printseeds seeds.txt
#-printusage unused.txt

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}