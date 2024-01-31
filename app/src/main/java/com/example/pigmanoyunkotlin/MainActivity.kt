package com.example.pigmanoyunkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.pigmanoyunkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var tasarim:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tasarim=DataBindingUtil.setContentView(this,R.layout.activity_main,)

        tasarim.mainActivityNesnesi=this@MainActivity


    }

    fun oyunBasla(){

        val intent=Intent(this@MainActivity,OyunActivity::class.java)
        startActivity(intent)


    }
}