package fr.isen.mollinari.androidktntoolbox.activity

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import fr.isen.mollinari.androidktntoolbox.R
import fr.isen.mollinari.androidktntoolbox.RoundedTransformation
import fr.isen.mollinari.androidktntoolbox.model.User

class UsersAdapter(private val usersList: List<User>, private val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val randomUser = usersList[position]

        val name =
            capitalize(randomUser.name.first) + " " + randomUser.name.last.toUpperCase()
        val location =
            randomUser.location.street + " " + randomUser.location.state + " " + randomUser.location.city
        Picasso.with(context)
            .load(randomUser.picture.medium)
            .fit().centerInside()
            .transform(RoundedTransformation(400, 0))
            .into(holder.pic)

        holder.name.text = name
        holder.location.text = location
        holder.mail.text = randomUser.email
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_web_service, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    private fun capitalize(line: String): String {
        return Character.toUpperCase(line[0]) + line.substring(1)
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val name = view.findViewById<TextView>(R.id.userName)
    val location = view.findViewById<TextView>(R.id.userLocation)
    val mail = view.findViewById<TextView>(R.id.userMail)
    val pic = view.findViewById<ImageView>(R.id.userPicture)
}