package com.namseox.st146_docxreader.module

import android.content.Context
import com.namseox.st146_docxreader.data.repository.FolderRepository
import com.namseox.st146_docxreader.utils.SharedPreferenceUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Singleton
    @Provides
    fun providerSharedPreference(@ApplicationContext appContext: Context): SharedPreferenceUtils {
        return SharedPreferenceUtils.getInstance(appContext)
    }

    @Singleton
    @Provides
    fun providerFolderRepository(@ApplicationContext appContext: Context): FolderRepository {
        return FolderRepository(appContext)
    }
}