package com.ikinciProje;


public abstract class Hava extends SavasAraclari{
    protected int karaVurusAvantaji;

    public Hava(int seviyePuani, int dayaniklilik, String sinif, int vurus) {
        super(seviyePuani, dayaniklilik, sinif, vurus);
    }

    public abstract int getKaraVurusAvantaji();
    public abstract void setKaraVurusAvantaji(int karaVurusAvantaji);

    public Hava(int seviyePuani) {
        super(seviyePuani);
    }

    public Hava(int seviyePuanı, int KaraVurusAvantaji){

        super(seviyePuanı);

    }

    @Override
    public void KartPuaniGoster() {
        System.out.println("Hava birimi kart puanı: " + seviyePuani);
    }

    @Override
    public void DurumGuncelle(int hasar) {
        System.out.println("Hava birimi hasar aldı: " + hasar);
    }
}