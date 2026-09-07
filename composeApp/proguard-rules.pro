# Proguard / R8 rules for KiblatSaya

# Keep Compose Multiplatform internals & annotations
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Domain Models
-keep class com.kiblatsaya.app.domain.** { *; }

# Multiplatform Settings
-keep class com.russhwolf.settings.** { *; }

# Kotlin Coroutines
-dontwarn kotlinx.coroutines.**
-keepclassmembers class kotlinx.coroutines.** { *; }
