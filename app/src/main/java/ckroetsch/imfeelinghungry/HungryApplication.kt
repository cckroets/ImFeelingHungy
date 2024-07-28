package ckroetsch.imfeelinghungry

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize

class HungryApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(context = this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }
}
