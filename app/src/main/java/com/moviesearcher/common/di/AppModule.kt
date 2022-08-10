package com.moviesearcher.common.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.moviesearcher.common.credentials.CredentialsHolder
import com.moviesearcher.common.credentials.CredentialsHolderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSecuredSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        return EncryptedSharedPreferences.create(
            "MuviPrefs",
            mainKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideCredentialsHolder(sharedPreferences: SharedPreferences): CredentialsHolder =
        CredentialsHolderImpl(sharedPreferences)
}