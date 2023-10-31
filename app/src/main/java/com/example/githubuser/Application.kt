package com.example.githubuser

import android.app.Application
import com.medomeckz.core.di.databaseModule
import com.medomeckz.core.di.networkModule
import com.medomeckz.core.di.repositoryModule
import com.example.githubuser.di.useCaseModule
import com.example.githubuser.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@Application)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}