package com.ikinciProje;


public class Firkateyn extends Deniz {
    private int havaVurusAvantaji;


    public Firkateyn(int seviyePuani, int dayaniklilik, String sinif, int vurus, int havaVurusAvantaji) {
        super(seviyePuani, dayaniklilik, sinif, vurus,havaVurusAvantaji);
        this.havaVurusAvantaji = havaVurusAvantaji;
    }


    public int getHavaVurusAvantaji() {

        return havaVurusAvantaji;
    }

    public void setHavaVurusAvantaji(int havaVurusAvantaji) {
        this.havaVurusAvantaji = havaVurusAvantaji;
    }


    @Override
    public void KartPuaniGoster() {
        System.out.println("Firkateyn Kartı - Seviye Puanı: " + getSeviyePuani() +
                ", Dayanıklılık: " + getDayaniklilik() +
                ", Sınıf: " + getSinif() +
                ", Vuruş Gücü: " + getVurus() +
                ", Hava Vuruş Avantajı: " + havaVurusAvantaji);
    }


    @Override
    public void DurumGuncelle(int hasar) {
        int kalanDayaniklilik = getDayaniklilik() - hasar;
        setDayaniklilik(kalanDayaniklilik);
        System.out.println("Firkateyn hasar aldı: " + hasar +
                ", Kalan Dayanıklılık: " + kalanDayaniklilik);
    }
}