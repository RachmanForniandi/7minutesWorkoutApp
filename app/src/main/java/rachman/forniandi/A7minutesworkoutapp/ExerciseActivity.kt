package rachman.forniandi.A7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import rachman.forniandi.A7minutesworkoutapp.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding:ActivityExerciseBinding?= null

    private var restTimer:CountDownTimer? = null
    private var restProgress=0

    private var exerciseTimer:CountDownTimer? = null
    private var exerciseProgress=0

    private var exerciseList:ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbExercise)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = ConstantsData.defaultExerciseList()

        binding?.tbExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        //binding?.flProgressBar?.visibility = View.INVISIBLE
        setupRestView()
    }

    private fun setupRestView(){
        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility= View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.imgExerciseMove?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
        if (restTimer != null){
            restTimer?.cancel()
            restProgress =0
        }
        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition+1].getName()

        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility= View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.imgExerciseMove?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE
        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress =0
        }

        binding?.imgExerciseMove?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text =exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar(){
        binding?.pg2?.progress = exerciseProgress

        exerciseTimer = object :CountDownTimer(3000,1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.pg2?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()

            }

            override fun onFinish() {
                /*Toast.makeText(this@ExerciseActivity,"30 seconds are over.Let's rest !!",
                    Toast.LENGTH_SHORT).show()
*/
                if (currentExercisePosition < exerciseList!!.size -1){
                    setupRestView()
                }else{
                    Toast.makeText(this@ExerciseActivity,"Congratulations ! You have completed the 7 minutes workout !!",
                        Toast.LENGTH_SHORT).show()
                }
            }

        }.start()
    }

    private fun setRestProgressBar(){
        binding?.pg1?.progress = restProgress

        restTimer = object :CountDownTimer(10000,1000){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.pg1?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()

            }

            override fun onFinish() {
                /*Toast.makeText(this@ExerciseActivity,"Here now we will start the exercise !!",
                    Toast.LENGTH_SHORT).show()*/
                currentExercisePosition++
                setupExerciseView()
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        /*if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }*/

        binding = null
    }
}