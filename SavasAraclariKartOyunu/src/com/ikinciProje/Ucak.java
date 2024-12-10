
package com.ikinciProje;
public class Ucak extends Hava {
    private int karaVurusAvantaji;


    public Ucak(int seviyePuani, int dayaniklilik, String sinif, int vurus, int karaVurusAvantaji) {
        super(seviyePuani, dayaniklilik, sinif, vurus);
        this.karaVurusAvantaji = karaVurusAvantaji;
    }

    @Override
    public int getKaraVurusAvantaji() {
        return karaVurusAvantaji;
    }

    @Override
    public void setKaraVurusAvantaji(int karaVurusAvantaji) {
        this.karaVurusAvantaji = karaVurusAvantaji;
    }

    @Override
    public void KartPuaniGoster() {
        System.out.println(
                "Uçak Kartı:\n" +
                        "Seviye Puanı: " + seviyePuani + "\n" +
                        "Dayanıklılık: " + dayaniklilik + "\n" +
                        "Sınıf: " + sinif + "\n" +
                        "Vuruş Gücü: " + vurus + "\n" +
                        "Kara Vuruş Avantajı: " + karaVurusAvantaji
        );
    }

    @Override
    public void DurumGuncelle(int hasar) {
        dayaniklilik -= hasar;
        if (dayaniklilik < 0) {
            dayaniklilik = 0;
        }
        System.out.println("Uçak hasar aldı: " + hasar + ", Kalan Dayanıklılık: " + dayaniklilik);
    }
}