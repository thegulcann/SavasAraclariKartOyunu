package com.ikinciProje;

public class Siha extends Hava {
    private int karaVurusAvantaji;
    private int denizVurusAvantaji;


    public Siha(int seviyePuani, int dayaniklilik, String sinif, int vurus, int karaVurusAvantaji, int denizVurusAvantaji) {
        super(seviyePuani, dayaniklilik, sinif, vurus);
        this.karaVurusAvantaji = karaVurusAvantaji;
        this.denizVurusAvantaji = denizVurusAvantaji;
    }


    @Override
    public int getKaraVurusAvantaji() {
        return karaVurusAvantaji;
    }

    @Override
    public void setKaraVurusAvantaji(int karaVurusAvantaji) {
        this.karaVurusAvantaji = karaVurusAvantaji;
    }

    public int getDenizVurusAvantaji() {
        return denizVurusAvantaji;
    }

    public void setDenizVurusAvantaji(int denizVurusAvantaji) {
        this.denizVurusAvantaji = denizVurusAvantaji;
    }

    @Override
    public void KartPuaniGoster() {
        System.out.println(
                "SİHA Kartı:\n" +
                        "Seviye Puanı: " + getSeviyePuani() + "\n" +
                        "Dayanıklılık: " + getDayaniklilik() + "\n" +
                        "Sınıf: " + getSinif() + "\n" +
                        "Vuruş Gücü: " + getVurus() + "\n" +
                        "Kara Vuruş Avantajı: " + getKaraVurusAvantaji() + "\n" +
                        "Deniz Vuruş Avantajı: " + getDenizVurusAvantaji()
        );
    }

    @Override
    public void DurumGuncelle(int hasar) {
        int yeniDayaniklilik = getDayaniklilik() - hasar;
        setDayaniklilik(Math.max(yeniDayaniklilik, 0)); // Dayanıklılık sıfırın altına inemez
        System.out.println("SİHA hasar aldı: " + hasar +
                ", Kalan Dayanıklılık: " + getDayaniklilik());
    }
}
