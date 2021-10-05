package com.example.gonews

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {


    private lateinit var mAdapter: NewsListAdapter

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout:DrawerLayout=findViewById(R.id.drawerLayout)
        val navView:NavigationView=findViewById(R.id.nav_view)

        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_sports ->{
                    val intent=Intent(this,Activity_Sports::class.java)
                    startActivity(intent)
                }
                R.id.nav_entertainment ->{
                    val intent=Intent(this,Activity_Entertainment::class.java)
                    startActivity(intent)
                }
                R.id.nav_technology ->{
                    val intent=Intent(this,Activity_Technology::class.java)
                    startActivity(intent)
                }
                R.id.nav_science ->{
                    val intent=Intent(this,Activity_Science::class.java)
                    startActivity(intent)
                }
                R.id.nav_international ->{
                    val intent=Intent(this,Activity_International::class.java)
                    startActivity(intent)
                }
                R.id.nav_health ->{
                    val intent=Intent(this,Activity_Health::class.java)
                    startActivity(intent)
                }


            }
            true
        }

        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
        mAdapter=NewsListAdapter(this)
        recyclerView.adapter=mAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun fetchData(){
        val url="https://saurav.tech/NewsAPI/top-headlines/category/general/in.json"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            {
                val newsJsonArray=it.getJSONArray("articles")
                val newsArray=ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject =newsJsonArray.getJSONObject(i)
                    val news=News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage"),
                    )
                    newsArray.add(news)
                }


                mAdapter.updateNews(newsArray)

            },
            {
                Toast.makeText(this,"error",Toast.LENGTH_LONG).show()

            }
        )


        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }



    override fun onItemClicked(item: News) {


        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))


    }
}