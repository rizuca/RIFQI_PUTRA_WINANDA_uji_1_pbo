import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Interface untuk pengingat audio (adzan)
// Interface mendefinisikan kontrak untuk fitur audio reminder
interface AudioReminder {
    void playAdzan();
    void setVolume(int volume);
    void stopAdzan();
}

// Abstract class Prayer sebagai parent class untuk semua sholat
// Abstract class tidak dapat diinstansiasi langsung
abstract class Prayer {
    // Protected agar bisa diakses oleh subclass
    protected String name;           // Nama sholat
    protected LocalTime time;        // Waktu sholat
    protected int rakaat;           // Jumlah rakaat
    protected boolean isCompleted;  // Status sudah sholat atau belum
    protected String arabicName;    // Nama dalam bahasa Arab
    
    // Constructor untuk inisialisasi properties dasar
    public Prayer(String name, String arabicName, LocalTime time, int rakaat) {
        this.name = name;
        this.arabicName = arabicName;
        this.time = time;
        this.rakaat = rakaat;
        this.isCompleted = false;
    }
    
    // Method abstract yang harus diimplementasikan oleh setiap subclass
    // Setiap sholat memiliki cara pengingat yang berbeda
    public abstract void remind();
    
    // Method abstract untuk mendapatkan doa setelah sholat
    public abstract String getAfterPrayerDua();
    
    // Method konkret yang bisa digunakan langsung oleh semua subclass
    public void displayInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        System.out.println("=== " + name + " (" + arabicName + ") ===");
        System.out.println("Waktu: " + time.format(formatter));
        System.out.println("Rakaat: " + rakaat);
        System.out.println("Status: " + (isCompleted ? "✅ Sudah Sholat" : "⏰ Belum Sholat"));
    }
    
    // Method untuk menandai sholat sudah dikerjakan
    public void markCompleted() {
        this.isCompleted = true;
        System.out.println("✅ " + name + " telah dikerjakan. Barakallahu fiik!");
    }
    
    // Method untuk reset status (untuk hari berikutnya)
    public void resetStatus() {
        this.isCompleted = false;
    }
    
    // Method untuk mengecek apakah sudah waktunya sholat
    public boolean isTimeTopray() {
        LocalTime now = LocalTime.now();
        return now.isAfter(time) && !isCompleted;
    }
    
    // Getter methods
    public String getName() { return name; }
    public String getArabicName() { return arabicName; }
    public LocalTime getTime() { return time; }
    public int getRakaat() { return rakaat; }
    public boolean isCompleted() { return isCompleted; }
    
    // Setter untuk waktu sholat
    public void setTime(LocalTime time) {
        this.time = time;
    }
}

// Class Fajr (Subuh) - mewarisi Prayer dan mengimplementasikan AudioReminder
class Fajr extends Prayer implements AudioReminder {
    private int volume;
    private boolean adzanEnabled;
    
    public Fajr(LocalTime time) {
        super("Subuh", "الفجر", time, 2);
        this.volume = 80;
        this.adzanEnabled = true;
    }
    
    // Override method abstract dari parent class
    @Override
    public void remind() {
        System.out.println("\n🌅 WAKTU SHOLAT SUBUH TELAH TIBA! 🌅");
        System.out.println("\"Dan dirikanlah sholat pada kedua tepi siang\" (QS. Hud: 114)");
        
        if (adzanEnabled) {
            playAdzan();
        }
        
        System.out.println("📿 Sunnah sebelum Subuh: 2 rakaat");
        System.out.println("💡 Tips: Subuh adalah waktu yang penuh berkah untuk memulai hari");
    }
    
    @Override
    public String getAfterPrayerDua() {
        return "اللَّهُمَّ أَعِنِّي عَلَى ذِكْرِكَ وَشُكْرِكَ وَحُسْنِ عِبَادَتِكَ\n" +
               "(Allahumma a'inni 'ala dzikrika wa syukrika wa husni 'ibadatika)\n" +
               "Ya Allah, tolonglah aku untuk berdzikir kepada-Mu, bersyukur kepada-Mu, dan beribadah dengan baik kepada-Mu";
    }
    
    // Implementasi interface AudioReminder
    @Override
    public void playAdzan() {
        System.out.println("🔊 Memutar Adzan Subuh...");
        System.out.println("♪ Allahu Akbar, Allahu Akbar... ♪");
        System.out.println("Volume: " + volume + "%");
    }
    
    @Override
    public void setVolume(int volume) {
        this.volume = Math.max(0, Math.min(100, volume));
        System.out.println("🔊 Volume adzan diatur ke: " + this.volume + "%");
    }
    
    @Override
    public void stopAdzan() {
        System.out.println("⏹️ Adzan Subuh dihentikan");
    }
    
    public void setAdzanEnabled(boolean enabled) {
        this.adzanEnabled = enabled;
    }
}

// Class Dhuhr (Dzuhur) - mewarisi Prayer dan mengimplementasikan AudioReminder
class Dhuhr extends Prayer implements AudioReminder {
    private int volume;
    private boolean adzanEnabled;
    
    public Dhuhr(LocalTime time) {
        super("Dzuhur", "الظهر", time, 4);
        this.volume = 75;
        this.adzanEnabled = true;
    }
    
    @Override
    public void remind() {
        System.out.println("\n☀️ WAKTU SHOLAT DZUHUR TELAH TIBA! ☀️");
        System.out.println("\"Dan dirikanlah sholat, tunaikanlah zakat\" (QS. Al-Baqarah: 43)");
        
        if (adzanEnabled) {
            playAdzan();
        }
        
        System.out.println("📿 Sunnah sebelum Dzuhur: 4 rakaat, setelah: 2 rakaat");
        System.out.println("💡 Tips: Waktu istirahat yang baik untuk mendekatkan diri kepada Allah");
    }
    
    @Override
    public String getAfterPrayerDua() {
        return "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ\n" +
               "(Rabbana atina fi'd-dunya hasanatan wa fi'l-akhirati hasanatan wa qina 'adzab an-nar)\n" +
               "Ya Tuhan kami, berilah kami kebaikan di dunia dan kebaikan di akhirat, dan peliharalah kami dari siksa neraka";
    }
    
    @Override
    public void playAdzan() {
        System.out.println("🔊 Memutar Adzan Dzuhur...");
        System.out.println("♪ Hayya 'ala's-Shalah, Hayya 'ala'l-Falah... ♪");
        System.out.println("Volume: " + volume + "%");
    }
    
    @Override
    public void setVolume(int volume) {
        this.volume = Math.max(0, Math.min(100, volume));
        System.out.println("🔊 Volume adzan diatur ke: " + this.volume + "%");
    }
    
    @Override
    public void stopAdzan() {
        System.out.println("⏹️ Adzan Dzuhur dihentikan");
    }
}

// Class Asr - mewarisi Prayer dan mengimplementasikan AudioReminder
class Asr extends Prayer implements AudioReminder {
    private int volume;
    private boolean adzanEnabled;
    
    public Asr(LocalTime time) {
        super("Ashar", "العصر", time, 4);
        this.volume = 70;
        this.adzanEnabled = true;
    }
    
    @Override
    public void remind() {
        System.out.println("\n🌤️ WAKTU SHOLAT ASHAR TELAH TIBA! 🌤️");
        System.out.println("\"Peliharalah semua sholat dan sholat wustha\" (QS. Al-Baqarah: 238)");
        
        if (adzanEnabled) {
            playAdzan();
        }
        
        System.out.println("📿 Sholat Ashar adalah sholat wustha (tengah)");
        System.out.println("💡 Tips: Waktu yang penuh berkah di sore hari");
    }
    
    @Override
    public String getAfterPrayerDua() {
        return "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ الَّذِي لَا إِلَهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ وَأَتُوبُ إِلَيْهِ\n" +
               "(Astaghfiru'llaha'l-'azhim alladzi la ilaha illa huwa'l-hayyu'l-qayyumu wa atubu ilayh)\n" +
               "Aku memohon ampun kepada Allah Yang Maha Agung, yang tiada Tuhan selain Dia, Yang Maha Hidup, Yang Maha Berdiri Sendiri, dan aku bertaubat kepada-Nya";
    }
    
    @Override
    public void playAdzan() {
        System.out.println("🔊 Memutar Adzan Ashar...");
        System.out.println("♪ La ilaha illa Allah... ♪");
        System.out.println("Volume: " + volume + "%");
    }
    
    @Override
    public void setVolume(int volume) {
        this.volume = Math.max(0, Math.min(100, volume));
        System.out.println("🔊 Volume adzan diatur ke: " + this.volume + "%");
    }
    
    @Override
    public void stopAdzan() {
        System.out.println("⏹️ Adzan Ashar dihentikan");
    }
}

// Class Maghrib - mewarisi Prayer dan mengimplementasikan AudioReminder
class Maghrib extends Prayer implements AudioReminder {
    private int volume;
    private boolean adzanEnabled;
    
    public Maghrib(LocalTime time) {
        super("Maghrib", "المغرب", time, 3);
        this.volume = 85;
        this.adzanEnabled = true;
    }
    
    @Override
    public void remind() {
        System.out.println("\n🌅 WAKTU SHOLAT MAGHRIB TELAH TIBA! 🌅");
        System.out.println("\"Dan bertasbihlah kepada-Nya di waktu matahari terbenam\" (QS. Qaf: 39)");
        
        if (adzanEnabled) {
            playAdzan();
        }
        
        System.out.println("📿 Sunnah setelah Maghrib: 2 rakaat");
        System.out.println("💡 Tips: Waktu mustajab untuk berdoa saat berbuka puasa");
        System.out.println("🥛 Sunnah berbuka dengan kurma dan air putih");
    }
    
    @Override
    public String getAfterPrayerDua() {
        return "اللَّهُمَّ أَعِذْنِي مِنْ النَّارِ\n" +
               "(Allahumma a'idzni min an-nar)\n" +
               "Ya Allah, lindungilah aku dari api neraka\n" +
               "(Dibaca 7 kali setelah sholat Maghrib dan Subuh)";
    }
    
    @Override
    public void playAdzan() {
        System.out.println("🔊 Memutar Adzan Maghrib...");
        System.out.println("♪ Allahu Akbar, Allahu Akbar... ♪");
        System.out.println("Volume: " + volume + "%");
    }
    
    @Override
    public void setVolume(int volume) {
        this.volume = Math.max(0, Math.min(100, volume));
        System.out.println("🔊 Volume adzan diatur ke: " + this.volume + "%");
    }
    
    @Override
    public void stopAdzan() {
        System.out.println("⏹️ Adzan Maghrib dihentikan");
    }
}

// Class Isha - mewarisi Prayer dan mengimplementasikan AudioReminder
class Isha extends Prayer implements AudioReminder {
    private int volume;
    private boolean adzanEnabled;
    
    public Isha(LocalTime time) {
        super("Isya", "العشاء", time, 4);
        this.volume = 75;
        this.adzanEnabled = true;
    }
    
    @Override
    public void remind() {
        System.out.println("\n🌙 WAKTU SHOLAT ISYA TELAH TIBA! 🌙");
        System.out.println("\"Dan di sebagian malam, maka sholatlah tahajjud\" (QS. Al-Isra: 79)");
        
        if (adzanEnabled) {
            playAdzan();
        }
        
        System.out.println("📿 Sunnah setelah Isya: 2 rakaat, Witir: 3 rakaat");
        System.out.println("💡 Tips: Waktu yang baik untuk sholat tahajjud dan istighfar");
        System.out.println("🌟 Disunnahkan membaca Surah Al-Mulk sebelum tidur");
    }
    
    @Override
    public String getAfterPrayerDua() {
        return "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n" +
               "(Allahumma innaka 'afuwwun tuhibbu'l-'afwa fa'fu 'anni)\n" +
               "Ya Allah, sesungguhnya Engkau Maha Pemaaf, Engkau menyukai pemaafan, maka maafkanlah aku";
    }
    
    @Override
    public void playAdzan() {
        System.out.println("🔊 Memutar Adzan Isya...");
        System.out.println("♪ As-Shalatu khayrun min an-nawm... ♪");
        System.out.println("Volume: " + volume + "%");
    }
    
    @Override
    public void setVolume(int volume) {
        this.volume = Math.max(0, Math.min(100, volume));
        System.out.println("🔊 Volume adzan diatur ke: " + this.volume + "%");
    }
    
    @Override
    public void stopAdzan() {
        System.out.println("⏹️ Adzan Isya dihentikan");
    }
}

// Class untuk mengelola jadwal sholat
class PrayerSchedule {
    private List<Prayer> dailyPrayers;
    private String location;
    
    public PrayerSchedule(String location) {
        this.location = location;
        this.dailyPrayers = new ArrayList<>();
        
        // Inisialisasi jadwal sholat default (Jakarta)
        initializeDefaultSchedule();
    }
    
    private void initializeDefaultSchedule() {
        dailyPrayers.add(new Fajr(LocalTime.of(4, 30)));      // Subuh 04:30
        dailyPrayers.add(new Dhuhr(LocalTime.of(12, 5)));     // Dzuhur 12:05
        dailyPrayers.add(new Asr(LocalTime.of(15, 20)));      // Ashar 15:20
        dailyPrayers.add(new Maghrib(LocalTime.of(18, 10)));  // Maghrib 18:10
        dailyPrayers.add(new Isha(LocalTime.of(19, 20)));     // Isya 19:20
    }
    
    public void displayTodaySchedule() {
        System.out.println("📅 JADWAL SHOLAT HARI INI - " + location.toUpperCase());
        
        for (Prayer prayer : dailyPrayers) {
            prayer.displayInfo();
            System.out.println();
        }
    }
    
    public void checkPrayerTimes() {
        System.out.println("⏰ PENGECEKAN WAKTU SHOLAT:");
        boolean foundDue = false;
        
        for (Prayer prayer : dailyPrayers) {
            if (prayer.isTimeTopray()) {
                prayer.remind();
                foundDue = true;
                System.out.println();
            }
        }
        
        if (!foundDue) {
            System.out.println("✅ Tidak ada sholat yang jatuh tempo saat ini");
        }
    }
    
    public void markPrayerCompleted(String prayerName) {
        for (Prayer prayer : dailyPrayers) {
            if (prayer.getName().equalsIgnoreCase(prayerName)) {
                prayer.markCompleted();
                System.out.println("📿 Doa setelah sholat:");
                System.out.println(prayer.getAfterPrayerDua());
                return;
            }
        }
        System.out.println("❌ Sholat " + prayerName + " tidak ditemukan");
    }
    
    public void resetDailyStatus() {
        for (Prayer prayer : dailyPrayers) {
            prayer.resetStatus();
        }
        System.out.println("🔄 Status sholat harian telah direset");
    }
    
    public void adjustVolume(int volume) {
        for (Prayer prayer : dailyPrayers) {
            if (prayer instanceof AudioReminder) {
                ((AudioReminder) prayer).setVolume(volume);
            }
        }
    }
    
    public void showProgress() {
        System.out.println("📈 PROGRESS SHOLAT HARI INI:");
        
        int completed = 0;
        for (Prayer prayer : dailyPrayers) {
            String status = prayer.isCompleted() ? "✅" : "⏰";
            System.out.println(status + " " + prayer.getName() + " (" + 
                             prayer.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) + ")");
            if (prayer.isCompleted()) completed++;
        }
        
        double percentage = (completed * 100.0) / dailyPrayers.size();
        System.out.println("\n📊 Progress: " + completed + "/" + dailyPrayers.size() + 
                          " (" + String.format("%.1f", percentage) + "%)");
    }
    
    // Getter methods
    public List<Prayer> getDailyPrayers() { return dailyPrayers; }
    public String getLocation() { return location; }
}

// Main class untuk menjalankan aplikasi
public class jadwalsholat {
    public static void main(String[] args) {
        System.out.println("🕌 SELAMAT DATANG DI SISTEM JADWAL SHOLAT 🕌");
        System.out.println("بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيم");
        
        // Membuat jadwal sholat untuk Jakarta
        PrayerSchedule schedule = new PrayerSchedule("Medan");
        
        // Menampilkan jadwal hari ini
        System.out.println("\n1️⃣ MENAMPILKAN JADWAL SHOLAT:");
        schedule.displayTodaySchedule();
        
        // Demonstrasi polymorphism - memanggil method remind() untuk setiap sholat
        System.out.println("\n2️⃣ DEMONSTRASI POLYMORPHISM - PENGINGAT SHOLAT:");
        System.out.println("Memanggil method remind() yang memiliki implementasi berbeda:");
        
        for (Prayer prayer : schedule.getDailyPrayers()) {
            prayer.remind();
        }
        
        // Demonstrasi interface AudioReminder
        System.out.println("\n3️⃣ DEMONSTRASI INTERFACE AUDIOREMINDER:");
        
        // Mengatur volume untuk semua sholat
        schedule.adjustVolume(90);
        
        // Mendemonstrasikan fitur audio khusus untuk Subuh
        Prayer fajr = schedule.getDailyPrayers().get(0);
        if (fajr instanceof AudioReminder) {
            System.out.println("\n--- Kontrol Audio Subuh ---");
            AudioReminder fajrAudio = (AudioReminder) fajr;
            fajrAudio.playAdzan();
            fajrAudio.setVolume(50);
            fajrAudio.stopAdzan();
        }
        
        // Simulasi menyelesaikan beberapa sholat
        System.out.println("\n4️⃣ SIMULASI MENYELESAIKAN SHOLAT:");
        
        schedule.markPrayerCompleted("Subuh");
        schedule.markPrayerCompleted("Dzuhur");
        schedule.markPrayerCompleted("Ashar");
        
        // Menampilkan progress
        System.out.println("\n5️⃣ PROGRESS SHOLAT:");
        schedule.showProgress();
        
        // Demonstrasi pengecekan waktu sholat
        System.out.println("\n6️⃣ PENGECEKAN WAKTU SHOLAT:");
        schedule.checkPrayerTimes();
        
        // Menampilkan doa setelah sholat untuk sholat yang belum dikerjakan
        System.out.println("\n7️⃣ DOA SETELAH SHOLAT:");
        for (Prayer prayer : schedule.getDailyPrayers()) {
            if (!prayer.isCompleted()) {
                System.out.println("\n--- Doa setelah " + prayer.getName() + " ---");
                System.out.println(prayer.getAfterPrayerDua());
                System.out.println();
                break; // Tampilkan hanya satu contoh
            }
        }
        
        // Summary konsep OOP yang digunakan
        System.out.println("\n🎯 KONSEP OOP YANG TELAH DIDEMONSTRASIKAN:");
        System.out.println("✅ Abstract Class: Prayer dengan method abstract remind() dan getAfterPrayerDua()");
        System.out.println("✅ Inheritance: Fajr, Dhuhr, Asr, Maghrib, Isha extends Prayer");
        System.out.println("✅ Interface: AudioReminder dengan method playAdzan(), setVolume(), stopAdzan()");
        System.out.println("✅ Polymorphism: Method remind() memiliki implementasi berbeda di setiap subclass");
        System.out.println("✅ Encapsulation: Private fields dengan public getter/setter methods");
        System.out.println("✅ Method Overriding: Setiap sholat override method abstract dengan logika khusus");
        
        System.out.println("\n🤲 جَزَاكَ اللَّهُ خَيْرًا");
        System.out.println("Semoga program ini bermanfaat untuk mengingatkan kita akan kewajiban sholat");
        System.out.println("\"Dan dirikanlah sholat, sesungguhnya sholat itu mencegah dari perbuatan keji dan mungkar\"");
        System.out.println("(QS. Al-Ankabut: 45)");
    }
}