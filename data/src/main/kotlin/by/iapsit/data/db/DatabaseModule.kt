package by.iapsit.data.db

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            NotificationsDatabase::class.java,
            NotificationsDatabase.DATABASE_NAME
        ).build()
    }
    single { get<NotificationsDatabase>().getNotificationsDao() }
    single { get<NotificationsDatabase>().getPackagesDao() }
}