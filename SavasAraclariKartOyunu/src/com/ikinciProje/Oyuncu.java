package com.ikinciProje;
import java.util.*;

public class Oyuncu {
    private int oyuncuID;
    private String oyuncuAdi;
    private int skor;
    private List<SavasAraclari> kartListesi;
    private boolean bilgisayarMi;
    private Scanner scanner;
    private int seviyePuani;


    public Oyuncu() {
        this.oyuncuID = 0;
        this.oyuncuAdi = "Oyuncu";
        this.skor = 0;
        this.kartListesi = new ArrayList<>();
        this.seviyePuani = 0;
        this.bilgisayarMi = false;
        if (!bilgisayarMi) {
            this.scanner = new Scanner(System.in);
        }
    }


    public Oyuncu(int oyuncuID, String oyuncuAdi, int skor, boolean bilgisayarMi) {
        this.oyuncuID = oyuncuID;
        this.oyuncuAdi = oyuncuAdi;
        this.skor = skor;
        this.kartListesi = new ArrayList<>();
        this.bilgisayarMi = bilgisayarMi;
        this.seviyePuani = 0;
        if (!bilgisayarMi) {
            this.scanner = new Scanner(System.in);

        }
    }


    public void SkorGoster() {
        System.out.println(oyuncuAdi + " Skor: " + skor);
    }


    public void kartEkle(SavasAraclari kart) {
        if (!kartListesi.contains(kart)) {
            kartListesi.add(kart);
        }
    }
    public int getSeviyePuani() {
        return seviyePuani;
    }

    public void setSeviyePuani(int seviyePuani) {
        this.seviyePuani = seviyePuani;
    }


    public void skorEkle(int puan) {
        this.skor += puan;
    }


    public boolean hasAliveKart() {
        for (SavasAraclari kart : kartListesi) {
            if (kart.isAlive()) {
                return true;
            }
        }
        return false;
    }


    public void kartlariTemizle() {
        kartListesi.removeIf(kart -> !kart.isAlive());
    }


    public List<SavasAraclari> kartSec(int kacKartSecilecek) {
        List<SavasAraclari> secilenKartlar = new ArrayList<>();
        Set<SavasAraclari> secilenKartSeti = new HashSet<>();


        kartlariTemizle();

        if (kartListesi.isEmpty()) {
            System.out.println(oyuncuAdi + " artık kartı kalmadı.");
            return secilenKartlar;
        }

        int secilecekKartSayisi = Math.min(kacKartSecilecek, kartListesi.size());

        if (bilgisayarMi) {

            Random random = new Random();
            while (secilenKartlar.size() < secilecekKartSayisi && !kartListesi.isEmpty()) {
                int rastgeleIndex = random.nextInt(kartListesi.size());
                SavasAraclari secilenKart = kartListesi.get(rastgeleIndex);
                if (!secilenKartSeti.contains(secilenKart)) {
                    secilenKartlar.add(secilenKart);
                    secilenKartSeti.add(secilenKart);
                }
            }
        } else {

            while (secilenKartlar.size() < secilecekKartSayisi && !kartListesi.isEmpty()) {
                System.out.println("\n" + oyuncuAdi + " elindeki kartlardan seçmelidir:");
                for (int i = 0; i < kartListesi.size(); i++) {
                    SavasAraclari kart = kartListesi.get(i);
                    System.out.println((i + 1) + ". " + kart.getSinif() + " (Dayanıklılık: " + kart.getDayaniklilik() + ")");
                }

                System.out.print("Seçiminizi yapın (1-" + kartListesi.size() + "): ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Lütfen bir sayı girin.");
                    scanner.next(); // Geçersiz girdiyi atla
                }
                int secim = scanner.nextInt() - 1;
                scanner.nextLine(); // Tamponu temizle

                if (secim >= 0 && secim < kartListesi.size()) {
                    SavasAraclari secilenKart = kartListesi.get(secim);
                    if (!secilenKartSeti.contains(secilenKart)) {
                        secilenKartlar.add(secilenKart);
                        secilenKartSeti.add(secilenKart);
                        System.out.println(secilenKart.getSinif() + " seçildi.");
                    } else {
                        System.out.println("Bu kartı zaten seçtiniz.");
                    }
                } else {
                    System.out.println("Geçersiz seçim.");
                }
            }
        }
        return secilenKartlar;
    }


    public int getOyuncuID() {
        return oyuncuID;
    }

    public String getOyuncuAdi() {
        return oyuncuAdi;
    }

    public int getSkor() {
        return skor;
    }

    public void setSkor(int skor) {
        this.skor = skor;
    }

    public List<SavasAraclari> getKartListesi() {
        return kartListesi;
    }

    public boolean isBilgisayarMi() {
        return bilgisayarMi;
    }
}