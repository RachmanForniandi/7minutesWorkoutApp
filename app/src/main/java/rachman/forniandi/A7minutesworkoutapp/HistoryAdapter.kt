package rachman.forniandi.A7minutesworkoutapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import rachman.forniandi.A7minutesworkoutapp.databinding.ItemExerciseStatusBinding
import rachman.forniandi.A7minutesworkoutapp.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val items:ArrayList<String>):RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {

    class HistoryHolder (binding:ItemHistoryRowBinding):RecyclerView.ViewHolder(binding.root){
        val llHistoryItemMain = binding.llHistoryItemMain
        val tvItem = binding.tvItem
        val tvPosition = binding.tvPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        return HistoryHolder(
            ItemHistoryRowBinding.inflate(
            LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        val date:String = items.get(position)
        holder.tvPosition.text = (position+1).toString()
        holder.tvItem.text = date

        if (position %2 ==0){
            holder.llHistoryItemMain.setBackgroundColor(
                Color.parseColor("#EBEBEB")
            )
        }else{
            holder.llHistoryItemMain.setBackgroundColor(
                Color.parseColor("#FFFFFF")
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}