
package com.ikinciProje;
public class Obus extends Kara {
    private String sinif;
    private int denizVurusAvantaji;


    public Obus(int seviyePuani, int dayaniklilik, String sinif, int vurus, int denizVurusAvantaji) {
        super(seviyePuani, dayaniklilik, sinif, vurus,denizVurusAvantaji);
        this.denizVurusAvantaji = denizVurusAvantaji;
        this.sinif=sinif;

    }


    public int getDenizVurusAvantaji() {
        return denizVurusAvantaji;
    }

    public void setDenizVurusAvantaji(int denizVurusAvantaji) {
        this.denizVurusAvantaji = denizVurusAvantaji;
    }

    @Override
    public void KartPuaniGoster() {
        System.out.println("Obüs Kartı - Seviye Puanı: " + getSeviyePuani() + ", Dayanıklılık: " + getDayaniklilik() +
                ", Vuruş Gücü: " + getVurus() +
                ", Deniz Vuruş Avantajı: " + denizVurusAvantaji +
                ", Sınıf: " + sinif);
    }

    @Override
    public void DurumGuncelle(int hasar) {
        int yeniDayaniklilik = getDayaniklilik() - hasar;
        setDayaniklilik(Math.max(yeniDayaniklilik, 0)); // Dayanıklılık sıfırın altına inemez
        System.out.println("Obüs hasar aldı: " + hasar + ". Güncel dayanıklılık: " + getDayaniklilik());
    }
}