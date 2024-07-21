package ckroetsch.imfeelinghungry

import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class AppCheckProvider {

    val factory: AppCheckProviderFactory = PlayIntegrityAppCheckProviderFactory.getInstance()

}
