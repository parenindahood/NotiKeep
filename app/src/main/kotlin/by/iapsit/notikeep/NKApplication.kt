package by.iapsit.notikeep

import android.app.Application
import by.iapsit.data.di.dataModule
import by.iapsit.notikeep.di.presentationModule
import by.iapsit.notikeep.workers.DeleteNotificationsWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

@Suppress("UNUSED")
class NKApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NKApplication)
            workManagerFactory()
            modules(dataModule, presentationModule)
        }
        DeleteNotificationsWorker.enqueue(this)
    }
}