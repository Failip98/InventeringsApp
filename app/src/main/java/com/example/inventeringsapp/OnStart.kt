package com.example.inventeringsapp

import android.app.Application
import com.example.inventeringsapp.main.MainActivity
import com.example.inventeringsapp.sheet.SheetActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component()
interface ApplicationComponent {
    fun inject(activity: SheetActivity)
    fun inject(activity: MainActivity)
}

class OnStart : Application() {

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.create()
    }

}