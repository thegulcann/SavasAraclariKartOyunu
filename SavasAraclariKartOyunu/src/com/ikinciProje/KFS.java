
package com.ikinciProje;
public class KFS extends Kara {
    private String sinif;
    private int denizVurusAvantaji;
    private int havaVurusAvantaji;


    public KFS(int seviyePuani, int dayaniklilik, String sinif, int vurus, int denizVurusAvantaji, int havaVurusAvantaji) {
        super(seviyePuani, dayaniklilik, sinif, vurus, denizVurusAvantaji);
        this.denizVurusAvantaji = denizVurusAvantaji;
        this.havaVurusAvantaji = havaVurusAvantaji;
        this.sinif = sinif;
    }


    public String getSinif() {
        return sinif;
    }

    public void setSinif(String sinif) {
        this.sinif = sinif;
    }

    public int getDenizVurusAvantaji() {
        return denizVurusAvantaji;
    }

    public void setDenizVurusAvantaji(int denizVurusAvantaji) {
        this.denizVurusAvantaji = denizVurusAvantaji;
    }

    public int getHavaVurusAvantaji() {
        return havaVurusAvantaji;
    }

    public void setHavaVurusAvantaji(int havaVurusAvantaji) {
        this.havaVurusAvantaji = havaVurusAvantaji;
    }

    @Override
    public void KartPuaniGoster() {
        System.out.println("KFS Kartı - Seviye Puanı: " + getSeviyePuani() +
                ", Dayanıklılık: " + getDayaniklilik() +
                ", Vuruş Gücü: " + getVurus() +
                ", Deniz Vuruş Avantajı: " + denizVurusAvantaji +
                ", Hava Vuruş Avantajı: " + havaVurusAvantaji +
                ", Sınıf: " + sinif);
    }

    @Override
    public void DurumGuncelle(int hasar) {
        int yeniDayaniklilik = getDayaniklilik() - hasar;
        setDayaniklilik(Math.max(yeniDayaniklilik, 0));
        System.out.println("KFS hasar aldı: " + hasar + ". Güncel dayanıklılık: " + getDayaniklilik());
    }
}
