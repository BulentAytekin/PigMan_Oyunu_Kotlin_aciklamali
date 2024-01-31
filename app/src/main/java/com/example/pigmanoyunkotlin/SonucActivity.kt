package com.example.pigmanoyunkotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import com.example.pigmanoyunkotlin.databinding.ActivitySonucBinding

class SonucActivity : AppCompatActivity() {

    private lateinit var tasarim:ActivitySonucBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tasarim=DataBindingUtil.setContentView(this,R.layout.activity_sonuc)

        tasarim.sonucActivityNesnesi=this@SonucActivity

        skorKaydet()



    }
    fun tekrarDene(view: View){


        val intent=Intent(this@SonucActivity,MainActivity::class.java)

        startActivity(intent)
    }

    fun skorKaydet(){

        val alinanSkor=intent.getIntExtra("skor",0)
        tasarim.toplamSkor=alinanSkor.toString()

        //En yüksek skoru kaydetmemiz için SharedPreferences sınıfından bir nesne oluşturuyoruz
        //bu nesne veiyi kaydedeceğimiz dosyayı oluşturup üzerinde işlem yapmamamızı sağlayacak
        //ve modunuda Context.MODE_PRIVATE olarakayarlıycaz uygulama içinde her yerden erişilir uygulama dışından erişilmez

        val sp=getSharedPreferences("sonuc", Context.MODE_PRIVATE)

        //hemen ilk olarak sonuc dosyası içindeki yüksekskor verisini alıcazki gelen skorla kıyaslama yapıp
        //buyuk olan skoru kaydetmek için uygulama ilk çalıştığında bir skor kayıtlı olmadığı için
        //default değeri olan 0 baz alıcak

        val enYuksekSkor=sp.getInt("kaydedilmisSkor",0)


        if (alinanSkor>enYuksekSkor){

            tasarim.yuksekSkor=alinanSkor.toString()

            //veriyi kaydetmek için sp'yi editliycez
            val editor=sp.edit()

            //editor değişkeni yardımıyla veriyi dosyaya ekliyoruz
            //editor.putInt("kaydedilecek dosya içindeki yer,başlık",kaydedilecek Değer)
            editor.putInt("kaydedilmisSkor",alinanSkor)

            //kaydın dosyaya yazılması için başlatılması commit() ile olur yazmayı başlat diyoruz
            editor.commit()

        }else{

            //eğer gelen skor enYüksekSkordan düşükse burası çalışacak edittext'e dosyadan okuduğumuz enyüksekSkoru
            //yazdırıcaz
            tasarim.yuksekSkor=enYuksekSkor.toString()

        }


    }
}