package com.example.pigmanoyunkotlin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.pigmanoyunkotlin.databinding.ActivityOyunBinding
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.math.floor

class OyunActivity : AppCompatActivity() {

    private lateinit var tasarim: ActivityOyunBinding

    //ana karaktere aşağı-yukarı(x-y ekseninde) hareket ettirmek için başlangıç koordinatlarını belirten
    // global değişkenler oluşturulur-pozisyonlar konumlar
    private var anakarakterX = 0.0f
    private var anakarakterY = 0.0f
    private var siyahKareX = 0.0f
    private var siyahKareY = 0.0f
    private var sariDaireX = 0.0f
    private var sariDaireY = 0.0f
    private var kirmiziUcgenX = 0.0f
    private var kirmiziUcgenY = 0.0f

    //yukarı ve aşağı hareketini kontrol etmek için kontrolyapısı inindokunma kontrollerinin değişkenlerini
    //oluşturuyoruz true-false olarak.Bu değişken sayasinde anakarakterin aşayamı yoksa yukarımı hareket edeceğini
    //belirliycez örnek true iken yukarı false iken aşaya hareket edecek-kontroller
    private var dokunmaKontrol = false

    //timer değişkenini oluşturuyoruz
    private val timer = Timer()

    //anakarakterin aşağı yukarı hareketidekihız dengesini kontrol edebilmek için başlangıç
    // kontrol değişkeni oluşturuyoruz
    private var baslangıcKontrol = false

    //anakarakterin aşaya-yukarı hareketlerinde ekran dışına taşmaması için kodlamamızda ekran boyutunu
    //belirlememiz gerekir onun için ekran boyutunu alacağımız değişkenleri oluşturuyoruz-boyutlar
    private var ekranGenisligi = 0
    private var ekranYuksekligi = 0
    private var anakarakterGenisligi = 0
    private var anakarakterYuksekligi = 0

    //skor değişkeni
    private var skor=0


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        tasarim = DataBindingUtil.setContentView(this,R.layout.activity_oyun)

        tasarim.oyunActivityNesnesi=this@OyunActivity

        tasarim.skorl=skor.toString()

        //oyun başlayınca kare-daire ve üçgenin oyun ilk başladığında ekranda gözükmemesi için
        //onları ekran dışına konumlandırıyoruz

        tasarim.siyahKare.x=-800.0f
        tasarim.siyahKare.y=-800.0f
        tasarim.sariDaire.x=-800.0f
        tasarim.sariDaire.y=-800.0f
        tasarim.kirmiziUcgen.x=-800.0f
        tasarim.kirmiziUcgen.y=-800.0f







    }


    //oyun oynarken oyun oynama ekranına dokunacağımız için ekrana dokunduğumuzun
    //algılası,anlaşılması gerekir
    //ekrana dokunma özelliğini algılamak için setOnTouchListener metodunu kullanıcaz bu metodu
    //constraint Layout ile birlikte kullanıcaz onun için ConstraintLayout'a İD verdik

    @SuppressLint("ClickableViewAccessibility")
    fun layoutDokunmaAlgila(view:View){

       view.setOnTouchListener(object : View.OnTouchListener {

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                //1.)Ekranla ilgili bilgileri almak istiyorsak burda onTouch metodu içinde işlem yapıyoruz.buranın dışında
                //kodlama yaparsak ekran hakkında bilgi alamayız. Örnek olarak ekranın yükseklik ve genişliğini alalım
                // bize ekranın yüksekliğini verir = tasarim.constraintL.height
                Log.e("YUKSEKLIK ", "Yükseklik =${tasarim.constraintL.height}")

                //bize ekranın genişliğini verir = tasarim.constraintL.width
                Log.e("GENISLIK", "Genişlik =${tasarim.constraintL.width}")


                //3.) anakarakterin aşagı yukarı hız dengesini kontrol altına alıyoruz if blokları içine almazisek
                //ekrana her dokunuşta Timer çalışacak her Timer çalışmasında period değerinde tekrarlıycak ve aşağı yukarı
                // hareket hızlanacak kontrol dışına çıkacak çünkü Timerın bir kere çalışması işimizi görür her ekrana dokunmada
                //tekrar tekrar çalışmasına gerek yok Timer'ı bir kere çalıştırmak için if bloklarını oluşturup baslangıcKontrol
                //değişkenini kullanacaz
                // baslangıcKontrol değişkenini false olarak başlattığımız için önce else bloğu çalışacak ilk iş olarak
                //orda baslangıcKontrol'u true yapıcaz Timer'ı bir kere çalıştırıcaz ve daha sonra else içinde
                //dokunmakontrolu true yapıtığımız için else bloğuna girmeden hep if bloğundan çalışacak if bloğu içinde
                //action_down ve up sorguları çalışacak ekrana dokunulup bırakıldığı bloklar onların içindede dokunmaKontrol
                //değişkeninin değerini değiştiriyoruz Timer Çalıştıştığı için içindeki aşağı veya yukarı tanımlı kodlar çalışıyor

                if (baslangıcKontrol) {
                    //2.) 2 tane if bloğu tanımlıycaz ACTION_DOWN ile tanımlı olanda İF Bloğunda ekrana dokunca yapılacak
                    //işler kodlanacak ACTION_UP ile tanımlı olan İF Bloğunda ekrana dokunmayı bırakınca yapılacak
                    //işler kodlanacak


                    //event? ile MotionEvent sınıfındaki ACTİON_DOWN kullanırız ACTION_DOWN Ekrana dokulduğumuzu
                    // tespit edebildiğimiz actionumuzdur
                    if (event?.action == MotionEvent.ACTION_DOWN) {

                        Log.e("MotionEvent", "ACTION_DOWN = Ekrana Dokundu")

                        dokunmaKontrol = true

                    }

                    //event? ile MotionEvent sınıfındaki ACTİON_UP kullanırız ACTION_UP Ekrana dokunmayı bıraktığımızı
                    // tespit edebildiğimiz actionumuzdur
                    if (event?.action == MotionEvent.ACTION_UP) {

                        Log.e("MotionEvent", "ACTION_UP = Ekranı Bıraktı")
                        dokunmaKontrol = false
                    }


                } else {
                    //oyun başlayınca ilk burası çalışacak

                    //başlangıç kontrol değişkenini uygulama çalıştığında else'se girip Timer'ı çalıştırsın diye
                    // false olarak başlatmıştık ilk iş else girince true'ya çeviriyoruz
                    baslangıcKontrol = true

                    //oyun oynama ekranında oyuna başlatextview var oyun başlayınca gözükmeye devam ediyor oyun başlayıca
                    // gözükmemesi için görünmez yapıyoruz
                    tasarim.textViewOyunBasla.visibility=View.INVISIBLE



                    //1.Adım= anakarakterimizin tasarım üzerindeki konumunu alırız oyun başladığı andaki anakarekterin
                    //konumunu almış oluyoruz

                    //anakarakterin x konumunu alıyoruz
                    anakarakterX = tasarim.anaKarakter.x

                    //anakarakterin y konumumu alıyoruz
                    anakarakterY = tasarim.anaKarakter.y

                    //anakarakterin genişliğini alırız
                    anakarakterGenisligi = tasarim.anaKarakter.width

                    //anakarakterin yüksekliğini alırız
                    anakarakterYuksekligi = tasarim.anaKarakter.height

                    //ekran genişliğini öğreniriz alırız
                    ekranGenisligi = tasarim.constraintL.width

                    //ektan yüksekliğini alırız
                    ekranYuksekligi = tasarim.constraintL.height

                    //2.adım = Timer oluşturulur
                    //scheduler'ın TimerTaSK döndüren metodunu seçiyoruz yukarıya import kotlin.concurrent.schedule bunu
                    // import edecek
                    //timer.schedule(delay-gecikme süresi(başladığında ne kadar sonra çalışsın),period-ne kadar süreyle tekrarlasın)
                    timer.schedule(0, 20) {

                        //Handler oluşturulur Handler Android sayfası üzerindeki görsel nesneleri üzerinde değişiklik yapmamaızı sağlıyor
                        //sadece Timer kullanırsak arayüzdeki görseller üzerinde değişiklik yapmak zorlaşıyor
                        //handler(android.os) olan

                        Handler(Looper.getMainLooper()).post {

                            //anakarakter hareketleriyle alakalı matodumuzu çağırıyoruz
                            anakarakterHareketEttirme()
                            cisimleriHareketEttirme()
                            carpismaKontrolu()


                        }


                    }


                }



                return true
            }


        })


    }

   // anakarakter hareketlerini ve aşayayukarı hareket edip ekran sınırlarında kalmasını sağlayan metodu oluşturuyoruz
    fun anakarakterHareketEttirme(){

        //Timer ile çalıştırmak istediğimiz kodları yazıyoruz
        //if bloğunda global dokunmaKontrol değişkenini kontrol ediyoruz true-false durumuna göre
        //anakarakteri aşağı-yukarı hareket ettiricez
       //anakarakterY = anakarakterY - 20.0f
       //y azalırsa yukarı çıkar AYNIzamanda hareket hızını belirler böyel sabit değerler verirsek
       //telefonların boyutları farlı olduğu için daha geniş ekranda daha yavaş veya hızlı çalışabilir
       //bunu ekrana göre dinamikleştirmek için

       val anaKarakterHizi=ekranYuksekligi/60.0f


        if (dokunmaKontrol) {

            //yukarı çakar
            anakarakterY=anakarakterY-anaKarakterHizi



        } else {

            //y artarsa aşaya iner aynı zamanda hareket hızını belirler
           // anakarakterY = anakarakterY + 20f böyle sabit değer verirsek farlı boyutlardaki telefonlarda
            //hız farkı olur

            anakarakterY=anakarakterY+anaKarakterHizi
        }

        //anakarakterin ekranın üstünden daha yukarı çıkıp ekrandan kaybolamaması için 0.0f konumunda
        // durmasını sağlıyoruz anakarakter yüksekliği 0.0f den küçükse veya eşitse yüksekliğini
        //0.0f konumuna eşitleyip 0.0f konumunda durması sağlanır
        if (anakarakterY <= 0.0f) {
            anakarakterY = 0.0f
        }

        //şimdide anakarakterin ekranın altında kalmasını daha aşaya gidip ekrandan kaybolamamsını sağlıycaz,
        //bunu  ekranın yüksekliğinden ,anakarakterin yüksekliğini çıkarıp en fazla ne kadar aşaya inmesi
        // gerektiği miktarı bulucaz

        if (anakarakterY >= ekranYuksekligi - anakarakterYuksekligi) {

            anakarakterY = (ekranYuksekligi - anakarakterYuksekligi).toFloat()
        }


        tasarim.anaKarakter.y = anakarakterY


    }

    //Bu metot ile siyaKare-sarıDaire - kirmiziUcgenin sağ taraftan rastgele bi dikey konumdan başlayıp sol tarafa doğru
    //hareket etmesi ve sol tarafa ekranın sonuna gelince tekrar rastgele sağ taraftan dikey bir konuma konumlanıp
    //sola doğru hareket etmesi sağlanacak sağlanacak bu metodu Timer içinde çağırıp otomatik olarak tekrar tekrar
    //çalışması sağlanacak böylece bir döngüyede ihtiyaç olmayacak

    fun cisimleriHareketEttirme(){
        //siyahkareyi 0.0f başlangıç konumunda başlatık oyun başladığında başlangıç konumundan
        // sola kayıp ekrandan çıkması içn ve oyun içinde siyahkarenin çalışması sağdan sola doğru gelip
        //ekrandan kaybolması şeklinde olucak ekrandan kaybolmayıda 0.0f konumuna gelince -25 piksel daha sola kayıp
        //ekrandan çıkacak böylece siyah kere oyun başladığında sağdan -sola gelip ekrandan kaybolacak ve kaybolunca
        //bizde siyah kareyi tekrar sağ başa konumlandırıp tekrar çalışmasını sağlıycaz böylece Y (ekranboyu) ekseninde
        // rastgele bir yerden başlatıp  X(ekran eni) ekseni boyunca sağdan sola kaydırıcaz hareker ettiricez.

      //  siyahKareX=siyahKareX-25.0f //bu ifade siyahkarenin sola doğru gitme hızını belirler bu değeri değiştirerek
        // hızınıda arttırıp azaltabiliriz -eksi olması sol tarafa  +artı olması sağ tarafa hareket ettirir
      //  sariDaireX=sariDaireX-20.0f //biraz yavaş hareket etsin bu 20 puan
      //  kirmiziUcgenX=kirmiziUcgenX-30.0f //bu biraz hızlı hareket etsin buma 50 puan veriyoz

        //kare-daire ve ücgeninde hızlarını hareket miktarlarını dinamikleştiriyoruz

        siyahKareX=siyahKareX-ekranGenisligi/44.0f
        sariDaireX=sariDaireX-ekranGenisligi/54.0f
        kirmiziUcgenX=kirmiziUcgenX-ekranGenisligi/36.0f




        //eğer siyahkare en solda ise ekranın sonunda ise baştan başlaması gerekir
        if (siyahKareX<0.0f){

            //if ile eğer siyahkarenin x konumu 0.0f küçükse yani ekrandan kaybolmuşsa
            //yukarda ekran geniişliğini alımıştık ekranın sağ tarafındaki konumunun dışına tekrar konumlandırıyoruz
            //oyunda siyahkarenin hareketi    soltaraf  <--------- sağ taraf  sağdan sola hareket edirek gidiyodu
            //sol tarafta ekran dışına çıkınca tekrar baştan sağ tarafa ekran dışına konumlandırıyoruz
            siyahKareX=ekranGenisligi+20.0f

            //siyahkarenin dikey konumunu rastgele bir konumda belirliycez
            siyahKareY= floor(Math.random() * ekranYuksekligi).toFloat()
        }
        //yukardaki gibi sarıDaire ve kirmiziUcgeninde konumlarını belirleriz
        if (sariDaireX<0.0f){

            sariDaireX=ekranGenisligi+20.0f

            //sariDairenin dikey konumunu rastgele bir konumda belirliycez
            sariDaireY= floor(Math.random() * ekranYuksekligi).toFloat()
        }
        if (kirmiziUcgenX<0.0f){

            kirmiziUcgenX=ekranGenisligi+20.0f

            //kirmiziÜçgenin dikey konumunu rastgele bir konumda belirliycez
            kirmiziUcgenY= floor(Math.random() * ekranYuksekligi).toFloat()
        }

        //siyahkare-sariDaire-kirmiziUcgen'in anlık X-Y konumları belirlenir

        tasarim.siyahKare.x=siyahKareX
        tasarim.siyahKare.y=siyahKareY
        tasarim.sariDaire.x=sariDaireX
        tasarim.sariDaire.y=sariDaireY
        tasarim.kirmiziUcgen.x=kirmiziUcgenX
        tasarim.kirmiziUcgen.y=kirmiziUcgenY

    }

    //Çarpışma Kontrolü için Metot Oluşturucaz
    //çarpışma kontrolünü kare-daire ve üçgenin merkez noktalarını bularak bulma işlemini cisimlerin
    //boyutlarının x-y koordinatlarından hesaplıycaz cisimlerim merkezi noktası ana karakterin içindeki
    //bölgedeyse çarpışma gerçekleşmiş sayılacak

    fun carpismaKontrolu(){

        //cisimlerin merkez x-y konumlarını bulucaz cisimlerin başlangıç noktalarını genişlik ve yüksekliklerinin
        //yarısıyla topluyoruz genişliğini 2 ye bölüp başlangıç noktasıyla topluyoruz yükseklik içinde aynısını yapıyoruz
        //ama ekranda pixel olduğu için ve işlemleri float ile yaptığımız için 2.0f yani floata bölüyoruz
        val sariDaireMerkezX=sariDaireX+tasarim.sariDaire.width/2.0f
        val sariDaireMerkezY=sariDaireY+tasarim.sariDaire.height/2.0f
        val siyahKareMerkezX=siyahKareX+tasarim.siyahKare.width/2.0f
        val siyahKareMerkezY=siyahKareY+tasarim.siyahKare.height/2.0f
        val kirmiziUcgenMerkezX=kirmiziUcgenX+tasarim.kirmiziUcgen.width/2.0f
        val kirmiziUcgenMerkezY=kirmiziUcgenY+tasarim.kirmiziUcgen.height/2.0f


        if (sariDaireMerkezX>=0.0f && sariDaireMerkezX<=anakarakterGenisligi && sariDaireMerkezY >=anakarakterY
            && sariDaireMerkezY <= anakarakterY+anakarakterYuksekligi){
            //yukardaki koşul sari dairenin merkezi 0.0f konumundan büyükse ve anakarakterin genişliğinden küçükse ve
            //sari dairenin yüksekliğide anakarakrterin yüksekliğinin başlangıç noktasına eşitse veya büyükse
            // (y ekseninde aşaya doğru değer artar) ve ana karakterin yükseklik değerinden küçükse bu daire
            //anakarakterin alanı içerisine girmiştir ve çarpıma olmuş sayırlı demek

            //skoru yirmi arttırıyorur
            skor+=20
            tasarim.skorl=skor.toString()


           //ve çarpışma olduğu için hemen daireyi ekranın dışına taşıyoruz
            sariDaireX =-800.0f


        }

        if (kirmiziUcgenMerkezX>=0.0f && kirmiziUcgenMerkezX<=anakarakterGenisligi && kirmiziUcgenMerkezY >=anakarakterY
            && kirmiziUcgenMerkezY <= anakarakterY+anakarakterYuksekligi){
            //yukardaki koşul ücgen merkezi 0.0f konumundan büyükse ve anakarakterin genişliğinden küçükse ve
            //üçgenin yüksekliğide anakarakrterin yüksekliğinin başlangıç noktasına eşitse veya büyükse
            // (y ekseninde aşaya doğru değer artar) ve ana karakterin yükseklik değerinden küçükse bu üçgen
            //anakarakterin alanı içerisine girmiştir ve çarpıma olmuş sayırlı demek

            //skoru elli arttırıyorur
            skor+=50
            tasarim.skorl=skor.toString()


            //ve çarpışma olduğu için hemen daireyi ekranın dışına taşıyoruz
            sariDaireX =-800.0f


        }

        if (siyahKareMerkezX>=0.0f && siyahKareMerkezX<=anakarakterGenisligi && siyahKareMerkezY >=anakarakterY
            && siyahKareMerkezY <= anakarakterY+anakarakterYuksekligi){
            //yukardaki koşul kare merkezi 0.0f konumundan büyükse ve anakarakterin genişliğinden küçükse ve
            //karenin yüksekliğide anakarakrterin yüksekliğinin başlangıç noktasına eşitse veya büyükse
            // (y ekseninde aşaya doğru değer artar) ve ana karakterin yükseklik değerinden küçükse bu kare
            //anakarakterin alanı içerisine girmiştir ve çarpıma olmuş sayırlı demek

            siyahKareX=-800.0f

            //timer durdurulur
            timer.cancel()
          var intent=Intent(this@OyunActivity,SonucActivity::class.java)

            intent.putExtra("skor",skor)
            startActivity(intent)
            finish()


        }


        tasarim.skorl=skor.toString()

    }



}