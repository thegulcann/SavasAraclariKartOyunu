package com.ikinciProje;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class Oyun extends Application {

    private Oyuncu oyuncu;
    private Oyuncu bilgisayar;
    private int toplamHamle;
    private int seviyePuani;
    private Map<String, Integer> kartSayac;
    private List<SavasAraclari> oyuncuOncekiSecilmeyenKartlar;
    private List<SavasAraclari> bilgisayarOncekiSecilmeyenKartlar;
    private Set<SavasAraclari> oyundanCikartilanKartlar;
    private Random random;
    private int hamle = 1;
    private List<SavasAraclari> kartHavuzu;

    private Label hamleLabel;
    private Label seviyePuaniLabel;
    private HBox oyuncuKartlariHBox;
    private HBox bilgisayarKartlariHBox;
    private List<SavasAraclari> oyuncuSecilenKartlar = new ArrayList<>();
    private Map<SavasAraclari, Button> oyuncuKartButonlari = new HashMap<>();
    private Button hamleYapButonu;
    private Label sonucLabel;

    private Stage primaryStageRef;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStageRef = primaryStage;


        try {

            FileOutputStream fos = new FileOutputStream("console.log", true);


            PrintStream console = System.out;


            TeeOutputStream tos = new TeeOutputStream(console, fos);
            PrintStream ps = new PrintStream(tos, true);
            System.setOut(ps);
            System.setErr(ps);

            System.out.println("Konsol çıktıları artık 'console.log' dosyasına da yazılacak.");
        } catch (IOException e) {
            e.printStackTrace();

        }


        random = new Random();


        seviyePuani = getSeviyePuani();

        oyuncu = new Oyuncu(1, "Oyuncu", seviyePuani, false);
        bilgisayar = new Oyuncu(2, "Bilgisayar", seviyePuani, true);

        oyuncu.setSkor(0);
        bilgisayar.setSkor(0);


        oyuncu.setSeviyePuani(seviyePuani);
        bilgisayar.setSeviyePuani(seviyePuani);


        System.out.println("Oyuncu Seviye Puani: " + oyuncu.getSeviyePuani());
        System.out.println("Bilgisayar Seviye Puani: " + bilgisayar.getSeviyePuani());
        System.out.println("Oyuncu Başlangıç Skoru: " + oyuncu.getSkor());
        System.out.println("Bilgisayar Başlangıç Skoru: " + bilgisayar.getSkor());

        kartSayac = new HashMap<>();
        oyuncuOncekiSecilmeyenKartlar = new ArrayList<>();
        bilgisayarOncekiSecilmeyenKartlar = new ArrayList<>();
        oyundanCikartilanKartlar = new HashSet<>();
        kartHavuzu = kartHavuzuOlustur();


        toplamHamle = getToplamHamle();

        baslangicKartDagit(6);

        oyuncuOncekiSecilmeyenKartlar.addAll(oyuncu.getKartListesi());
        bilgisayarOncekiSecilmeyenKartlar.addAll(bilgisayar.getKartListesi());

        hamleLabel = new Label("Hamle: " + hamle);
        hamleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        seviyePuaniLabel = new Label("Seviye Puanı: " + seviyePuani);
        seviyePuaniLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        oyuncuKartlariHBox = new HBox(30);
        oyuncuKartlariHBox.setPadding(new Insets(20));
        oyuncuKartlariHBox.setAlignment(Pos.CENTER);

        bilgisayarKartlariHBox = new HBox(30);
        bilgisayarKartlariHBox.setPadding(new Insets(20));
        bilgisayarKartlariHBox.setAlignment(Pos.CENTER);

        oyuncuKartlariGuncelle();
        bilgisayarKartlariGuncelle();

        hamleYapButonu = new Button("Hamle Yap");
        hamleYapButonu.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        hamleYapButonu.setOnAction(event -> {
            if (oyuncuSecilenKartlar.size() == 3) {
                hamleYap();
            } else {
                Alert uyarı = new Alert(Alert.AlertType.WARNING);
                uyarı.setTitle("Uyarı");
                uyarı.setHeaderText(null);
                uyarı.setContentText("Lütfen 3 kart seçin!");
                uyarı.showAndWait();
            }
        });

        sonucLabel = new Label("");
        sonucLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: red;");

        VBox anaLayout = new VBox(20,
                hamleLabel,
                seviyePuaniLabel, // Seviye puanı label'ını ekledik
                new Label("Bilgisayar Kartları:"),
                bilgisayarKartlariHBox,
                new Separator(),
                new Label("Oyuncu Kartları (Öncelikli kartlar * ile işaretlenmiştir):"),
                oyuncuKartlariHBox,
                hamleYapButonu,
                sonucLabel // Sonuç label'ını ekledik
        );
        anaLayout.setPadding(new Insets(20));
        anaLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(anaLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Savaş Kartları Oyunu");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    private int getToplamHamle() {
        TextInputDialog dialog = new TextInputDialog("5"); // Varsayılan değer 5
        dialog.setTitle("Hamle Sayısı Belirleme");
        dialog.setHeaderText("Oyun Hamle Sayısını Belirleyin");
        dialog.setContentText("Maksimum hamle sayısını giriniz:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int inputHamle = Integer.parseInt(result.get());
                if (inputHamle > 0) {
                    System.out.println("Kullanıcı tarafından girilen hamle sayısı: " + inputHamle);
                    return inputHamle;
                } else {
                    showErrorDialog("Geçersiz Hamle Sayısı", "Hamle sayısı pozitif bir sayı olmalıdır.");
                    return 5;
                }
            } catch (NumberFormatException e) {
                showErrorDialog("Geçersiz Giriş", "Lütfen geçerli bir sayı giriniz.");
                return 5;
            }
        } else {

            return 5;
        }
    }


    private int getSeviyePuani() {
        TextInputDialog dialog = new TextInputDialog("0");
        dialog.setTitle("Seviye Puanı Belirleme");
        dialog.setHeaderText("Oyunun Başlangıç Seviye Puanını Belirleyin");
        dialog.setContentText("Başlangıç seviye puanını giriniz:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int inputPuan = Integer.parseInt(result.get());
                if (inputPuan >= 0) {
                    System.out.println("Kullanıcı tarafından girilen seviye puanı: " + inputPuan);
                    return inputPuan;
                } else {
                    showErrorDialog("Geçersiz Seviye Puanı", "Seviye puanı negatif olamaz.");
                    return 0;
                }
            } catch (NumberFormatException e) {
                showErrorDialog("Geçersiz Giriş", "Lütfen geçerli bir sayı giriniz.");
                return 0;
            }
        } else {

            return 0;
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private List<SavasAraclari> kartHavuzuOlustur() {
        List<SavasAraclari> kartHavuzu = new ArrayList<>();
        kartHavuzu.add(new Ucak(seviyePuani, 20, "Uçak", 10, 10));
        kartHavuzu.add(new Obus(seviyePuani, 20, "Obüs", 10, 5));
        kartHavuzu.add(new Firkateyn(seviyePuani, 25, "Fırkateyn", 10, 5));
        kartHavuzu.add(new Siha(seviyePuani, 15, "SİHA", 10, 10, 10));
        kartHavuzu.add(new KFS(seviyePuani, 10, "KFS", 10, 10, 20));
        kartHavuzu.add(new Sida(seviyePuani, 15, "Sida", 10, 10, 10));
        return kartHavuzu;
    }


    private void baslangicKartDagit(int kartSayisi) {
        System.out.println("Oyuncuya ve Bilgisayara verilen başlangıç kartları:");


        List<SavasAraclari> uygunKartlar = new ArrayList<>();
        for (SavasAraclari kart : kartHavuzu) {
            if (kart instanceof Ucak || kart instanceof Obus || kart instanceof Firkateyn) {
                uygunKartlar.add(kart);
            }
        }

        for (int i = 0; i < kartSayisi; i++) {

            SavasAraclari yeniKartOyuncu = kartIsminiNumaralandir(uygunKartlar.get(random.nextInt(uygunKartlar.size())));

            yeniKartOyuncu.setSeviyePuani(seviyePuani);
            oyuncu.kartEkle(yeniKartOyuncu);
            System.out.println("Oyuncu: - " + yeniKartOyuncu.getSinif() + " (Dayanıklılık: " + yeniKartOyuncu.getDayaniklilik() + ", Seviye Puani: " + yeniKartOyuncu.getSeviyePuani() + ")");


            SavasAraclari yeniKartBilgisayar = kartIsminiNumaralandir(uygunKartlar.get(random.nextInt(uygunKartlar.size())));

            yeniKartBilgisayar.setSeviyePuani(seviyePuani);
            yeniKartBilgisayar.setVurus(10);
            bilgisayar.kartEkle(yeniKartBilgisayar);
            System.out.println("Bilgisayar: - " + yeniKartBilgisayar.getSinif() + " (Dayanıklılık: " + yeniKartBilgisayar.getDayaniklilik() + ", Seviye Puani: " + yeniKartBilgisayar.getSeviyePuani() + ")");
        }
        System.out.println("Başlangıç kartları dağıtıldı!\n");
    }


    private SavasAraclari kartIsminiNumaralandir(SavasAraclari kart) {
        try {
            // Yeni bir kart kopyası oluştur
            SavasAraclari yeniKart = (SavasAraclari) kart.clone();
            String sinifAdi = yeniKart.getSinif();
            kartSayac.put(sinifAdi, kartSayac.getOrDefault(sinifAdi, 0) + 1);
            int numara = kartSayac.get(sinifAdi);
            yeniKart.setSinif(sinifAdi + numara);
            System.out.println("Yeni kart oluşturuldu: " + yeniKart.getSinif() + " (Dayanıklılık: " + yeniKart.getDayaniklilik() + ", Seviye Puani: " + yeniKart.getSeviyePuani() + ")");
            return yeniKart;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Kart kopyalama sırasında hata oluştu!");
        }
    }


    private void oyuncuKartlariGuncelle() {
        oyuncuKartlariHBox.getChildren().clear();
        oyuncuKartButonlari.clear();


        List<SavasAraclari> oncelikliKartlar = new ArrayList<>();
        List<SavasAraclari> digerKartlar = new ArrayList<>();

        for (SavasAraclari kart : oyuncu.getKartListesi()) {
            if (kart.isAlive()) {
                if (oyuncuOncekiSecilmeyenKartlar.contains(kart)) {
                    oncelikliKartlar.add(kart);
                } else {
                    digerKartlar.add(kart);
                }
            }
        }


        List<SavasAraclari> tumKartlar = new ArrayList<>();
        tumKartlar.addAll(oncelikliKartlar);
        tumKartlar.addAll(digerKartlar);

        for (SavasAraclari kart : tumKartlar) {
            Button kartButonu;
            if (oncelikliKartlar.contains(kart)) {
                kartButonu = new Button("*" + kart.getSinif() + "\nDayanıklılık: " + kart.getDayaniklilik());
                kartButonu.setStyle("-fx-background-color: lightgreen; -fx-font-size: 14px;");
            } else {
                kartButonu = new Button(kart.getSinif() + "\nDayanıklılık: " + kart.getDayaniklilik());
                kartButonu.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px;");
            }
            kartButonu.setPrefSize(200, 250);
            kartButonu.setWrapText(true);
            kartButonu.setOnAction(event -> kartSecimIslemi(kart, kartButonu));
            oyuncuKartlariHBox.getChildren().add(kartButonu);
            oyuncuKartButonlari.put(kart, kartButonu);
        }
    }

    private void bilgisayarKartlariGuncelle() {
        bilgisayarKartlariHBox.getChildren().clear();

        for (int i = 0; i < bilgisayar.getKartListesi().size(); i++) {
            SavasAraclari kart = bilgisayar.getKartListesi().get(i);
            if (kart.isAlive()) {
                Button kartButonu = new Button();
                kartButonu.setStyle("-fx-background-color: gray;");
                kartButonu.setPrefSize(200, 250);
                bilgisayarKartlariHBox.getChildren().add(kartButonu);
            }
        }
    }


    private void kartSecimIslemi(SavasAraclari kart, Button kartButonu) {
        boolean oncelikli = oyuncuOncekiSecilmeyenKartlar.contains(kart);


        if (!oncelikli && !oyuncuOncekiSecilmeyenKartlar.isEmpty()) {
            // Öncelikli kartlardan kaç tane seçildi?
            int secilenOncelikliKartSayisi = 0;
            for (SavasAraclari secilenKart : oyuncuSecilenKartlar) {
                if (oyuncuOncekiSecilmeyenKartlar.contains(secilenKart)) {
                    secilenOncelikliKartSayisi++;
                }
            }

            if (secilenOncelikliKartSayisi < oyuncuOncekiSecilmeyenKartlar.size()) {
                Alert uyarı = new Alert(Alert.AlertType.WARNING);
                uyarı.setTitle("Uyarı");
                uyarı.setHeaderText(null);
                uyarı.setContentText("Öncelikli kartları seçmeden diğer kartları seçemezsiniz!");
                uyarı.showAndWait();
                return;
            }
        }

        if (oyuncuSecilenKartlar.size() < 3 && !oyuncuSecilenKartlar.contains(kart)) {
            oyuncuSecilenKartlar.add(kart);
            kartButonu.setStyle("-fx-background-color: gold; -fx-font-size: 14px;");
        } else if (oyuncuSecilenKartlar.contains(kart)) {
            oyuncuSecilenKartlar.remove(kart);
            if (oncelikli) {
                kartButonu.setStyle("-fx-background-color: lightgreen; -fx-font-size: 14px;");
            } else {
                kartButonu.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px;");
            }
        } else {
            Alert uyarı = new Alert(Alert.AlertType.WARNING);
            uyarı.setTitle("Uyarı");
            uyarı.setHeaderText(null);
            uyarı.setContentText("En fazla 3 kart seçebilirsiniz!");
            uyarı.showAndWait();
        }
    }


    private void hamleYap() {
        System.out.println("\n--- Hamle " + hamle + " Başlıyor ---");


        oyuncuOncekiSecilmeyenKartlar.removeAll(oyuncuSecilenKartlar);


        List<SavasAraclari> bilgisayarSecilenKartlar = bilgisayaraKartSecimYap(
                bilgisayarOncekiSecilmeyenKartlar,
                bilgisayar.getKartListesi(),
                hamle
        );


        if (bilgisayarSecilenKartlar.size() < 3) {
            Alert uyarı = new Alert(Alert.AlertType.WARNING);
            uyarı.setTitle("Uyarı");
            uyarı.setHeaderText(null);
            uyarı.setContentText("Bilgisayar yeterli kart seçemedi!");
            uyarı.showAndWait();
            return;
        }


        showKarsilastirmaEkrani(oyuncuSecilenKartlar, bilgisayarSecilenKartlar, () -> {

            karsilastirVeSonuclariGuncelle(oyuncuSecilenKartlar, bilgisayarSecilenKartlar);
        });
    }


    private void showKarsilastirmaEkrani(List<SavasAraclari> oyuncuKartlar, List<SavasAraclari> bilgisayarKartlar, Runnable onClose) {
        Stage karsilastirmaStage = new Stage();
        karsilastirmaStage.initModality(Modality.APPLICATION_MODAL);
        karsilastirmaStage.setTitle("Kart Karşılaştırması");

        VBox layout = new VBox(30);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);


        VBox bilgisayarKartlarVBox = new VBox(20);
        bilgisayarKartlarVBox.setAlignment(Pos.CENTER);
        Label bilgisayarLabel = new Label("Bilgisayar Kartları:");
        bilgisayarLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        bilgisayarKartlarVBox.getChildren().add(bilgisayarLabel);

        HBox bilgisayarKartlarHBox = new HBox(30);
        bilgisayarKartlarHBox.setAlignment(Pos.CENTER);

        for (SavasAraclari kart : bilgisayarKartlar) {
            VBox kartVBox = createKartVBox(kart, "Bilgisayar");
            bilgisayarKartlarHBox.getChildren().add(kartVBox);
        }

        bilgisayarKartlarVBox.getChildren().add(bilgisayarKartlarHBox);


        VBox oyuncuKartlarVBox = new VBox(20);
        oyuncuKartlarVBox.setAlignment(Pos.CENTER);
        Label oyuncuLabel = new Label("Oyuncu Kartları:");
        oyuncuLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        oyuncuKartlarVBox.getChildren().add(oyuncuLabel);

        HBox oyuncuKartlarHBox = new HBox(30);
        oyuncuKartlarHBox.setAlignment(Pos.CENTER);

        for (SavasAraclari kart : oyuncuKartlar) {
            VBox kartVBox = createKartVBox(kart, "Oyuncu");
            oyuncuKartlarHBox.getChildren().add(kartVBox);
        }

        oyuncuKartlarVBox.getChildren().add(oyuncuKartlarHBox);


        layout.getChildren().addAll(bilgisayarKartlarVBox, oyuncuKartlarVBox);


        Button devamButonu = new Button("Devam Et");
        devamButonu.setPrefWidth(200);
        devamButonu.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        devamButonu.setOnAction(event -> {
            karsilastirmaStage.close();
            onClose.run();
        });

        layout.getChildren().add(devamButonu);

        Scene scene = new Scene(layout, 1200, 800);
        karsilastirmaStage.setScene(scene);
        karsilastirmaStage.setMaximized(true);
        karsilastirmaStage.showAndWait();
    }


    private VBox createKartVBox(SavasAraclari kart, String sahip) {
        VBox kartVBox = new VBox(10);
        kartVBox.setAlignment(Pos.CENTER);
        kartVBox.setPadding(new Insets(10));
        kartVBox.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: white;");
        kartVBox.setPrefSize(200, 250); // Kart kutusunun boyutlarını belirledik

        Label sahipLabel = new Label(sahip + " Kartı:");
        sahipLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        Label sinifLabel = new Label("Sınıf: " + kart.getSinif());
        sinifLabel.setStyle("-fx-font-size: 14px;");
        Label dayanıklılıkLabel = new Label("Dayanıklılık: " + kart.getDayaniklilik());
        dayanıklılıkLabel.setStyle("-fx-font-size: 14px;");



        kartVBox.getChildren().addAll(sahipLabel, sinifLabel, dayanıklılıkLabel);
        return kartVBox;
    }


    private void karsilastirVeSonuclariGuncelle(List<SavasAraclari> oyuncuKartlar, List<SavasAraclari> bilgisayarKartlar) {
        int karsilastirmaSayisi = Math.min(oyuncuKartlar.size(), bilgisayarKartlar.size());

        for (int i = 0; i < karsilastirmaSayisi; i++) {
            SavasAraclari oyuncuKart = oyuncuKartlar.get(i);
            SavasAraclari bilgisayarKart = bilgisayarKartlar.get(i);

            System.out.println("\nKarşılaşma " + (i + 1) + ":");
            System.out.println("Oyuncu Kartı: " + oyuncuKart.getSinif() + " (Dayanıklılık: " + oyuncuKart.getDayaniklilik() + ", Seviye Puani: " + oyuncuKart.getSeviyePuani() + ")");
            System.out.println("Bilgisayar Kartı: " + bilgisayarKart.getSinif() + " (Dayanıklılık: " + bilgisayarKart.getDayaniklilik() + ", Seviye Puani: " + bilgisayarKart.getSeviyePuani() + ")");

            int[] saldiriDegerleri = SaldiriHesapla(oyuncuKart, bilgisayarKart);

            System.out.println("Oyuncu Saldırı Gücü: " + saldiriDegerleri[0]);
            System.out.println("Bilgisayar Saldırı Gücü: " + saldiriDegerleri[1]);


            oyuncuKart.setDayaniklilik(oyuncuKart.getDayaniklilik() - saldiriDegerleri[1]);
            bilgisayarKart.setDayaniklilik(bilgisayarKart.getDayaniklilik() - saldiriDegerleri[0]);

            System.out.println("Karşılaşma Sonrası Durum:");
            System.out.println("Oyuncu Kartı Dayanıklılığı: " + oyuncuKart.getDayaniklilik());
            System.out.println("Bilgisayar Kartı Dayanıklılığı: " + bilgisayarKart.getDayaniklilik());


            if (!oyuncuKart.isAlive()) {
                int kaybedenSeviyePuani = oyuncuKart.getSeviyePuani();

                int puanArtisi = (kaybedenSeviyePuani < 10) ? 10 : kaybedenSeviyePuani;


                bilgisayarKart.setSeviyePuani(bilgisayarKart.getSeviyePuani() + puanArtisi);
                bilgisayar.skorEkle(puanArtisi);

                System.out.println("Oyuncu kartı elendi: " + oyuncuKart.getSinif());
                System.out.println("Bilgisayar kartının seviye puanı arttı: +" + puanArtisi +
                        " (" + bilgisayarKart.getSinif() + "'ın yeni seviye puanı: " + bilgisayarKart.getSeviyePuani() + ")");
            }


            if (!bilgisayarKart.isAlive()) {
                int kaybedenSeviyePuani = bilgisayarKart.getSeviyePuani();
                // Puani şartlı olarak artır
                int puanArtisi = (kaybedenSeviyePuani < 10) ? 10 : kaybedenSeviyePuani;

                // Oyuncu kartının seviye puanını artır
                oyuncuKart.setSeviyePuani(oyuncuKart.getSeviyePuani() + puanArtisi);
                oyuncu.skorEkle(puanArtisi);

                System.out.println("Bilgisayar kartı elendi: " + bilgisayarKart.getSinif());
                System.out.println("Oyuncu kartının seviye puanı arttı: +" + puanArtisi +
                        " (" + oyuncuKart.getSinif() + "'ın yeni seviye puanı: " + oyuncuKart.getSeviyePuani() + ")");
            }
        }


        System.out.println("\nGüncel Skorlar:");
        System.out.println("Oyuncu Skor: " + oyuncu.getSkor());
        System.out.println("Bilgisayar Skor: " + bilgisayar.getSkor());


        hamle++;
        hamleLabel.setText("Hamle: " + hamle);
        seviyePuaniLabel.setText("Seviye Puanı: " + seviyePuani);


        oyuncuSecilenKartlar.clear();
        oyuncuKartlariGuncelle();
        bilgisayarKartlariGuncelle();


        if (hamle > toplamHamle || !oyuncu.hasAliveKart() || !bilgisayar.hasAliveKart()) {
            oyunuBitir();
            oyunSonuMesajiGoster();
        } else {

            yeniKartDagit();
            oyuncuKartlariGuncelle();
            bilgisayarKartlariGuncelle();
        }
    }


    public List<SavasAraclari> bilgisayaraKartSecimYap(List<SavasAraclari> secilmeyenKartlar, List<SavasAraclari> bilgisayarKartListesi, int hamle) {
        Random random = new Random();
        List<SavasAraclari> secilenKartlar = new ArrayList<>();

        List<SavasAraclari> bilgisayarKartListesiCopy = new ArrayList<>();
        for (SavasAraclari kart : bilgisayarKartListesi) {
            if (kart.isAlive()) {
                bilgisayarKartListesiCopy.add(kart);
            }
        }

        if (bilgisayarKartListesiCopy.isEmpty()) {
            System.out.println("Bilgisayarın seçebileceği kart kalmadı!");
            return secilenKartlar;
        }


        List<SavasAraclari> oncelikliKartlar = new ArrayList<>();
        List<SavasAraclari> secilebilirKartlar = new ArrayList<>();

        for (SavasAraclari kart : bilgisayarKartListesiCopy) {
            if (secilmeyenKartlar.contains(kart)) {
                oncelikliKartlar.add(kart);
            } else {
                secilebilirKartlar.add(kart);
            }
        }


        Collections.shuffle(oncelikliKartlar, random);
        while (secilenKartlar.size() < 3 && !oncelikliKartlar.isEmpty()) {
            SavasAraclari kart = oncelikliKartlar.remove(0);
            secilenKartlar.add(kart);
            secilmeyenKartlar.remove(kart);
            System.out.println("Bilgisayar öncelikli kart seçti: " + kart.getSinif());
        }


        Collections.shuffle(secilebilirKartlar, random);
        while (secilenKartlar.size() < 3 && !secilebilirKartlar.isEmpty()) {
            SavasAraclari kart = secilebilirKartlar.remove(0);
            secilenKartlar.add(kart);
            System.out.println("Bilgisayar rastgele kart seçti: " + kart.getSinif());
        }

        // Bilgisayarın seçtiği kartları konsola yazdır
        System.out.println("\nBilgisayarın Seçtiği Kartlar:");
        for (SavasAraclari kart : secilenKartlar) {
            System.out.println("- " + kart.getSinif() + " (Dayanıklılık: " + kart.getDayaniklilik() + ", Seviye Puani: " + kart.getSeviyePuani() + ")");
        }

        return secilenKartlar;
    }

    public int[] SaldiriHesapla(SavasAraclari oyuncuKart, SavasAraclari bilgisayarKart) {
        int oyuncuSaldiri = oyuncuKart.getVurus();
        int bilgisayarSaldiri = bilgisayarKart.getVurus();


        if (oyuncuKart instanceof Obus) {
            if (bilgisayarKart instanceof Firkateyn) {
                oyuncuSaldiri += ((Obus) oyuncuKart).getDenizVurusAvantaji();
            }
            else if (bilgisayarKart instanceof Sida) {
                oyuncuSaldiri += ((Obus) oyuncuKart).getDenizVurusAvantaji();
            }
        }

        else if (oyuncuKart instanceof KFS) {
            if (bilgisayarKart instanceof Firkateyn) {
                oyuncuSaldiri += ((KFS) oyuncuKart).getDenizVurusAvantaji();
            } else if (bilgisayarKart instanceof Siha) {
                oyuncuSaldiri += ((KFS) oyuncuKart).getHavaVurusAvantaji();
            }
            else if (bilgisayarKart instanceof Sida) {
                oyuncuSaldiri += ((KFS) oyuncuKart).getDenizVurusAvantaji();
            }
            else if (bilgisayarKart instanceof Ucak) {
                oyuncuSaldiri += ((KFS) oyuncuKart).getHavaVurusAvantaji();
            }
        }

        else if (oyuncuKart instanceof Ucak) {
            if (bilgisayarKart instanceof Obus) {
                oyuncuSaldiri += ((Ucak) oyuncuKart).getKaraVurusAvantaji();
            } else if (bilgisayarKart instanceof KFS) {
                oyuncuSaldiri += ((Ucak) oyuncuKart).getKaraVurusAvantaji();
            }
        }

        else if (oyuncuKart instanceof Siha) {
            if (bilgisayarKart instanceof Obus) {
                oyuncuSaldiri += ((Siha) oyuncuKart).getKaraVurusAvantaji();
            } else if (bilgisayarKart instanceof Firkateyn) {
                oyuncuSaldiri += ((Siha) oyuncuKart).getDenizVurusAvantaji();
            }
            else if (bilgisayarKart instanceof Sida) {
                oyuncuSaldiri += ((Siha) oyuncuKart).getDenizVurusAvantaji();
            }
            else if (bilgisayarKart instanceof KFS) {
                oyuncuSaldiri += ((Siha) oyuncuKart).getKaraVurusAvantaji();
            }
        }

        else if (oyuncuKart instanceof Firkateyn) {
            if (bilgisayarKart instanceof Ucak) {
                oyuncuSaldiri += ((Firkateyn) oyuncuKart).getHavaVurusAvantaji();
            } else if (bilgisayarKart instanceof Siha) {
                oyuncuSaldiri += ((Firkateyn) oyuncuKart).getHavaVurusAvantaji();
            }
        }

        else if (oyuncuKart instanceof Sida) {
            if (bilgisayarKart instanceof Ucak) {
                oyuncuSaldiri += ((Sida) oyuncuKart).getHavaVurusAvantaji();
            } else if (bilgisayarKart instanceof Obus) {
                oyuncuSaldiri += ((Sida) oyuncuKart).getKaraVurusAvantaji();
            }
            else if (bilgisayarKart instanceof KFS) {
                oyuncuSaldiri += ((Sida) oyuncuKart).getKaraVurusAvantaji();
            }
            else if (bilgisayarKart instanceof Siha) {
                oyuncuSaldiri += ((Sida) oyuncuKart).getHavaVurusAvantaji();
            }
        }

        if (bilgisayarKart instanceof Obus) {
            if (oyuncuKart instanceof Firkateyn) {
                bilgisayarSaldiri += ((Obus) bilgisayarKart ).getDenizVurusAvantaji();
            }
            else if (oyuncuKart instanceof Sida) {
                bilgisayarSaldiri += ((Obus) bilgisayarKart ).getDenizVurusAvantaji();
            }
        }

        else if (bilgisayarKart instanceof KFS) {
            if (oyuncuKart instanceof Firkateyn) {
                bilgisayarSaldiri += ((KFS) bilgisayarKart ).getDenizVurusAvantaji();
            } else if (oyuncuKart instanceof Siha) {
                bilgisayarSaldiri += ((KFS) bilgisayarKart ).getHavaVurusAvantaji();
            }
            else if (oyuncuKart instanceof Sida) {
                bilgisayarSaldiri += ((KFS) bilgisayarKart ).getDenizVurusAvantaji();
            }
            else if (oyuncuKart instanceof Ucak) {
                bilgisayarSaldiri += ((KFS) bilgisayarKart ).getHavaVurusAvantaji();
            }
        }

        else if (bilgisayarKart instanceof Ucak) {
            if (oyuncuKart instanceof Obus) {
                bilgisayarSaldiri += ((Ucak) bilgisayarKart ).getKaraVurusAvantaji();
            } else if (oyuncuKart instanceof KFS) {
                bilgisayarSaldiri += ((Ucak) bilgisayarKart ).getKaraVurusAvantaji();
            }
        }

        else if (bilgisayarKart instanceof Siha) {
            if (oyuncuKart instanceof Obus) {
                bilgisayarSaldiri+= ((Siha) bilgisayarKart ).getKaraVurusAvantaji();
            } else if (oyuncuKart instanceof Firkateyn) {
                bilgisayarSaldiri += ((Siha) bilgisayarKart ).getDenizVurusAvantaji();
            }
            else if (oyuncuKart instanceof Sida) {
                bilgisayarSaldiri+= ((Siha) bilgisayarKart ).getDenizVurusAvantaji();
            }
            else if (oyuncuKart instanceof KFS) {
                bilgisayarSaldiri += ((Siha) bilgisayarKart ).getKaraVurusAvantaji();
            }
        }

        else if (bilgisayarKart instanceof Firkateyn) {
            if (oyuncuKart instanceof Ucak) {
                bilgisayarSaldiri += ((Firkateyn) bilgisayarKart ).getHavaVurusAvantaji();
            } else if (oyuncuKart instanceof Siha) {
                bilgisayarSaldiri+= ((Firkateyn) bilgisayarKart ).getHavaVurusAvantaji();
            }
        }

        else if (bilgisayarKart instanceof Sida) {
            if (oyuncuKart instanceof Ucak) {
                bilgisayarSaldiri+= ((Sida) bilgisayarKart ).getHavaVurusAvantaji();
            } else if (oyuncuKart instanceof Obus) {
                bilgisayarSaldiri += ((Sida) bilgisayarKart ).getKaraVurusAvantaji();
            }
            else if (oyuncuKart instanceof KFS) {
                bilgisayarSaldiri+= ((Sida) bilgisayarKart ).getKaraVurusAvantaji();
            }
            else if (oyuncuKart instanceof Siha) {
                bilgisayarSaldiri += ((Sida) bilgisayarKart ).getHavaVurusAvantaji();
            }
        }

        return new int[]{oyuncuSaldiri, bilgisayarSaldiri};
    }


    public void yeniKartDagit() {

        Set<SavasAraclari> dagitilmisKartlar = new HashSet<>(oyuncu.getKartListesi());
        dagitilmisKartlar.addAll(bilgisayar.getKartListesi());

        List<SavasAraclari> uygunKartlarOyuncu = getUygunKartlar(oyuncu.getSkor(), dagitilmisKartlar);
        List<SavasAraclari> uygunKartlarBilgisayar = getUygunKartlar(bilgisayar.getSkor(), dagitilmisKartlar);


        if (!uygunKartlarOyuncu.isEmpty()) {
            SavasAraclari yeniKartOyuncu = kartIsminiNumaralandir(uygunKartlarOyuncu.get(random.nextInt(uygunKartlarOyuncu.size())));

            yeniKartOyuncu.setSeviyePuani(seviyePuani);
            oyuncu.kartEkle(yeniKartOyuncu);
            oyuncuOncekiSecilmeyenKartlar.add(yeniKartOyuncu);
            System.out.println("\nOyuncuya yeni bir kart eklendi: " + yeniKartOyuncu.getSinif() + " (Dayanıklılık: " + yeniKartOyuncu.getDayaniklilik() + ", Seviye Puani: " + yeniKartOyuncu.getSeviyePuani() + ")");
        }


        if (!uygunKartlarBilgisayar.isEmpty()) {
            SavasAraclari yeniKartBilgisayar = kartIsminiNumaralandir(uygunKartlarBilgisayar.get(random.nextInt(uygunKartlarBilgisayar.size())));

            yeniKartBilgisayar.setSeviyePuani(seviyePuani);
            yeniKartBilgisayar.setVurus(10);
            bilgisayar.kartEkle(yeniKartBilgisayar);
            bilgisayarOncekiSecilmeyenKartlar.add(yeniKartBilgisayar);
            System.out.println("Bilgisayara yeni bir kart eklendi: " + yeniKartBilgisayar.getSinif() + " (Dayanıklılık: " + yeniKartBilgisayar.getDayaniklilik() + ", Seviye Puani: " + yeniKartBilgisayar.getSeviyePuani() + ")");
        }
    }


    private List<SavasAraclari> getUygunKartlar(int skor, Set<SavasAraclari> dagitilmisKartlar) {
        List<SavasAraclari> uygunKartlar = new ArrayList<>();

        for (SavasAraclari kart : kartHavuzu) {
            if (!oyundanCikartilanKartlar.contains(kart) && !dagitilmisKartlar.contains(kart)) {
                if (skor < 20 && (kart instanceof Obus || kart instanceof Ucak || kart instanceof Firkateyn)) {
                    uygunKartlar.add(kart);
                } else if (skor >= 20) {
                    uygunKartlar.add(kart);
                }
            }
        }
        return uygunKartlar;
    }


    private void oyunuBitir() {
        System.out.println("\nOyun sona erdi!");
        System.out.println("Son Skorlar:");

        System.out.println(oyuncu.getOyuncuAdi() + " Skor: " + oyuncu.getSkor());
        System.out.println(bilgisayar.getOyuncuAdi() + " Skor: " + bilgisayar.getSkor());

        if (oyuncu.getSkor() > bilgisayar.getSkor()) {
            System.out.println("Kazanan: " + oyuncu.getOyuncuAdi());
        } else if (bilgisayar.getSkor() > oyuncu.getSkor()) {
            System.out.println("Kazanan: " + bilgisayar.getOyuncuAdi());
        } else {
            System.out.println("Sonuç: Berabere!");
        }
    }


    private void oyunSonuMesajiGoster() {
        String mesaj;
        if (oyuncu.getSkor() > bilgisayar.getSkor()) {
            mesaj = "Kazanan: " + oyuncu.getOyuncuAdi() + "\n" +
                    oyuncu.getOyuncuAdi() + " Skor: " + oyuncu.getSkor() + "\n" +
                    bilgisayar.getOyuncuAdi() + " Skor: " + bilgisayar.getSkor();
        } else if (bilgisayar.getSkor() > oyuncu.getSkor()) {
            mesaj = "Kazanan: " + bilgisayar.getOyuncuAdi() + "\n" +
                    oyuncu.getOyuncuAdi() + " Skor: " + oyuncu.getSkor() + "\n" +
                    bilgisayar.getOyuncuAdi() + " Skor: " + bilgisayar.getSkor();
        } else {

            int oyuncuToplamDayaniklilik = oyuncu.getKartListesi().stream()
                    .filter(SavasAraclari::isAlive) // Sadece yaşayan kartları dahil et
                    .mapToInt(SavasAraclari::getDayaniklilik)
                    .sum();
            int bilgisayarToplamDayaniklilik = bilgisayar.getKartListesi().stream()
                    .filter(SavasAraclari::isAlive) // Sadece yaşayan kartları dahil et
                    .mapToInt(SavasAraclari::getDayaniklilik)
                    .sum();

            System.out.println("Oyuncu Toplam Dayanıklılık: " + oyuncuToplamDayaniklilik);
            System.out.println("Bilgisayar Toplam Dayanıklılık: " + bilgisayarToplamDayaniklilik);

            if (oyuncuToplamDayaniklilik > bilgisayarToplamDayaniklilik) {
                mesaj = "Sonuç: Berabere! Ama " + oyuncu.getOyuncuAdi() + " toplam dayanıklılıkta üstün!\n" +
                        oyuncu.getOyuncuAdi() + " Skor: " + oyuncu.getSkor() + "\n" +
                        bilgisayar.getOyuncuAdi() + " Skor: " + bilgisayar.getSkor();
            } else if (bilgisayarToplamDayaniklilik > oyuncuToplamDayaniklilik) {
                mesaj = "Sonuç: Berabere! Ama " + bilgisayar.getOyuncuAdi() + " toplam dayanıklılıkta üstün!\n" +
                        oyuncu.getOyuncuAdi() + " Skor: " + oyuncu.getSkor() + "\n" +
                        bilgisayar.getOyuncuAdi() + " Skor: " + bilgisayar.getSkor();
            } else {
                mesaj = "Sonuç: Tamamen Berabere!\n" +
                        oyuncu.getOyuncuAdi() + " Skor: " + oyuncu.getSkor() + "\n" +
                        bilgisayar.getOyuncuAdi() + " Skor: " + bilgisayar.getSkor();
            }
        }


        sonucLabel.setText(mesaj);
        System.out.println("Oyun Sonucu: " + mesaj);


        Alert sonucAlert = new Alert(Alert.AlertType.INFORMATION);
        sonucAlert.initOwner(this.primaryStageRef); // Sahip olarak primaryStage'i ekleyin
        sonucAlert.setTitle("Oyun Bitti");
        sonucAlert.setHeaderText(null);
        sonucAlert.setContentText(mesaj);
        sonucAlert.showAndWait();


        Platform.exit();
    }

    private String getKartListesiAsString(List<SavasAraclari> kartlar) {
        StringBuilder sb = new StringBuilder();
        for (SavasAraclari kart : kartlar) {
            sb.append(kart.getSinif()).append(" (Dayanıklılık: ").append(kart.getDayaniklilik()).append("), ");
        }
        // Son virgülü kaldır
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }


    private SavasAraclari uygunKartDagit() {
        List<SavasAraclari> uygunKartlar = new ArrayList<>();
        for (SavasAraclari kart : kartHavuzu) {
            if (!oyundanCikartilanKartlar.contains(kart) && !oyuncu.getKartListesi().contains(kart) && !bilgisayar.getKartListesi().contains(kart)) {
                uygunKartlar.add(kart);
            }
        }
        if (uygunKartlar.isEmpty()) {
            throw new RuntimeException("Dağıtılacak uygun kart kalmadı!");
        }
        return uygunKartlar.get(random.nextInt(uygunKartlar.size()));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
