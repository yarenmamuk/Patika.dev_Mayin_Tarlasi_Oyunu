import java.util.Random;
import java.util.Scanner;

public class MineSweeper {
    private int satirSayisi;
    private int sutunSayisi;
    private int[][] oyunAlani;
    private boolean[][] mayinAlani;
    private boolean devamEdiyor;

    public MineSweeper(int satirSayisi, int sutunSayisi) {
        this.satirSayisi = satirSayisi;
        this.sutunSayisi = sutunSayisi;
        this.oyunAlani = new int[satirSayisi][sutunSayisi];
        this.mayinAlani = new boolean[satirSayisi][sutunSayisi];
        this.devamEdiyor = true;
        mayinlariYerlestir();
    }

    private void mayinlariYerlestir() {
        Random random = new Random();
        int mayinSayisi = satirSayisi * sutunSayisi / 4;

        while (mayinSayisi > 0) {
            int randomSatir = random.nextInt(satirSayisi);
            int randomSutun = random.nextInt(sutunSayisi);

            if (!mayinAlani[randomSatir][randomSutun]) {
                mayinAlani[randomSatir][randomSutun] = true;
                mayinSayisi--;
            }
        }
    }

    private boolean sinirlariKontrolEt(int satir, int sutun) {
        return satir >= 0 && satir < satirSayisi && sutun >= 0 && sutun < sutunSayisi;
    }

    private void oyunTahtasiniGoster(boolean gizliMi) {
        System.out.println("Mayın Tarlası Oyunu\n");
        for (int i = 0; i < satirSayisi; i++) {
            for (int j = 0; j < sutunSayisi; j++) {
                if (gizliMi) {
                    System.out.print("-\t");
                } else {
                    if (mayinAlani[i][j]) {
                        System.out.print("X\t");
                    } else {
                        System.out.print(oyunAlani[i][j] + "\t");
                    }
                }
            }
            System.out.println();
        }
    }

    private void oyunuOynat() {
        Scanner scanner = new Scanner(System.in);

        while (devamEdiyor) {
            oyunTahtasiniGoster(false);
            System.out.print("\nSatır seçin: ");
            int satir = scanner.nextInt() - 1;
            System.out.print("Sütun seçin: ");
            int sutun = scanner.nextInt() - 1;

            if (!sinirlariKontrolEt(satir, sutun)) {
                System.out.println("Geçersiz bir nokta seçtiniz. Lütfen tekrar deneyin.");
                continue;
            }

            if (mayinAlani[satir][sutun]) {
                System.out.println("Mayına bastınız! Oyunu kaybettiniz.");
                devamEdiyor = false;
            } else {
                int etrafdakiMayinSayisi = etrafdakiMayinSayisiniBul(satir, sutun);
                oyunAlani[satir][sutun] = etrafdakiMayinSayisi;

                if (etrafdakiMayinSayisi == 0) {
                    mayinlariAc(satir, sutun);
                }

                kazandinMi();
            }
        }

        oyunTahtasiniGoster(false);
    }

    private int etrafdakiMayinSayisiniBul(int satir, int sutun) {
        int sayac = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                int yeniSatir = satir + i;
                int yeniSutun = sutun + j;

                if (sinirlariKontrolEt(yeniSatir, yeniSutun) && mayinAlani[yeniSatir][yeniSutun]) {
                    sayac++;
                }
            }
        }

        return sayac;
    }

    private void mayinlariAc(int satir, int sutun) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int yeniSatir = satir + i;
                int yeniSutun = sutun + j;

                if (sinirlariKontrolEt(yeniSatir, yeniSutun) && oyunAlani[yeniSatir][yeniSutun] == 0) {
                    oyunAlani[yeniSatir][yeniSutun] = etrafdakiMayinSayisiniBul(yeniSatir, yeniSutun);
                    if (oyunAlani[yeniSatir][yeniSutun] == 0) {
                        mayinlariAc(yeniSatir, yeniSutun);
                    }
                }
            }
        }
    }

    private void kazandinMi() {
        int acilanNoktaSayisi = 0;
        for (int i = 0; i < satirSayisi; i++) {
            for (int j = 0; j < sutunSayisi; j++) {
                if (oyunAlani[i][j] != 0 || mayinAlani[i][j]) {
                    acilanNoktaSayisi++;
                }
            }
        }

        if (acilanNoktaSayisi == satirSayisi * sutunSayisi) {
            System.out.println("Tebrikler! Oyunu kazandınız.");
            devamEdiyor = false;
        }
    }

    public void oyunuBaslat() {
        oyunuOynat();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Satır sayısını girin: ");
        int satirSayisi = scanner.nextInt();
        System.out.print("Sütun sayısını girin: ");
        int sutunSayisi = scanner.nextInt();

        MineSweeper mayinTarlası = new MineSweeper(satirSayisi, sutunSayisi);
        mayinTarlası.oyunuBaslat();
    }
}
