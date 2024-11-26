import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters


class ResetStepsWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {

        val sharedPreferences: SharedPreferences =
            applicationContext.getSharedPreferences("StepCounter", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("totalSteps", 0)
        editor.apply()

        return Result.success()
    }
}
