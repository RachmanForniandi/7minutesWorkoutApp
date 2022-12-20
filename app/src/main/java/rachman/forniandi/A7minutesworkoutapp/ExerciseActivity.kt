package rachman.forniandi.A7minutesworkoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import rachman.forniandi.A7minutesworkoutapp.databinding.ActivityExerciseBinding
import rachman.forniandi.A7minutesworkoutapp.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding:ActivityExerciseBinding?= null

    private var restTimer:CountDownTimer? = null
    private var restProgress=0

    private var exerciseTimer:CountDownTimer? = null
    private var exerciseProgress=0
    private var exerciseTimerDuration:Long=10
    private var exerciseList:ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts:TextToSpeech? =null
    private var player:MediaPlayer?= null
    private var exerciseAdapter:ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbExercise)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = ConstantsData.defaultExerciseList()
        tts = TextToSpeech(this,this)

        binding?.tbExercise?.setNavigationOnClickListener {
            showDialogForBackButton()
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
        binding?.tvUpcomingExerciseName?.text =
            exerciseList!![currentExercisePosition+1].getName()

        setRestView()
        setupExerciseStatus()
    }

    override fun onBackPressed() {
        showDialogForBackButton()
    }

    private fun showDialogForBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.optionYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.optionNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setupExerciseStatus(){
        /*binding?.listNumberStatus?.layoutManager = LinearLayoutManager(
            this,LinearLayoutManager.HORIZONTAL,false)*/
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.listNumberStatus?.adapter = exerciseAdapter

    }

    private fun setupExerciseView(){

        try {
            val soundURI = Uri.parse("android.resource://eu.tutorials.a7_minutesworkoutapp/" + R.raw.press_start)
            player = MediaPlayer.create(this,soundURI)
            player?.isLooping
            player?.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
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

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.imgExerciseMove?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text =exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar(){
        binding?.pg2?.progress = exerciseProgress

        exerciseTimer = object :CountDownTimer(exerciseTimerDuration*1000,1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.pg2?.progress = exerciseTimerDuration.toInt() - exerciseProgress
                binding?.tvTimerExercise?.text = (exerciseTimerDuration.toInt() - exerciseProgress).toString()
            }

            override fun onFinish() {
                /*Toast.makeText(this@ExerciseActivity,"30 seconds are over.Let's rest !!",
                    Toast.LENGTH_SHORT).show()
*/
                if (currentExercisePosition < exerciseList!!.size -1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter?.notifyDataSetChanged()
                    setupRestView()
                }else{
                    /*Toast.makeText(this@ExerciseActivity,"Congratulations ! You have completed the 7 minutes workout !!",
                        Toast.LENGTH_SHORT).show()*/
                    finish()
                    val intentToFinish = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intentToFinish)

                }
            }

        }.start()
    }

    private fun setRestView(){
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

                //exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter?.notifyDataSetChanged()

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
        if (tts != null){
            tts?.stop()
            tts?.shutdown()
        }

        if (player != null){
            player?.stop()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The language specified is not  supported!")
            }
        }else{
            Log.e("TTS","Initialization failed !")

        }
    }

    private fun speakOut(txt:String){
        tts?.speak(txt,TextToSpeech.QUEUE_FLUSH,null,"")
    }
}