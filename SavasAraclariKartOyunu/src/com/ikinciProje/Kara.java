
package com.ikinciProje;
public abstract class Kara extends SavasAraclari{

    protected abstract int getDenizVurusAvantaji();
    protected abstract void setDenizVurusAvantaji(int denizVurusAvantaji);


    public Kara(int seviyePuani) {
        super(seviyePuani);
    }

    public Kara(int seviyePuani,int dayaniklilik, String sinif,int vurus, int denizVurusuAvantaji) {
        super(seviyePuani,dayaniklilik,sinif,vurus);
        setDenizVurusAvantaji(denizVurusuAvantaji);
    }

    @Override
    public void KartPuaniGoster() {
        System.out.println("Kara birimi kart puanı: " + seviyePuani);
    }

    @Override
    public void DurumGuncelle(int hasar) {
        System.out.println("Kara birimi hasar aldı: " + hasar);
    }
}