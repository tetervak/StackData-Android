package ca.tetervak.stackdata

import android.app.Application
import ca.tetervak.stackdata.data.DataContainer

class StackDataApplication: Application(){

    lateinit var container: DataContainer

    override fun onCreate() {
        super.onCreate()
        container = DataContainer(this)
    }
}