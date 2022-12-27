package rachman.forniandi.A7minutesworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

        val dao = (application as WorkOutApp).db.historyDao()

        getAllCompletedDates(dao)
    }

    private fun getAllCompletedDates(dao: HistoryDao) {
        Log.e("Date: ","getAllCompletedDates run")
        lifecycleScope.launch {
            dao.fetchAllDates().collect{ allCompletedDatesList->

                if (allCompletedDatesList.isNotEmpty()){
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvListHistory?.visibility = View.VISIBLE
                    binding?.txtNoDataHistory?.visibility = View.GONE

                    //binding?.rvListHistory?.layoutManager =LinearLayoutManager(this@HistoryActivity)

                    val dates = ArrayList<String>()
                    for (date in allCompletedDatesList){
                        dates.add(date.date)
                        Log.e("date: ",""+date.date)
                    }
                    val historyAdapter = HistoryAdapter(ArrayList(dates))

                    binding?.rvListHistory?.adapter = historyAdapter
                }else{
                    binding?.tvHistory?.visibility = View.GONE
                    binding?.rvListHistory?.visibility = View.GONE
                    binding?.txtNoDataHistory?.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}