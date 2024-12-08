package com.example.final1

import android.os.AsyncTask

@Deprecated("Use alternative concurrency APIs like Coroutines")
class NetworkTask : AsyncTask<String, Void, String>() {
    @Deprecated("Use alternative concurrency APIs like Coroutines")
    override fun doInBackground(vararg params: String?): String {
        // Perform network operation to fetch BBC news
        return "Result"
    }

    @Deprecated("Use alternative concurrency APIs like Coroutines")
    override fun onPostExecute(result: String?) {
        // Update UI with result
    }
}