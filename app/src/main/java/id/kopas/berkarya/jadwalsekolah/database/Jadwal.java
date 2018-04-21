package id.kopas.berkarya.jadwalsekolah.database;

public class Jadwal {

    public static final String TABLE_NAME = "jadwal";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_HARI = "hari";
    public static final String COLUMN_JADWAL = "jadwal";
    public static final String COLUMN_RUANG = "ruang";
    public static final String COLUMN_MULAI = "mulai";
    public static final String COLUMN_SELESAI = "selesai";
    public static final String COLUMN_PENGINGAT = "pengingat";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String hari;
    private String jadwal;
    private String ruang;
    private String mulai;
    private String selesai;
    private String pengingat;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_HARI + " TEXT,"
                    + COLUMN_JADWAL + " TEXT,"
                    + COLUMN_RUANG + " TEXT,"
                    + COLUMN_MULAI + " TEXT,"
                    + COLUMN_SELESAI + " TEXT,"
                    + COLUMN_PENGINGAT + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Jadwal() {
    }

    public Jadwal(int id, String hari, String jadwal, String ruang, String mulai, String selesai, String pengingat, String timestamp) {
        this.id = id;
        this.hari = hari;
        this.jadwal = jadwal;
        this.ruang = ruang;
        this.mulai = mulai;
        this.selesai = selesai;
        this.pengingat = pengingat;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }
    public String getHari() {
        return hari;
    }
    public String getJadwal() {
        return jadwal;
    }
    public String getRuang() {
        return ruang;
    }
    public String getMulai() {
        return mulai;
    }
    public String getSelesai() {
        return selesai;
    }
    public String getPengingat() {
        return pengingat;
    }
    public String getTimestamp() {
        return timestamp;
    }


    public void setId(int id) {
        this.id = id;
    }
    public void setHari(String hari) {
        this.hari = hari;
    }
    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }
    public void setRuang(String ruang) {
        this.ruang = ruang;
    }
    public void setMulai(String mulai) {
        this.mulai = mulai;
    }
    public void setSelesai(String selesai) {
        this.selesai = selesai;
    }
    public void setPengingat(String pengingat) {
        this.pengingat = pengingat;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
