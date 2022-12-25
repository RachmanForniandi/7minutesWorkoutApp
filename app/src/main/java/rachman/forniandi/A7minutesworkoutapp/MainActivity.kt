package rachman.forniandi.A7minutesworkoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import rachman.forniandi.A7minutesworkoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.flStart?.setOnClickListener {
            //Toast.makeText(this@MainActivity,"Start the exercise !!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding?.flBMI?.setOnClickListener {
            val intentBmi = Intent(this,BMIActivity::class.java)
            startActivity(intentBmi)
        }

        binding?.flHistory?.setOnClickListener {
            val intentHistory = Intent(this,HistoryActivity::class.java)
            startActivity(intentHistory)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}