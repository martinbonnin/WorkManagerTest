package mbonnin.net.workmanagertest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.work.*
import java.lang.Exception
import java.net.InetAddress

class MainActivity : AppCompatActivity() {
    class TestWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork(): Result {

            println("doWork called")
            val host = "graphql.api.dailymotion.com"
            try {
                val address = InetAddress.getAllByName(host)
                activity?.runOnUiThread {
                    activity?.findViewById<TextView>(R.id.text)?.setText("DNS request resolved correctly")
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    activity?.findViewById<TextView>(R.id.text)
                        ?.setText("DNS request resolved failed: ${e.message}")
                }
            }

            return Result.success()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.text)
            .setText("Enable wifi and wait of the Worker to do its work")

        val workManager = WorkManager.getInstance(this)
        val request = OneTimeWorkRequest.Builder(TestWorker::class.java)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build())

        activity = this

        workManager.enqueue(request.build())
    }
    companion object {
        /**
         * I know this leaks but it's for the purpose of the Demo
         */
        var activity: AppCompatActivity? = null
    }
}
