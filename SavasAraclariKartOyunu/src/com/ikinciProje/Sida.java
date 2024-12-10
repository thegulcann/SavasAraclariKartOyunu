package com.ikinciProje;

public class Sida extends Deniz {
    private String sinif;
    private int havaVurusAvantaji;
    private int karaVurusAvantaji;

    public Sida(int seviyePuani, int dayaniklilik, String sinif, int vurus, int havaVurusAvantaji, int karaVurusAvantaji) {
        super(seviyePuani, dayaniklilik, sinif, vurus, havaVurusAvantaji);
        this.havaVurusAvantaji = havaVurusAvantaji;
        this.karaVurusAvantaji = karaVurusAvantaji;
        this.sinif = sinif;
    }

    public int getHavaVurusAvantaji() {
        return havaVurusAvantaji;
    }

    public void setHavaVurusAvantaji(int havaVurusAvantaji) {
        this.havaVurusAvantaji = havaVurusAvantaji;
    }

    public int getKaraVurusAvantaji() {
        return karaVurusAvantaji;
    }

    public void setKaraVurusAvantaji(int karaVurusAvantaji) {
        this.karaVurusAvantaji = karaVurusAvantaji;
    }

    @Override
    public void KartPuaniGoster() {
        System.out.println("Sida Kartı - Seviye Puanı: " + getSeviyePuani() +
                ", Dayanıklılık: " + getDayaniklilik() +
                ", Vuruş Gücü: " + getVurus() +
                ", Hava Vuruş Avantajı: " + getHavaVurusAvantaji() +
                ", Kara Vuruş Avantajı: " + getKaraVurusAvantaji() +
                ", Sınıf: " + sinif);
    }

    @Override
    public void DurumGuncelle(int hasar) {
        int yeniDayaniklilik = getDayaniklilik() - hasar;
        setDayaniklilik(Math.max(yeniDayaniklilik, 0));
        System.out.println("Sida hasar aldı: " + hasar + ". Güncel dayanıklılık: " + getDayaniklilik());
    }
}
