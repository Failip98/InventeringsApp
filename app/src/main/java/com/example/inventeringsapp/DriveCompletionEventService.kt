package com.example.inventeringsapp

import android.util.Log
import com.google.android.gms.drive.events.CompletionEvent
import com.google.android.gms.drive.events.DriveEventService

class DriveCompletionEventService : DriveEventService() {
    override fun onCompletion(event: CompletionEvent) {
        when {
            event.status == CompletionEvent.STATUS_SUCCESS -> {
                // Do here whatever we want.
                val driveId = event.driveId
                Log.d("___",driveId.resourceId.toString())
                // Finally dismiss completed event. We no longer need it.
                event.dismiss()
            }
        }
    }
}