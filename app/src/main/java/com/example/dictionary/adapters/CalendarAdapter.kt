package com.example.dictionary.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.objects.Day
import com.example.dictionary.R
import com.example.dictionary.events.DoubleClickListener
import com.example.dictionary.utils.AppPreferences

class CalendarAdapter(var context: Context?, var dayOfMonth: ArrayList<Day>) :
    RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {
    var onClick: ((Day) -> Unit)? = null
    var index = -1
    var color: Int = Color.YELLOW

    init {
        AppPreferences.init(context)
    }

    fun setOnItemClick(callBack: (Day) -> Unit) {
        onClick = callBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.day_item, parent, false)
        return DayViewHolder(view)
    }


    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        var day = dayOfMonth[position]
        holder.tvDay.text = day.date.dayOfMonth.toString()
        if (day.checked) {
            index = position
            holder.bgrChecked.setBackgroundColor(color)
        } else {
            holder.bgrChecked.setBackgroundColor(0)
        }

        if (!day.isInMonth) {
            holder.tvDay.setTextColor(ContextCompat.getColor(context!!, R.color.grey))
            holder.bgrChecked.isEnabled = false
        } else {
            holder.tvDay.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        }


        holder.itemView.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick() {
                onClick?.invoke(day)
            }

            override fun onSingleClick() {
                if (index == -1) index = position
                dayOfMonth[index].checked = false
                notifyItemChanged(index)
                dayOfMonth[position].checked = true
                index = position
                color = Color.YELLOW
                AppPreferences.checkedDay = day.date.dayOfMonth
                AppPreferences.checkedMonth = day.date.month.value
                notifyItemChanged(position)
            }

        })

    }

    override fun getItemCount(): Int {
        return dayOfMonth.size
    }

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDay: TextView = itemView.findViewById(R.id.tv_day_item)
        var bgrChecked: RelativeLayout = itemView.findViewById(R.id.bgr_day_checked)
    }
}