# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

-keep class com.athkar.sa.models.AthkarCategory
-keep class com.athkar.sa.remote.ResponseCalendar
-keep class com.athkar.sa.remote.CalenderDto
-keep class com.athkar.sa.remote.CalenderDtoKt
-keep class com.athkar.sa.remote.TimingPrayDto
-keep class com.athkar.sa.remote.DateTimeStampDto
-keep class com.athkar.sa.remote.PrayInfoDto
-keep class com.athkar.sa.db.entity.Athkar
-keep class com.google.gson.reflect.TypeToken
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class retrofit.** { *; }
-keep class com.athkar.sa.quran.dto.QuranDataModelDto
-keep class com.athkar.sa.quran.dto.Reciter
-keep class com.athkar.sa.quran.dto.Moshaf
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken