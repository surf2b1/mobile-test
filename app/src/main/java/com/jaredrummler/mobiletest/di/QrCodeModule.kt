package com.jaredrummler.mobiletest.di

import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class QrCodeModule {

  @Provides
  @Singleton
  fun provideBarcodeEncoder() = BarcodeEncoder()

}