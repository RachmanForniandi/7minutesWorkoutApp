package rachman.forniandi.A7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rachman.forniandi.A7minutesworkoutapp.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private var binding:ActivityHistoryBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbHistory)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }

        binding?.tbHistory?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}