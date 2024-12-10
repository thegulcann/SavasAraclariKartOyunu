package com.ikinciProje;
public abstract class SavasAraclari implements Cloneable {
    protected int seviyePuani;
    protected int dayaniklilik;
    protected String sinif;
    protected int vurus;


    public SavasAraclari(int seviyePuani, int dayaniklilik, String sinif, int vurus) {
        this.seviyePuani = seviyePuani;
        this.dayaniklilik = dayaniklilik;
        this.sinif = sinif;
        this.vurus = vurus;
    }

    public SavasAraclari(int seviyePuani) {
        this.seviyePuani = seviyePuani;
    }

    // Getter ve Setter Metotlar
    public int getSeviyePuani() {
        return seviyePuani;
    }

    public void setSeviyePuani(int seviyePuani) {
        this.seviyePuani = seviyePuani;
    }

    public int getDayaniklilik() {
        return dayaniklilik;
    }

    public void setDayaniklilik(int dayaniklilik) {
        this.dayaniklilik = dayaniklilik;
    }

    public String getSinif() {
        return sinif;
    }

    public void setSinif(String sinif) {
        this.sinif = sinif;
    }

    public int getVurus() {
        return vurus;
    }

    public void setVurus(int vurus) {
        this.vurus = vurus;
    }


    public boolean isAlive() {
        return this.dayaniklilik > 0;
    }


    public abstract void KartPuaniGoster();

    public abstract void DurumGuncelle(int hasar);


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}