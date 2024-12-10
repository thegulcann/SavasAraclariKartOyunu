package com.ikinciProje;
public abstract class Deniz extends SavasAraclari {

    public Deniz(int seviyePuani) {

        super(seviyePuani);
    }

    public Deniz(int seviyePuani, int dayaniklilik, String sinif, int vurus, int havaVurusAvantaji) {
        super(seviyePuani, dayaniklilik, sinif, vurus);
        setHavaVurusAvantaji(havaVurusAvantaji);
    }



    protected abstract int getHavaVurusAvantaji();
    protected abstract void setHavaVurusAvantaji(int havaVurusAvantaji);

    @Override
    public void KartPuaniGoster() {
        System.out.println("Deniz birimi bilgileri:");
        System.out.println("Seviye Puanı: " + seviyePuani);
        System.out.println("Dayanıklılık: " + getDayaniklilik());
        System.out.println("Vuruş Gücü: " + getVurus());
        System.out.println("Sınıf: " + getSinif());
    }

    @Override
    public void DurumGuncelle(int hasar) {
        int yeniDayaniklilik = getDayaniklilik() - hasar;
        setDayaniklilik(Math.max(yeniDayaniklilik, 0));
        System.out.println("Deniz birimi hasar aldı: " + hasar + ". Güncel dayanıklılık: " + getDayaniklilik());
    }
}
