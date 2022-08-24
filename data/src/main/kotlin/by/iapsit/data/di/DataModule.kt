package by.iapsit.data.di

import by.iapsit.core.repositories.NotificationsRepository
import by.iapsit.data.db.databaseModule
import by.iapsit.data.repositories.NotificationsRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get()) }
    includes(databaseModule)
}